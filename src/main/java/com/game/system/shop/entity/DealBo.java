package com.game.system.shop.entity;

/**
 * 面对面交易-业务对象
 * @Author andy
 * @create 2020/6/26 20:50
 */
public class DealBo {
    /** 交易订单号 */
    private String dealId;
    /** 对方id */
    private Integer receiveRoleId;
    /** 交易装备id */
    private Integer equipmentId;
    /** 交易药品id */
    private Integer potionId;
    /** 药品数量 */
    private Integer num;
    /** 交易价格 */
    private Integer price;
    /** 发送者id */
    private Integer sendRoleId;
    /** 是否同意此次交易 */
    private boolean isAgree;

    public DealBo(String dealId, Integer receiveRoleId, Integer equipmentId, Integer potionId, Integer num, Integer price, Integer sendRoleId) {
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

    public Integer getReceiveRoleId() {
        return receiveRoleId;
    }

    public void setReceiveRoleId(Integer receiveRoleId) {
        this.receiveRoleId = receiveRoleId;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Integer getPotionId() {
        return potionId;
    }

    public void setPotionId(Integer potionId) {
        this.potionId = potionId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSendRoleId() {
        return sendRoleId;
    }

    public void setSendRoleId(Integer sendRoleId) {
        this.sendRoleId = sendRoleId;
    }

    public boolean isAgree() {
        return isAgree;
    }

    public void setAgree(boolean agree) {
        isAgree = agree;
    }
}
