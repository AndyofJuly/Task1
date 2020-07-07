package com.game.entity.vo;

import java.util.ArrayList;

/**
 * 面对面交易
 * @Author andy
 * @create 2020/6/26 20:50
 */
public class DealVo {
    private String dealId;
    private int receiveRoleId;
/*    private ArrayList<Integer> equipmentsList = new ArrayList<>();
    private ArrayList<Integer> potionList = new ArrayList<>();*/
    //private int goodsId;
    private int equipmentId;
    private int potionId;
    private int price;
    private int sendRoleId;
    private boolean isAgree;

    public DealVo(String dealId, int receiveRoleId, int equipmentId, int potionId, int price, int sendRoleId) {
        this.dealId = dealId;
        this.receiveRoleId = receiveRoleId;
        //this.goodsId = goodsId;
        this.equipmentId = equipmentId;
        this.potionId = potionId;
        this.price = price;
        this.sendRoleId = sendRoleId;
        this.isAgree = false;
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

/*    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }*/

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

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getPotionId() {
        return potionId;
    }

    public void setPotionId(int potionId) {
        this.potionId = potionId;
    }

    public boolean isAgree() {
        return isAgree;
    }

    public void setAgree(boolean agree) {
        isAgree = agree;
    }
}
