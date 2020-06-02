package com.game.entity;

import com.game.entity.excel.EquipmentStatic;

/**
 * @Author andy
 * @create 2020/5/29 14:26
 */
public class Equipment {
    //背包中每格最多放一件装备
    public static final int packageMaxNumber = 1;
    private EquipmentStatic equipmentStatic;

    public Equipment(EquipmentStatic equipmentStatic) {
        this.equipmentStatic = equipmentStatic;
    }

    public EquipmentStatic getEquipmentStatic() {
        return equipmentStatic;
    }

    public void setEquipmentStatic(EquipmentStatic equipmentStatic) {
        this.equipmentStatic = equipmentStatic;
    }
}
