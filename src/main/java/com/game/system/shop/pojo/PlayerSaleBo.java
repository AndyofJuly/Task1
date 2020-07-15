package com.game.system.shop.pojo;

/**
 * 玩家一口价出售的物品
 * @Author andy
 * @create 2020/7/15 10:37
 */
public class PlayerSaleBo {

    //物品id
    private int goodsId;
    //价格
    private int price;
    //数量
    private int num;

    public PlayerSaleBo(int goodsId, int price, int num) {
        this.goodsId = goodsId;
        this.price = price;
        this.num = num;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
