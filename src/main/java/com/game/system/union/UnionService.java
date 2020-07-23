package com.game.system.union;

import com.game.netty.server.ServerHandler;
import com.game.system.achievement.observer.FsJoinUnionOb;
import com.game.system.achievement.entity.Subject;
import com.game.system.bag.PackageService;
import com.game.common.Const;
import com.game.system.role.entity.Role;
import com.game.system.union.entity.JobResource;
import com.game.system.union.entity.Union;
import com.game.system.gameserver.AssistService;
import com.game.system.gameserver.GlobalInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 公会模块的业务逻辑处理
 * @Author andy
 * @create 2020/6/28 14:27
 */
@Service
public class UnionService {

    /** 所有的公会集合，key为unionId，value为Union */
    public static HashMap<Integer,Union> unionHashMap = new HashMap<>();

    @Autowired UnionDao unionDao;
    private PackageService packageService = PackageService.getInstance();

    /**
     * 创建公会，创建者获得最高权限-1，职务为会长
     * @param unionName 公会名
     * @param role 角色
     * @return 信息提示
     */
    public String createUnion(String unionName,Role role){
        int unionId = AssistService.getInstance().generateUnionId();
        Union union = new Union(unionId,unionName);
        union.getRoleJobHashMap().put(role.getId(),1);
        role.setUnionId(unionId);
        unionHashMap.put(unionId,union);
        unionSubject.notifyObserver(0,role);
        return String.valueOf(unionId);
    }

    /**
     * 解散公会
     * @param role 角色
     * @return 信息提示
     */
    public String disbandUnion(Role role){
        int unionId = role.getUnionId();
        int jobId = unionHashMap.get(unionId).getRoleJobHashMap().get(role.getId());
        int grade = JobResource.getJobStaticHashMap().get(jobId).getGrade();
        if(grade!= Const.Union.FIRST_GRADE){
            return Const.Union.NO_PERMISSION;
        }
        ServerHandler.notifyGroupRoles(getRoles(unionId),role.getName()+Const.Union.DISBAND_SUCCESS);
        for(Integer roleId : unionHashMap.get(unionId).getRoleJobHashMap().keySet()){
            Role role1 = GlobalInfo.getRoleHashMap().get(roleId);
            unionDao.deleteRole(role1);
            role1.setUnionId(0);
        }
        unionHashMap.remove(unionId);
        return Const.Union.DISBAND_SUCCESS;
    }

    /**
     * 对某位公会成员任职
     * @param memberId 成员id
     * @param authorityLevel 职务对应的权限等级
     * @param role 角色
     * @return 信息提示
     */
    public String appointCareer(int memberId,int authorityLevel,Role role){
        int unionId = role.getUnionId();
        int jobId = unionHashMap.get(unionId).getRoleJobHashMap().get(role.getId());
        int grade = JobResource.getJobStaticHashMap().get(jobId).getGrade();
        if(grade >Const.Union.SECOND_GRADE || grade>=authorityLevel){
            return Const.Union.NO_PERMISSION;
        }
        Union union = unionHashMap.get(unionId);
        union.getRoleJobHashMap().put(memberId,authorityLevel);
        return Const.Union.APPOINTMENT_SUCCESS;
    }

    /**
     * 申请参加公会，放入申请列表中
     * @param unionId 公会id
     * @param role 角色
     */
    public void applyFor(int unionId,Role role){
        if(role.getUnionId()!=0){return;}
        unionHashMap.get(unionId).getApplyRoleList().add(role.getId());
    }

    /**
     * 离开公会
     * @param role 角色
     */
    public void leaveUnion(Role role){
        int unionId = role.getUnionId();
        unionHashMap.get(unionId).getRoleJobHashMap().remove(role.getId());
        role.setUnionId(0);
    }

    /**
     * 获取申请列表
     * @param role 角色
     * @return 信息提示
     */
    public String getApplyList(Role role){
        StringBuilder stringBuilder = new StringBuilder("申请人有：");
        for(Integer roleId : unionHashMap.get(role.getUnionId()).getApplyRoleList()){
            stringBuilder.append(roleId+" ");
        }
        return stringBuilder.toString();
    }


    /**
     * 同意入会申请
     * @param applyRoleId 申请人id
     * @param role 角色
     * @return 信息提示
     */
    public String agreeApply(int applyRoleId,Role role){
        int unionId = role.getUnionId();
        int jobId = unionHashMap.get(unionId).getRoleJobHashMap().get(role.getId());
        int grade = JobResource.getJobStaticHashMap().get(jobId).getGrade();
        if(grade >Const.Union.THRID_GRADE){
            return Const.Union.NO_PERMISSION;
        }
        unionHashMap.get(unionId).getApplyRoleList().remove(Integer.valueOf(applyRoleId));
        Role member = GlobalInfo.getRoleHashMap().get(applyRoleId);
        member.setUnionId(unionId);
        unionHashMap.get(unionId).getRoleJobHashMap().put(member.getId(),4);

        unionSubject.notifyObserver(0,member);

        ServerHandler.notifyGroupRoles(getRoles(unionId),role.getName()+"已批准"+member.getName()+"入会");
        return Const.Union.AGREE_SUCCESS;
    }

