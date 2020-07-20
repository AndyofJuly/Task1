package com.game.system.bag;

import com.game.common.Const;
import com.game.system.achievement.observer.BestEquipOb;
import com.game.system.achievement.observer.SumMoneyOb;
import com.game.system.achievement.subject.Subject;
import com.game.system.assist.AssistService;
import com.game.system.role.pojo.Role;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 背包模块的业务逻辑处理
 * @Author andy
 * @create 2020/6/17 12:11
 */
@Service
public class PackageService {

    /**
     * 将物品放入背包
     * @param goodsId 物品id
     * @param number 数量
     * @param role 角色
     * @return boolean
     */
    public boolean putIntoPackage(int goodsId,int number,Role role){
        if(!role.getMyPackageBo().checkIfCanPut(goodsId, number)){
            System.out.println("背包放不下了-测试");
            return false;
        }

        if(role.getMyPackageBo().getGoodsHashMap().get(goodsId)!=null){
            int allAmount = role.getMyPackageBo().getGoodsHashMap().get(goodsId)+number;
            role.getMyPackageBo().getGoodsHashMap().put(goodsId,allAmount);
        }else {
            role.getMyPackageBo().getGoodsHashMap().put(goodsId,number);
        }

        //Subject.notifyObservers(goodsId,role,bestEquipOb);
        if(String.valueOf(goodsId).startsWith(Const.EQUIPMENT_HEAD)){
            bagSubject.notifyObserver(AssistService.getStaticEquipId(goodsId),role);
        }
        return true;
    }

    /**
     * 将物品从背包中取出
     * @param goodsId 物品id
     * @param number 数量
     * @param role 角色
     */
    public boolean getFromPackage(int goodsId,int number,Role role){
        int leftAmount = role.getMyPackageBo().getGoodsHashMap().get(goodsId)-number;
        if(leftAmount<0){
            System.out.println("背包中无法拿出这么多物品-测试用");
            return false;
        }else{
            role.getMyPackageBo().getGoodsHashMap().put(goodsId,leftAmount);
            role.getMyPackageBo().orderPackageGrid();
            return true;
        }
    }

    /**
     * 角色金钱增加
     * @param number 金钱数量
     * @param role 角色
     */
    public void addMoney(int number,Role role){
        role.setMoney(role.getMoney()+number);
        //Subject.notifyObservers(number,role,sumMoneyOb);
        bagSubject.notifyObserver(number,role);
    }

    /**
     * 角色金钱减少
     * @param number 金钱数量
     * @param role 角色
     */
    public boolean lostMoney(int number,Role role){
        int leftMoney = role.getMoney()-number;
        if(leftMoney<0){
            return false;
        }
        role.setMoney(leftMoney);
        return true;
    }

    /*    static {
            BestEquipSB.registerObserver(new BestEquipOb());
            SumMoneySB.registerObserver(new SumMoneyOb());
        }*/
    Subject bagSubject = new Subject();
    private BestEquipOb bestEquipOb = new BestEquipOb(bagSubject);
    private SumMoneyOb sumMoneyOb = new SumMoneyOb(bagSubject);

}
