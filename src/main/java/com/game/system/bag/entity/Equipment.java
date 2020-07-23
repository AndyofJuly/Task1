package com.game.system.bag.entity;

/**
 * 装备类
 * @Author andy
 * @create 2020/5/29 14:26
 */
public class Equipment {
    /** 装备唯一id-动态属性 */
    private Integer id;
    /** 装备id-静态属性*/
    private Integer equipmentId;
    /** 装备当前耐久*/
    private Integer dura;
    /** 装备等级*/
    private Integer level;

    public Equipment(Integer id,Integer equipmentId,int dura) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.dura = dura;
        this.level = EquipmentResource.getEquipmentStaticHashMap().get(equipmentId).getLevel();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Integer getDura() {
        return dura;
    }

    public void setDura(Integer dura) {
        this.dura = dura;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
