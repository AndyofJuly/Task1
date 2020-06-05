package com.game.entity;

import com.game.entity.excel.EquipmentStatic;

/**
 * @Author andy
 * @create 2020/5/29 14:26
 */
public class Equipment {
    //背包中每格最多放一件装备
    public static final int packageMaxNumber = 1;
    //装备id-静态属性
    private int equipmentId;
    //装备当前耐久
    private int dura;

    public Equipment(int equipmentId, int dura) {
        this.equipmentId = equipmentId;
        this.dura = dura;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getDura() {
        return dura;
    }

    public void setDura(int dura) {
        this.dura = dura;
    }
}
