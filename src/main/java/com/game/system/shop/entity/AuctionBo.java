package com.game.system.shop.entity;

import com.game.system.role.entity.Role;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 拍卖对象
 * @Author andy
 * @create 2020/6/27 11:30
 */
public class AuctionBo {
    /** 拍卖场次id */
    private String id;
    /** 物品id */
    private Integer goodsId;
    /** 物品价格 */
    private Integer price;
    /** 供货者角色id */
    private Integer roleId;
    /** 拍卖时间点 */
    private Instant tagTime;
    /** 最终定价 */
    private Integer lastPrice;
    /** 出价者角色id */
    private Integer buyRoleId;
    /** 参与此次竞价的所有角色 */
    private ArrayList<Role> roleArrayList = new ArrayList<>();
    /** 竞价记录，roleId-price */
    private HashMap<Integer,Integer> priceHashMap = new HashMap<>();
    /** 拍卖结束标志 */
    private boolean ifEnding;

    public AuctionBo(String id, Integer goodsId, Integer price, Integer roleId) {
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

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Instant getTagTime() {
        return tagTime;
    }

    public void setTagTime(Instant tagTime) {
        this.tagTime = tagTime;
    }

    public Integer getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Integer lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Integer getBuyRoleId() {
        return buyRoleId;
    }

    public void setBuyRoleId(Integer buyRoleId) {
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

    public boolean isIfEnding() {
        return ifEnding;
    }

    public void setIfEnding(boolean ifEnding) {
        this.ifEnding = ifEnding;
    }
}
