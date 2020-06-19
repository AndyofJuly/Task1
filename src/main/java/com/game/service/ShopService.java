package com.game.service;

import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.dao.RoleMapper;
import com.game.entity.Role;
import com.game.entity.store.EquipmentResource;
import com.game.entity.store.PotionResource;
import com.game.service.assis.AssistService;
import com.game.service.assis.GlobalResource;

import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/6/15 15:00
 */
public class ShopService {

    static RoleMapper roleMapper = new RoleMapper();
    public static String buyGoods(String goods,int number,int roleId){
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        int key = AssistService.checkGoodsId(goods);
        //药品的情况
        if(String.valueOf(key).startsWith(Const.POTION_HEAD)){
            if(!ifLimitBuy(key,number,roleId)){
                return Const.Shop.LIMIT_MSG;
            }
            int cost = PotionResource.getPotionStaticHashMap().get(key).getPrice()*number;
            if(!ifCanbuy(role,cost)){
                return Const.Shop.BUY_FAILURE;
            }
            PackageService.putIntoPackage(key,number,roleId);
            return Const.Shop.BUY_SUCCESS+ role.getMyPackage().getGoodsHashMap().get(key);
        }else {  //装备的情况

            int cost = EquipmentResource.getEquipmentStaticHashMap().get(key).getPrice()*number;
            if(!ifCanbuy(role,cost)){
                return Const.Shop.BUY_FAILURE;
            }
            PackageService.putIntoPackage(key,number,roleId);
            return Const.Shop.BUY_SUCCESS+ role.getMyPackage().getGoodsHashMap().get(key);
        }
    }

    //限购代码
    public static boolean ifLimitBuy(int goodsId,int number,int roleId){
        HashMap<Integer,HashMap<Integer,Integer>> buyRecord = new HashMap<>();
        buyRecord = roleMapper.selectBuyRecord(roleId);
        if(buyRecord.get(roleId).get(goodsId)==null){
            buyRecord.get(roleId).put(goodsId,0);
        }
        int sumNumber = number + buyRecord.get(roleId).get(goodsId);
        System.out.println(sumNumber);
        if(sumNumber> Const.GOODS_BUG_MAX){
            return false;
        }else {
            roleMapper.insertBuyRecord(roleId,goodsId,sumNumber);
        }
        return true;
    }

    public static boolean ifCanbuy(Role role,int cost){
        if(role.getMoney()<cost){
            return false;
        }
        role.setMoney(role.getMoney()-cost);
        return true;
    }
}
