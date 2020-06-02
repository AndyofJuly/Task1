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
    //背包容量
    private int size;
    //药类可叠加，只计数即可

    private HashMap<Integer,Potion> potionHashMap = new HashMap<Integer,Potion>();
    private HashMap<Integer,Equipment> packageEquipmentHashMap = new HashMap<Integer,Equipment>();

    public MyPackage(int size, HashMap<Integer, Potion> potionHashMap, HashMap<Integer, Equipment> packageEquipmentHashMap) {
        this.size = size;
        this.potionHashMap = potionHashMap;
        this.packageEquipmentHashMap = packageEquipmentHashMap;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public HashMap<Integer, Potion> getPotionHashMap() {
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
    }
}
