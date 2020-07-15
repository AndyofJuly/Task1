package com.game.system.shop.pojo;

/**
 * 面对面交易
 * @Author andy
 * @create 2020/6/26 20:50
 */
public class DealBo {
    //交易订单号
    private String dealId;
    //对方id
    private int receiveRoleId;
    //交易装备id
    private int equipmentId;
    //交易药品id
    private int potionId;
    //药品数量
    private int num;
    //交易价格
    private int price;
    //发送者id
    private int sendRoleId;
    //是否同意此次交易
    private boolean isAgree;

    public DealBo(String dealId, int receiveRoleId, int equipmentId, int potionId, int num, int price, int sendRoleId) {
        this.dealId = dealId;
        this.receiveRoleId = receiveRoleId;
        this.equipmentId = equipmentId;
        this.potionId = potionId;
        this.num = num;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