    /**
     * 开除某位公会成员
     * @param memberId 成员id
     * @param role 角色
     * @return 信息提示
     */
    public String fireMember(Integer memberId,Role role){
        int unionId = role.getUnionId();
        //查看执行此操作的人是否有该权限，是否为该公会的人，设置的权限时否越级
        int jobId = unionHashMap.get(unionId).getRoleJobHashMap().get(role.getId());
        int grade = JobResource.getJobStaticHashMap().get(jobId).getGrade();
        Role member = GlobalInfo.getRoleHashMap().get(memberId);
        int memberJobId = unionHashMap.get(unionId).getRoleJobHashMap().get(memberId);
        int memberGrade = JobResource.getJobStaticHashMap().get(memberJobId).getGrade();
        if(grade >Const.Union.THRID_GRADE|| grade>=memberGrade){
            return Const.Union.NO_PERMISSION;
        }
        member.setUnionId(0);
        unionHashMap.get(unionId).getRoleJobHashMap().remove(memberId);

        ServerHandler.notifyGroupRoles(getRoles(unionId),role.getName()+"已开除"+member.getName());
        return Const.Union.FIRE_SUCCESS;
    }

    /** 查看权限，待做，可以对前面重复代码进行抽取 */
    private void checkAuthority(){
        //todo
    }

    /**
     * 捐款给公会
     * @param money 捐赠金钱
     * @param role 角色
     * @return 信息提示
     */
    public String donateMoney(int money,Role role){//int unionId,
        int unionId = role.getUnionId();
        if(!packageService.lostMoney(money,role)){
            return Const.Union.OUT_OF_MONEY;
        }
        int sumMoney = unionHashMap.get(unionId).getMoney()+money;
        unionHashMap.get(unionId).setMoney(sumMoney);
        return Const.Union.DONATE_SUCCESS;
    }

    /**
     * 捐赠物品
     * @param goodsId 捐赠物品id
     * @param number 捐赠数量
     * @param role 角色
     * @return 信息提示
     */
    public String donateGoods(int goodsId,int number,Role role){//int unionId,
        int unionId = role.getUnionId();
        if(!packageService.getFromPackage(goodsId,number,role)){
            return Const.Union.OUT_OF_GOODS;
        }

        if(unionHashMap.get(unionId).getGoodsHashMap().get(goodsId)!=null){
            int num = unionHashMap.get(unionId).getGoodsHashMap().get(goodsId)+number;
            unionHashMap.get(unionId).getGoodsHashMap().put(goodsId,num);
        }else {
            unionHashMap.get(unionId).getGoodsHashMap().put(goodsId,number);
        }

        return Const.Union.DONATE_GOODS_SUCCESS;
    }

    /**
     * 拿取公会物品
     * @param goodsId 物品id
     * @param role 角色
     * @return 信息提示
     */
    public String getGoods(int goodsId,Role role){//int unionId,
        int unionId = role.getUnionId();
        packageService.putIntoPackage(goodsId,1,role);
        int num = unionHashMap.get(unionId).getGoodsHashMap().get(goodsId)-1;
        if(num<0){
            return Const.Union.NOT_FOUND_GOODS;
        }
        unionHashMap.get(unionId).getGoodsHashMap().put(goodsId,num);
        return Const.Union.GET_GOODS_SUCCESS;
    }

    /**
     * 获得公会详细信息，公会成员列表和职务
     * @param role 角色
     * @return 信息提示
     */
    public String getUnionInfo(Role role){
        int unionId = role.getUnionId();
        StringBuilder list = new StringBuilder();
        if(unionId==0){
            return "null";
        }
        for(Integer memberId : unionHashMap.get(unionId).getRoleJobHashMap().keySet()){
            Role member = GlobalInfo.getRoleHashMap().get(memberId);
            int jobId = unionHashMap.get(unionId).getRoleJobHashMap().get(memberId);
            list.append("角色：").append(member.getName()).append("，职务：").append(JobResource.getJobStaticHashMap().get(jobId).getName())
                    .append("，权限等级：").append(JobResource.getJobStaticHashMap().get(jobId).getGrade()).append("； ");
        }
        list.append("\n物品有： ");
        for(Integer goodsId : unionHashMap.get(unionId).getGoodsHashMap().keySet()){
            list.append(goodsId+"，数量为"+unionHashMap.get(unionId).getGoodsHashMap().get(goodsId)+"。 ");
        }
        list.append("\n金钱有："+unionHashMap.get(unionId).getMoney());
        return list.toString();
    }

    /**
     * 获得公会成员集合
     * @param unionId 公会id
     * @return 公会成员集合
     */
    private ArrayList<Role> getRoles(int unionId){
        ArrayList<Role> roles = new ArrayList<>();
        for(Integer memberId : unionHashMap.get(unionId).getRoleJobHashMap().keySet()){
            roles.add(GlobalInfo.getRoleHashMap().get(memberId));
        }
        return roles;
    }

    /**
     * 获得所有公会列表
     * @return 信息提示
     */
    public String getUnionList() {
        StringBuilder list = new StringBuilder();
        for(Integer unionId : unionHashMap.keySet()){
            Union union = unionHashMap.get(unionId);
            list.append(unionId+"-"+union.getName()+"\n");
        }
        return list.toString();
    }

    /** 注册成就观察者 */
    Subject unionSubject = new Subject();
    FsJoinUnionOb fsJoinUnionOb = new FsJoinUnionOb(unionSubject);
}
