package com.game.service;

import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.entity.Role;
import com.game.entity.store.EquipmentResource;
import com.game.entity.store.PotionResource;
import com.game.service.assis.AssistService;
import com.game.service.assis.DynamicResource;
import com.game.service.assis.InitGame;

import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/6/15 15:00
 */
public class ShopService {

    //获得一些物品 todo 优化条件语句；建议药品查询与装备查询放在一起
    //购买物品，计算金钱，提示金钱不够or购买成功；角色金钱>物品价格*数量
    // todo 可以拆分：是否限购方法、是否足够银两方法、数量叠加方法、装备药品分开写
    public static String buyGoods(String goods,String amount,int roleId){
        Role role = RoleController.roleHashMap.get(roleId);
        int number = Integer.parseInt(amount);
        int key = AssistService.checkGoodsId(goods);
        //药品的情况
        if(String.valueOf(key).startsWith(Const.POTION_HEAD)){

            //限购代码-demo
            HashMap<Integer,Integer> recordHashMap = new HashMap<>();
            //先检查该角色id下该物品之前的购买数量，如果加上这次购买，数量已经超过限购数量上限，则提示无法购买，否则可以购买
            if(DynamicResource.buyRecord.get(roleId)==null){
                recordHashMap.put(key,Const.ZERO);
                DynamicResource.buyRecord.put(roleId,recordHashMap);
            }
            int sumNumber = number + DynamicResource.buyRecord.get(roleId).get(key);
            if(sumNumber> Const.GOODS_BUG_MAX){
                return "超过限购数量，不可以再购买了";
            }else {
                recordHashMap.put(key,sumNumber);
                DynamicResource.buyRecord.put(roleId,recordHashMap);
            }

            //先计算是否买得起，买不起直接提示
            int cost = PotionResource.potionStaticHashMap.get(key).getPrice()*number;
            if(role.getMoney()<cost){
                return "您所携带的银两不够，无法够买";
            }else {
                //够买成功，角色金钱变少
                role.setMoney(role.getMoney()-cost);
            }
            //如果背包中有该key，则数量叠加，如果getkey==null则新增该key，并设置数量
            if(role.getMyPackage().getGoodsHashMap().get(key)!=null){
                int allAmount = role.getMyPackage().getGoodsHashMap().get(key)+number;
                if(allAmount>=Const.POTION_MAX_NUM){  //药品类叠加超过上限，提示最多存99，并设置数量为99
                    role.getMyPackage().getGoodsHashMap().put(key,Const.POTION_MAX_NUM);
                    System.out.println("你的背包最多只能存放该药物"+Const.POTION_MAX_NUM+"瓶");
                }else{
                    role.getMyPackage().getGoodsHashMap().put(key,allAmount);
                    System.out.println("获得药品！");
                }
            }else {  //没有该物品，直接放入背包
                role.getMyPackage().getGoodsHashMap().put(key,number);
            }
            return "购买成功，目前该药品在背包中的数量为"+ role.getMyPackage().getGoodsHashMap().get(key);
        }else {  //装备的情况
            //先计算是否买得起，买不起直接提示
            int cost = EquipmentResource.equipmentStaticHashMap.get(key).getPrice()*number;
            if(role.getMoney()<cost){
                return "您所携带的银两不够，无法够买";
            }else {
                //够买成功，角色金钱变少
                role.setMoney(role.getMoney()-cost);
            }
            //装备类，如果有相同的装备，不操作，如果装备不同，数量+1
            int keyEquipment = AssistService.checkEquipmentId(goods);
            if(role.getMyPackage().getGoodsHashMap().get(key)==null){
                role.getMyPackage().getGoodsHashMap().put(keyEquipment,Const.EQUIPMENT_MAX_NUM);
                System.out.println("获得装备！");
            }
            return "购买成功，目前该装备在背包中的数量为"+ role.getMyPackage().getGoodsHashMap().get(keyEquipment);
        }
    }
}
