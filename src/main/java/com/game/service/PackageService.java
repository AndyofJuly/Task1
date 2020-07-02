package com.game.service;

import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.entity.Equipment;
import com.game.entity.Role;
import com.game.entity.store.EquipmentResource;
import com.game.service.assis.AssistService;
import com.game.service.assis.GlobalResource;

/**
 * 背包的服务
 * @Author andy
 * @create 2020/6/17 12:11
 */
public class PackageService {

    public void putIntoPackage(int goodsId,int number,Role role){
        if((goodsId+"").startsWith(Const.POTION_HEAD)){ //药品情况
            //如果背包中有该key，则数量叠加，如果getkey==null则新增该key，并设置数量
            if(role.getMyPackage().getGoodsHashMap().get(goodsId)!=null){
                int allAmount = role.getMyPackage().getGoodsHashMap().get(goodsId)+number;
                if(allAmount>= Const.POTION_MAX_NUM){  //药品类叠加超过上限，提示最多存99，并设置数量为99
                    role.getMyPackage().getGoodsHashMap().put(goodsId,Const.POTION_MAX_NUM);
                    System.out.println("你的背包最多只能存放该药物"+Const.POTION_MAX_NUM+"瓶");
                }else{
                    role.getMyPackage().getGoodsHashMap().put(goodsId,allAmount);
                    System.out.println("获得药品！");
                }
            }else {  //没有该物品，直接放入背包
                role.getMyPackage().getGoodsHashMap().put(goodsId,number);
            }
        }else{ //装备情况
            //装备类，如果有相同的装备，不操作，如果装备不同，数量+1
            if(role.getMyPackage().getGoodsHashMap().get(goodsId)==null){
                role.getMyPackage().getGoodsHashMap().put(goodsId,Const.EQUIPMENT_MAX_NUM);
                System.out.println("获得装备！");
                //待修改为动态id下的level
                int level = EquipmentResource.getEquipmentStaticHashMap().get(goodsId).getLevel();
                if(level>=10){
                    role.getMyPackage().setSumBestNum(1);
                }
                AchievementService.ifGetNBestEquipment(role);
                //AchievementService.ifSumEquipmentLevelToFourty(level,roleId);
            }
        }
    }

    public void getFromPackage(int goodsId,int number,Role role){
        if((goodsId+"").startsWith(Const.POTION_HEAD)){ //药品情况
            int leftAmount = role.getMyPackage().getGoodsHashMap().get(goodsId)-number;
            if(role.getMyPackage().getGoodsHashMap().get(goodsId)<0){
                System.out.println("错误，没有该物品了");
            }else{
                role.getMyPackage().getGoodsHashMap().put(goodsId,leftAmount);
            }
        }else{ //装备一次只能拿一件
            if(role.getMyPackage().getGoodsHashMap().get(goodsId)<=0){
                System.out.println("错误，没有该物品了");
            }else{
                role.getMyPackage().getGoodsHashMap().put(goodsId,0);
            }
        }
    }

    public static void addMoney(int number,Role role){
        role.setMoney(role.getMoney()+number);
        AchievementService.ifSumMoneyToThousand(number,role);
    }


}
