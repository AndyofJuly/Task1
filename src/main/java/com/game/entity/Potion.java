package com.game.entity;

import com.game.entity.excel.PotionStatic;

/**
 * @Author andy
 * @create 2020/5/29 9:56
 */
public class Potion {
    //背包中每格最多99瓶药
    public static final int packageMaxNumber = 99;
    private int number;
    private PotionStatic potionStatic;

    public Potion(int number, PotionStatic potionStatic) {
        this.number = number;
        this.potionStatic = potionStatic;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public PotionStatic getPotionStatic() {
        return potionStatic;
    }

    public void setPotionStatic(PotionStatic potionStatic) {
        this.potionStatic = potionStatic;
    }
}
