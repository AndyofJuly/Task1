package com.game.entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 公会类
 * 公会成员及权限级别，角色id和对应的级别；1为会长；2为副会长；3为精英；4为普通会员
 * 对应权限：解散公会：1；任职1-2；批准申请：1-2-3；开除1-2-3；
 * @Author andy
 * @create 2020/6/28 14:34
 */
public class Union {
    /** 公会id */
    private int id;
    /** 公会名 */
    private String name;
    /** 公会成员id及职务id */
    private final HashMap<Integer, Integer> roleJobHashMap = new HashMap<>();
    /** 申请列表 */
    private final ArrayList<Integer> roleList = new ArrayList<>();
    /** 钱 */
    private int money;
    /** 道具id和数量 */
    private final HashMap<Integer, Integer> goodsHashMap = new HashMap<>();

    public Union(int id, String name) {
        this.id = id;
        this.name = name;
        this.money = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Integer, Integer> getRoleJobHashMap() {
        return roleJobHashMap;
    }

    public ArrayList<Integer> getRoleList() {
        return roleList;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public HashMap<Integer, Integer> getGoodsHashMap() {
        return goodsHashMap;
    }
}
