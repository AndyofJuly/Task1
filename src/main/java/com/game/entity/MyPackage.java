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
    private int id;
    private int size;
    private ArrayList<PotionStatic> potionStatics;
    private HashMap<Integer,EquipmentStatic> equipmentStaticHashMap;
}
