package com.game.system.bag.pojo;

/**
 * 药品类-暂时无用
 * @Author andy
 * @create 2020/5/29 9:56
 */
public class Potion {//改
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
