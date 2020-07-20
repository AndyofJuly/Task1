package com.game.system.bag.pojo;

/**
 * 背包中的每个格子对象
 * @Author andy
 * @create 2020/7/6 17:48
 */
public class BagGridBo {//改
    /** 格子id */
    private Integer id;
    /** 格子物品id */
    private Integer goodsId;
    /** 该物品数量 */
    private Integer number;

    public BagGridBo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
