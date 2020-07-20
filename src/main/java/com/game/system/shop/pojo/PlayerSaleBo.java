package com.game.system.shop.pojo;

/**
 * 玩家一口价出售的物品
 * @Author andy
 * @create 2020/7/15 10:37
 */
public class PlayerSaleBo {

    /** 物品id */
    private Integer goodsId;
    /** 价格 */
    private Integer price;
    /** 数量 */
    private Integer num;

    public PlayerSaleBo(Integer goodsId, Integer price, Integer num) {
        this.goodsId = goodsId;
        this.price = price;
        this.num = num;
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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
