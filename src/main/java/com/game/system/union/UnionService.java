package com.game.system.union;

import com.game.netty.server.ServerHandler;
import com.game.system.achievement.observer.FsJoinUnionOb;
import com.game.system.achievement.pojo.Subject;
import com.game.system.bag.PackageService;
import com.game.common.Const;
import com.game.system.role.pojo.Role;
import com.game.system.union.pojo.JobResource;
import com.game.system.union.pojo.Union;
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

    @Autowired
    private PackageService packageService;

    /**
     * 创建公会，创建者获得最高权限-1，职务为会长
     * @param unionName 公会名
     * @param role 角色
     * @return 信息提示
     */
    public String createUnion(String unionName,Role role){
        int unionId = AssistService.generateUnionId();
        Union union = new Union(unionId,unionName);
        union.getRoleJobHashMap().put(role.getId(),1);
        role.setUnionId(unionId);
        unionHashMap.put(unionId,union);
        unionSubject.notifyObserver(0,role);
        return String.valueOf(unionId);
    }

    /**
     * 解散公会
     * @param unionId 公会id
     * @param role 角色
     * @return 信息提示
     */
    public String disbandUnion(int unionId,Role role){
        int jobId = unionHashMap.get(unionId).getRoleJobHashMap().get(role.getId());
        int grade = JobResource.getJobStaticHashMap().get(jobId).getGrade();
        if(grade!= Const.union.FIRST_GRADE || role.getUnionId()!=unionId){
            return "你没有该权限";
        }
        ServerHandler.notifyGroupRoles(getRoles(unionId),role.getName()+"已解散工会");
        for(Integer roleId : unionHashMap.get(unionId).getRoleJobHashMap().keySet()){
            Role role1 = GlobalInfo.getRoleHashMap().get(roleId);
            role1.setUnionId(0);
        }
        unionHashMap.remove(unionId);
        return "解散公会成功";
    }

    /**
     * 对某位公会成员任职
     * @param unionId 公会id
     * @param memberId 成员id
     * @param authorityLevel 职务对应的权限等级
     * @param role 角色
     * @return 信息提示
     */
    public String appointCareer(int unionId,int memberId,int authorityLevel,Role role){
        int jobId = unionHashMap.get(unionId).getRoleJobHashMap().get(role.getId());
        int grade = JobResource.getJobStaticHashMap().get(jobId).getGrade();
        if(grade >Const.union.SECOND_GRADE || role.getUnionId()!=unionId || grade>=authorityLevel){
            return"你没有该权限";
        }
        Union union = unionHashMap.get(unionId);
        union.getRoleJobHashMap().put(memberId,authorityLevel);
        return "已任命该角色";
    }

    /**
     * 申请参加公会，放入申请列表中
     * @param unionId 公会id
     * @param role 角色
     */
    public void applyFor(int unionId,Role role){
        unionHashMap.get(unionId).getRoleList().add(role.getId());
    }

    /**
     * 同意入会申请
     * @param unionId 公会id
     * @param applyRoleId 申请人id
     * @param role 角色
     * @return 信息提示
     */
    public String agreeApply(int unionId,int applyRoleId,Role role){
        int jobId = unionHashMap.get(unionId).getRoleJobHashMap().get(role.getId());
        int grade = JobResource.getJobStaticHashMap().get(jobId).getGrade();
        if(grade >Const.union.THRID_GRADE|| role.getUnionId()!=unionId){
            return "你没有该权限";
        }
        unionHashMap.get(unionId).getRoleList().remove(Integer.valueOf(applyRoleId));
        Role member = GlobalInfo.getRoleHashMap().get(applyRoleId);
        member.setUnionId(unionId);

        unionSubject.notifyObserver(0,role);

        ServerHandler.notifyGroupRoles(getRoles(unionId),role.getName()+"已批准"+member.getName()+"入会");
        return "已批准该角色入会";
    }

    /**
     * 开除某位公会成员
     * @param unionId 公会id
     * @param memberId 成员id
     * @param role 角色
     * @return 信息提示
     */
    public String fireMember(int unionId,Integer memberId,Role role){
        //查看执行此操作的人是否有该权限，是否为该公会的人，设置的权限时否越级
        int jobId = unionHashMap.get(unionId).getRoleJobHashMap().get(role.getId());
        int grade = JobResource.getJobStaticHashMap().get(jobId).getGrade();
        Role member = GlobalInfo.getRoleHashMap().get(memberId);
        int memberJobId = unionHashMap.get(unionId).getRoleJobHashMap().get(memberId);
        int memberGrade = JobResource.getJobStaticHashMap().get(memberJobId).getGrade();
        if(grade >Const.union.THRID_GRADE|| role.getUnionId()!=unionId || grade>=memberGrade){
            return "你没有该权限";
        }
        member.setUnionId(0);
        unionHashMap.get(unionId).getRoleJobHashMap().remove(memberId);

        ServerHandler.notifyGroupRoles(getRoles(unionId),role.getName()+"已开除"+member.getName());
        return "已开除该角色";
    }

    /** 查看权限，待做，可以对前面共同代码进行抽取 */
    private void checkAuthority(){
        //todo
    }

    /**
     * 捐款给公会
     * @param unionId 公会id
     * @param money 捐赠金钱
     * @param role 角色
     * @return 信息提示
     */
    public String donateMoney(int unionId,int money,Role role){
        if(!packageService.lostMoney(money,role)){
            return "你没有这么多钱";
        }
        int sumMoney = unionHashMap.get(unionId).getMoney()+money;
        unionHashMap.get(unionId).setMoney(sumMoney);
        return "已捐赠银两";
    }

    /**
     * 捐赠物品
     * @param unionId 公会id
     * @param goodsId 捐赠物品id
     * @param number 捐赠数量
     * @param role 角色
     * @return 信息提示
     */
    public String donateGoods(int unionId,int goodsId,int number,Role role){
        if(!packageService.getFromPackage(goodsId,number,role)){
            return "背包中没有这么多物品";
        }

        if(unionHashMap.get(unionId).getGoodsHashMap().get(goodsId)!=null){
            int num = unionHashMap.get(unionId).getGoodsHashMap().get(goodsId)+number;
            unionHashMap.get(unionId).getGoodsHashMap().put(goodsId,num);
        }else {
            unionHashMap.get(unionId).getGoodsHashMap().put(goodsId,number);
        }

        return "已捐赠该道具";
    }

    /**
     * 拿取公会物品
     * @param unionId 公会id
     * @param goodsId 物品id
     * @param role 角色
     * @return 信息提示
     */
    public String getGoods(int unionId,int goodsId,Role role){
        packageService.putIntoPackage(goodsId,1,role);
        int num = unionHashMap.get(unionId).getGoodsHashMap().get(goodsId)-1;
        if(num<0){
            return "仓库中没有该物品";
        }
        unionHashMap.get(unionId).getGoodsHashMap().put(goodsId,num);
        return "已拿取该道具";
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
            list.append("角色：").append(member.getName()).append("，职务：").append(JobResource.getJobStaticHashMap().get(jobId).getName()).append("； ");
        }
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

    /** 注册成就观察者 */
    Subject unionSubject = new Subject();
    FsJoinUnionOb fsJoinUnionOb = new FsJoinUnionOb(unionSubject);
}
