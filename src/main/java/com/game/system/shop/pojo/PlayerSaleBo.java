package com.game.system.shop.pojo;

import com.game.system.role.pojo.Role;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/6/27 11:30
 */
public class PlayerSaleBo {
    //拍卖场次id
    private String id;
    //物品id
    private int goodsId;
    //物品价格
    private int price;
    //供货者角色id
    private int roleId;
    //拍卖时间点
    private Instant tagTime;
    //最终定价
    private int lastPrice;
    //出价者角色id
    private int buyRoleId;
    //参与此次竞价的所有角色
    private ArrayList<Role> roleArrayList = new ArrayList<>();
    //竞价记录，roleId-price
    public HashMap<Integer,Integer> priceHashMap = new HashMap<>();

    public PlayerSaleBo(String id, int goodsId, int price, int roleId) {
        this.id = id;
        this.goodsId = goodsId;
        this.price = price;
        this.roleId = roleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Instant getTagTime() {
        return tagTime;
    }

    public void setTagTime(Instant tagTime) {
        this.tagTime = tagTime;
    }

    public int getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(int lastPrice) {
        this.lastPrice = lastPrice;
    }

    public int getBuyRoleId() {
        return buyRoleId;
    }

    public void setBuyRoleId(int buyRoleId) {
        this.buyRoleId = buyRoleId;
    }

    public ArrayList<Role> getRoleArrayList() {
        return roleArrayList;
    }

    public void setRoleArrayList(ArrayList<Role> roleArrayList) {
        this.roleArrayList = roleArrayList;
    }

    public HashMap<Integer, Integer> getPriceHashMap() {
        return priceHashMap;
    }

    public void setPriceHashMap(HashMap<Integer, Integer> priceHashMap) {
        this.priceHashMap = priceHashMap;
    }
}
