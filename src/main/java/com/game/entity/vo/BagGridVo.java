package com.game.entity.vo;

/**
 * 背包每个格子对象
 * @Author andy
 * @create 2020/7/6 17:48
 */
public class BagGridVo {
    //格子id
    private int id;
    //格子物品id
    private int goodsId;
    //该物品数量
    private int number;

    public BagGridVo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
