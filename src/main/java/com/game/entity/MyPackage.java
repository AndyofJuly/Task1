package com.game.entity;

import com.game.entity.excel.EquipmentStatic;
import com.game.entity.excel.PotionStatic;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 背包类
 * @Author andy
 * @create 2020/5/21 20:04
 */
public class MyPackage {
    // 背包id
    private int packageId;
    // 背包容量
    private int size;
    // 药类可叠加，只计数即可
    // 背包中的药品，使用List集合，元素可重复
//    private HashMap<Integer,Potion> potionHashMap = new HashMap<Integer,Potion>();
    // 背包中的装备，使用map集合，元素不可重复
//    private HashMap<Integer,Equipment> packageEquipmentHashMap = new HashMap<Integer,Equipment>();
    //物品id和物品数量
    private HashMap<Integer,Integer> goodsHashMap = new HashMap<Integer,Integer>();

/*    public MyPackage(int size, HashMap<Integer, Potion> potionHashMap, HashMap<Integer, Equipment> packageEquipmentHashMap) {
        this.size = size;
        this.potionHashMap = potionHashMap;
        this.packageEquipmentHashMap = packageEquipmentHashMap;
    }*/

    public MyPackage(int size, HashMap<Integer, Integer> goodsHashMap) {
        this.size = size;
        this.goodsHashMap = goodsHashMap;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public HashMap<Integer, Integer> getGoodsHashMap() {
        return goodsHashMap;
    }

    public void setGoodsHashMap(HashMap<Integer, Integer> goodsHashMap) {
        this.goodsHashMap = goodsHashMap;
    }
/*    public HashMap<Integer, Potion> getPotionHashMap() {
        return potionHashMap;
    }

    public void setPotionHashMap(HashMap<Integer, Potion> potionHashMap) {
        this.potionHashMap = potionHashMap;
    }

    public HashMap<Integer, Equipment> getPackageEquipmentHashMap() {
        return packageEquipmentHashMap;
    }

    public void setPackageEquipmentHashMap(HashMap<Integer, Equipment> packageEquipmentHashMap) {
        this.packageEquipmentHashMap = packageEquipmentHashMap;
    }*/
}
