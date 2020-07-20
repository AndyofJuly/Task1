package com.game.system.bag.pojo;

import java.util.Objects;

/**
 * 装备类
 * @Author andy
 * @create 2020/5/29 14:26
 */
public class Equipment {//改
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
        //this.dura = EquipmentResource.getEquipmentStaticHashMap().get(equipmentId).getDurability();
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

/*    @Override
    public boolean equals(Object obj) {
        // 这里使用==显示判断比较对象是否是同一对象
        if (this == obj) {
            return true;
        }
        // 对于任何非null的引用值x，x.equals(null)必须返回false
        if (obj == null) {
            return false;
        }
        // 通过 instanceof 判断比较对象类型是否合法
        if (!(obj instanceof Equipment)) {
            return false;
        }
        // 对象类型强制转换，如果核心域比较相等，则返回true，否则返回false
        Equipment other = (Equipment) obj;
        // 如果两者相等，返回true（含两者皆空的情形），否则比较两者值是否相等
        return Objects.equals(this.equipmentId, other.equipmentId);
    }*/
}
