package com.game.system.bag.entity;

/**
 * 药品类-暂时不用，使用PotionStatic提供信息即可
 * @Author andy
 * @create 2020/5/29 9:56
 */
public class Potion {
    /** 资源类中的静态id和动态id相同 */
    private Integer potionId;

    public Potion(Integer potionId) {
        this.potionId = potionId;
    }

    public Integer getPotionId() {
        return potionId;
    }

    public void setPotionId(Integer potionId) {
        this.potionId = potionId;
    }
}
