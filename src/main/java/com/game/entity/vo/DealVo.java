package com.game.entity.vo;

/**
 * 面对面交易
 * @Author andy
 * @create 2020/6/26 20:50
 */
public class DealVo {
    private String dealId;
    private int receiveRoleId;
    private int goodsId;
    private int price;
    private int sendRoleId;

    public DealVo(String dealId, int receiveRoleId, int goodsId, int price, int sendRoleId) {
        this.dealId = dealId;
        this.receiveRoleId = receiveRoleId;
        this.goodsId = goodsId;
        this.price = price;
        this.sendRoleId = sendRoleId;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public int getReceiveRoleId() {
        return receiveRoleId;
    }

    public void setReceiveRoleId(int receiveRoleId) {
        this.receiveRoleId = receiveRoleId;
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

    public int getSendRoleId() {
        return sendRoleId;
    }

    public void setSendRoleId(int sendRoleId) {
        this.sendRoleId = sendRoleId;
    }
}
