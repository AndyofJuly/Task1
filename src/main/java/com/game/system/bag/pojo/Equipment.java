package com.game.system.bag.pojo;

/**
 * @Author andy
 * @create 2020/5/29 14:26
 */
public class Equipment {
    //装备id-静态属性-动态属性（需要修改-要有唯一id）
    private int equipmentId;
    //装备当前耐久
    private int dura;
    //装备等级
    private int level;

    public Equipment(int equipmentId, int dura) {
        this.equipmentId = equipmentId;
        this.dura = dura;
        this.level = EquipmentResource.getEquipmentStaticHashMap().get(equipmentId).getLevel();
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
