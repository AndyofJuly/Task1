package com.game.service;

import com.game.common.Const;
import com.game.entity.Role;
import com.game.entity.store.JobResource;
import com.game.entity.vo.Union;
import com.game.service.assis.GlobalResource;
import com.game.service.assis.IdGenerator;

import java.util.HashMap;

/**
 * 公会
 * @Author andy
 * @create 2020/6/28 14:27
 */
public class UnionService {
    //unionId和对应的union
    public static HashMap<Integer,Union> unionHashMap = new HashMap<>();
    static int id = 1;
    //创建公会
    public String createUnion(String unionName,Role role){
        //该角色扣80银
        //在该公会中，llPointerException
        //	at com.game.service.UnionService.donateMo角色权限为1，名字为会长，拥有所有权限
        int unionId = IdGenerator.generateUnionId();
        Union union = new Union(unionId,unionName);
        union.getRoleJobHashMap().put(role,1);
        role.setUnionId(unionId);
        unionHashMap.put(unionId,union);
        AchievementService.ifFirstJoinUnion(role);
        return unionId+"";
    }

    //解散公会
    public void disbandUnion(int unionId,Role role){
        //条件该公会是角色自己的公会，且权限为1
        int jobId = unionHashMap.get(unionId).getRoleJobHashMap().get(role);
        int grade = JobResource.getJobStaticHashMap().get(jobId).getGrade();
        if(grade!=1 || role.getUnionId()!=unionId){
            System.out.println("你没有该权限");
        }
        //该union中的所有角色，其unionId设为空
        for(Role everyOne : unionHashMap.get(unionId).getRoleJobHashMap().keySet()){
            everyOne.setUnionId(0);
        }
        //清理公会列表中该公会remove
        unionHashMap.remove(unionId);
    }

    //任职
    public void appointCareer(int unionId,int memberId,int authorityLevel,Role role){
        //查看执行此操作的人是否有该权限，是否为该公会的人，设置的权限时否越级
        int jobId = unionHashMap.get(unionId).getRoleJobHashMap().get(role);
        int grade = JobResource.getJobStaticHashMap().get(jobId).getGrade();
        if(grade >2 || role.getUnionId()!=unionId || grade>=authorityLevel){
            System.out.println("你没有该权限");
        }
        //给目标重新设置权限级别-包括公会集合和自身属性修改
        Role member = GlobalResource.getRoleHashMap().get(memberId);
        Union union = unionHashMap.get(unionId);
        union.getRoleJobHashMap().put(member,authorityLevel);
        //member.setUnionId(unionId);
        //union.setRoleAuthority(member.getUnion().getRoleAuthority());
    }

    //入会申请
    public void applyFor(int unionId,int roleId){
        //放入申请列表中（还可以通知所有包含权限的人，告知有申请）
        unionHashMap.get(unionId).getRoleList().add(roleId);
    }

    //批准入会申请
    public void agreeApply(int unionId,Integer applyRoleId,Role role){
        //查看执行此操作的人是否有该权限，是否为该公会的人，设置的权限时否越级
        int jobId = unionHashMap.get(unionId).getRoleJobHashMap().get(role);
        int grade = JobResource.getJobStaticHashMap().get(jobId).getGrade();
        if(grade >3 || role.getUnionId()!=unionId){
            System.out.println("你没有该权限");
        }
        unionHashMap.get(unionId).getRoleList().remove(applyRoleId);
        Role member = GlobalResource.getRoleHashMap().get(applyRoleId);
        member.setUnionId(unionId);
        AchievementService.ifFirstJoinUnion(member);
    }

    //开除
    public void fireMember(int unionId,Integer memberId,Role role){
        //查看执行此操作的人是否有该权限，是否为该公会的人，设置的权限时否越级
        int jobId = unionHashMap.get(unionId).getRoleJobHashMap().get(role);
        int grade = JobResource.getJobStaticHashMap().get(jobId).getGrade();
        Role member = GlobalResource.getRoleHashMap().get(memberId);
        int memberJobId = unionHashMap.get(unionId).getRoleJobHashMap().get(member);
        int memberGrade = JobResource.getJobStaticHashMap().get(memberJobId).getGrade();
        if(grade >3|| role.getUnionId()!=unionId || grade>=memberGrade){
            System.out.println("你没有该权限");
        }
        //角色的union设为空，公会集合中移除该角色
        member.setUnionId(0);
        unionHashMap.get(unionId).getRoleJobHashMap().remove(member);
    }

    //捐款-钱
    public void donateMoney(int unionId,int money,Role role){
        //角色扣钱
        role.setMoney(role.getMoney()-money);
        //公会仓库加钱
        int sumMoney = unionHashMap.get(unionId).getMoney()+money;
        unionHashMap.get(unionId).setMoney(sumMoney);
    }

    //捐款-道具
    public void donateGoods(int unionId,int goodsId,Role role){
        //角色扣道具（可以抽象为一个方法，扣减时进行下限判断）
        int number = role.getMyPackage().getGoodsHashMap().get(goodsId)-1;
        role.getMyPackage().getGoodsHashMap().put(goodsId,number);
        //公会仓库加道具
        if(unionHashMap.get(unionId).getGoodsHashMap().get(goodsId)!=null){
            int num = unionHashMap.get(unionId).getGoodsHashMap().get(goodsId)+1;
            unionHashMap.get(unionId).getGoodsHashMap().put(goodsId,num);
        }else {  //没有该物品，直接放入仓库
            unionHashMap.get(unionId).getGoodsHashMap().put(goodsId,1);
        }
    }

    //获取仓库物品-加入到自己背包中-可扩展为任意数量
    public void getGoods(int unionId,int goodsId,Role role){
        PackageService packageService = new PackageService();
        packageService.putIntoPackage(goodsId,1,role);
        int num = unionHashMap.get(unionId).getGoodsHashMap().get(goodsId)-1;
        unionHashMap.get(unionId).getGoodsHashMap().put(goodsId,num);
    }

    //获取公会成员列表和职务
    public String getUnionInfo(Role role){
        int unionId = role.getUnionId();
        String list = "";
        if(unionId==0){
            return "null";
        }
        for(Role member : unionHashMap.get(unionId).getRoleJobHashMap().keySet()){
            int jobId = unionHashMap.get(unionId).getRoleJobHashMap().get(member);
            list+="角色："+member.getName()+"，职务："+JobResource.getJobStaticHashMap().get(jobId).getName()+"； ";
        }
        return list;
    }
}
