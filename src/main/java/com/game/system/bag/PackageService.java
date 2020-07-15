package com.game.system.bag;

import com.game.common.Const;
import com.game.system.achievement.observer.BestEquipOB;
import com.game.system.achievement.observer.SumMoneyOB;
import com.game.system.achievement.subject.BestEquipSB;
import com.game.system.achievement.subject.SumMoneySB;
import com.game.system.role.pojo.Role;

import java.util.HashMap;

/**
 * 背包的服务
 * @Author andy
 * @create 2020/6/17 12:11
 */
public class PackageService {

    /**
     * 放入背包，服务端使用
     * @param goodsId 物品id
     * @param number 数量
     * @param role 角色
     * @return boolean
     */
    public boolean putIntoPackage(int goodsId,int number,Role role){
        if(role.getMyPackageBo().checkIfCanPut(goodsId,number)==false){
            System.out.println("背包放不下了-测试");
            return false;
        }

        //如果背包中有该key，则数量叠加，如果getkey==null则新增该key，并设置数量
        if(role.getMyPackageBo().getGoodsHashMap().get(goodsId)!=null){
            int allAmount = role.getMyPackageBo().getGoodsHashMap().get(goodsId)+number;
                role.getMyPackageBo().getGoodsHashMap().put(goodsId,allAmount);
        }else {  //没有该物品，直接放入背包
            role.getMyPackageBo().getGoodsHashMap().put(goodsId,number);
        }

        //成就
        BestEquipSB.notifyObservers(goodsId,role);
        return true;
    }

    /**
     * 从背包中取出物品
     * @param goodsId 物品id
     * @param number 数量
     * @param role 角色
     */
    public boolean getFromPackage(int goodsId,int number,Role role){
        int leftAmount = role.getMyPackageBo().getGoodsHashMap().get(goodsId)-number;
        if(role.getMyPackageBo().getGoodsHashMap().get(goodsId)<0){
            System.out.println("背包中无法拿出这么多物品-测试用");
            return false;
        }else{
            role.getMyPackageBo().getGoodsHashMap().put(goodsId,leftAmount);
            return true;
        }
    }

    /**
     * 背包减少该物品
     * @param goodsId 物品id
     * @param role 角色
     */
/*    public void lostGoods(int goodsId,int num,Role role){//int number,
        int nowNum = role.getMyPackageBo().getGoodsHashMap().get(goodsId);
        role.getMyPackageBo().getGoodsHashMap().put(goodsId,nowNum-num);
    }*/

    /**
     * 角色金钱增加
     * @param number 金钱数量
     * @param role 角色
     */
    public void addMoney(int number,Role role){
        role.setMoney(role.getMoney()+number);
        //iAchieveService.ifSumMoneyToThousand(number,role);
        SumMoneySB.notifyObservers(number,role);

    }

    /**
     * 角色金钱减少
     * @param number 金钱数量
     * @param role 角色
     */
    public void lostMoney(int number,Role role){
        role.setMoney(role.getMoney()-number);
    }

    static {
        BestEquipSB.registerObserver(new BestEquipOB());
        SumMoneySB.registerObserver(new SumMoneyOB());
    }

}
