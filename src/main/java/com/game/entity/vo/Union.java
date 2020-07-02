package com.game.entity.vo;

import com.game.entity.Potion;
import com.game.entity.Role;
import com.game.entity.excel.JobStatic;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 公会类
 * @Author andy
 * @create 2020/6/28 14:34
 */
public class Union {
    //公会id
    private int id;
    //公会名
    private String name;
    //公会成员及权限级别，角色id和对应的级别；1为会长；2为副会长；3为精英；4为普通会员
    //对应权限：解散公会：1；任职1-2；批准申请：1-2-3；开除1-2-3；
    //private HashMap<Integer, Integer> roleAuthority = new HashMap<>();
    //公会成员及职务id
    private HashMap<Role, Integer> roleJobHashMap = new HashMap<>();
    //申请列表
    private ArrayList<Integer> roleList = new ArrayList<>();
    //钱
    private int money;
    //道具-目前暂时用药品测试，后续将其改为道具vo，道具id和数量
    private HashMap<Integer, Integer> goodsHashMap = new HashMap<>();

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

    public HashMap<Role, Integer> getRoleJobHashMap() {
        return roleJobHashMap;
    }

    public void setRoleJobHashMap(HashMap<Role, Integer> roleJobHashMap) {
        this.roleJobHashMap = roleJobHashMap;
    }

    public ArrayList<Integer> getRoleList() {
        return roleList;
    }

    public void setRoleList(ArrayList<Integer> roleList) {
        this.roleList = roleList;
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

    public void setGoodsHashMap(HashMap<Integer, Integer> goodsHashMap) {
        this.goodsHashMap = goodsHashMap;
    }
}
