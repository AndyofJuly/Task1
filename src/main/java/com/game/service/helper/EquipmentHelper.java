package com.game.service.helper;

import com.game.entity.Equipment;
import com.game.entity.excel.EquipmentStatic;
import com.game.entity.store.EquipmentResource;

/**
 * @Author andy
 * @create 2020/6/29 16:12
 */
public class EquipmentHelper {
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

/*    //获得静态装备对象
    public static EquipmentStatic getEquipmentStatic(int key){
        return EquipmentResource.getEquipmentStaticHashMap().get(key);
    }*/
}
