package com.game.system.bag;

import com.game.system.achievement.AchieveServiceImpl;
import com.game.common.Const;
import com.game.system.achievement.observer.BestEquipOB;
import com.game.system.achievement.observer.SumMoneyOB;
import com.game.system.achievement.observer.TalkNpcOB;
import com.game.system.achievement.subject.BestEquipSB;
import com.game.system.achievement.subject.SumMoneySB;
import com.game.system.achievement.subject.TalkNpcSB;
import com.game.system.role.pojo.Role;
import com.game.system.achievement.IAchieveService;

/**
 * 背包的服务
 * @Author andy
 * @create 2020/6/17 12:11
 */
public class PackageServiceImpl implements IPackageService {

    private IAchieveService iAchieveService = new AchieveServiceImpl();

    @Override
    public boolean putIntoPackage(int goodsId,int number,Role role){
        if(role.getMyPackageBo().checkIfCanPut(goodsId,number)==false){
            System.out.println("背包放不下了");
            return false;
        }
        //不再限制getGoodsHashMap()集合中药品和装备上限，改为在格子中限制，如果药品大于上限，则将多余的拆分放到第二个格子中
        if((goodsId+"").startsWith(Const.POTION_HEAD)){ //药品情况
            //如果背包中有该key，则数量叠加，如果getkey==null则新增该key，并设置数量
            if(role.getMyPackageBo().getGoodsHashMap().get(goodsId)!=null){
                int allAmount = role.getMyPackageBo().getGoodsHashMap().get(goodsId)+number;
                    role.getMyPackageBo().getGoodsHashMap().put(goodsId,allAmount);
            }else {  //没有该物品，直接放入背包
                role.getMyPackageBo().getGoodsHashMap().put(goodsId,number);
            }
            System.out.println("获得药品！");
        }else{ //装备情况
            //装备类，如果有相同的装备，不操作，如果装备不同，数量+1
            if(role.getMyPackageBo().getGoodsHashMap().get(goodsId)!=null) {
                int allAmount = role.getMyPackageBo().getGoodsHashMap().get(goodsId)+number;
                role.getMyPackageBo().getGoodsHashMap().put(goodsId, allAmount);
            }else {
                role.getMyPackageBo().getGoodsHashMap().put(goodsId, number);
            }
            //iAchieveService.countBestEquipment(goodsId, role);
            BestEquipSB.notifyObservers(goodsId,role);
            System.out.println("获得装备！");
        }
        return true;
    }

    // todo 待引用
    @Override
    public void getFromPackage(int goodsId,int number,Role role){
        if((goodsId+"").startsWith(Const.POTION_HEAD)){ //药品情况
            int leftAmount = role.getMyPackageBo().getGoodsHashMap().get(goodsId)-number;
            if(role.getMyPackageBo().getGoodsHashMap().get(goodsId)<0){
                System.out.println("错误，没有该物品了");
            }else{
                role.getMyPackageBo().getGoodsHashMap().put(goodsId,leftAmount);
            }
        }else{ //装备一次只能拿一件
            if(role.getMyPackageBo().getGoodsHashMap().get(goodsId)<=0){
                System.out.println("错误，没有该物品了");
            }else{
                role.getMyPackageBo().getGoodsHashMap().put(goodsId,0);
            }
        }
    }

    @Override
    public void addMoney(int number,Role role){
        role.setMoney(role.getMoney()+number);
        //iAchieveService.ifSumMoneyToThousand(number,role);
        SumMoneySB.notifyObservers(number,role);

    }

    @Override
    public void lostMoney(int number,Role role){
        role.setMoney(role.getMoney()-number);
    }

    @Override
    public void lostGoods(int goodsId,Role role){//int number,
        int nowNum = role.getMyPackageBo().getGoodsHashMap().get(goodsId);
        role.getMyPackageBo().getGoodsHashMap().put(goodsId,nowNum-1);
    }


    static {
        BestEquipSB.registerObserver(new BestEquipOB());
        SumMoneySB.registerObserver(new SumMoneyOB());
    }

}
