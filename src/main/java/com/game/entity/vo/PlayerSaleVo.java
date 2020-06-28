package com.game.entity.vo;

import com.game.entity.Role;

import java.time.Instant;

/**
 * @Author andy
 * @create 2020/6/27 11:30
 */
public class PlayerSaleVo {
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

    public PlayerSaleVo(int goodsId, int price, int roleId) {
        this.goodsId = goodsId;
        this.price = price;
        this.roleId = roleId;
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
}
