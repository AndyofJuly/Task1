package com.game.service.assist;

import com.game.entity.store.EquipmentResource;
import com.game.entity.store.PotionResource;

/**
 * 查找对应静态资源，简化
 * @Author andy
 * @create 2020/7/7 14:41
 */
public class ResourceSearch {

    public static String getPotionName(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getName();
    }
    //获得价格
    public static int getPotionPrice(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getPrice();
    }

    public static int getPotionAddHp(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getAddHp();
    }

    public static int getPotionAddMp(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getAddMp();
    }

    //获得装备名
    public static String getEquipmentName(int key){
        return EquipmentResource.getEquipmentStaticHashMap().get(key).getName();
    }
    //获得价格
    public static int getEquipmentPrice(int key){
        return EquipmentResource.getEquipmentStaticHashMap().get(key).getPrice();
    }
    //获得装备攻击力
    public static int getEquipmentAtk(int key){
        return EquipmentResource.getEquipmentStaticHashMap().get(key).getAtk();
    }
    //获得装备耐久
    public static int getEquipmentDura(int key){
        return EquipmentResource.getEquipmentStaticHashMap().get(key).getDurability();
    }

}
