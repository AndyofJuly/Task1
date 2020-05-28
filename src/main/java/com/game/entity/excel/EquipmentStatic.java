package com.game.entity.excel;

/**
 * 装备类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/5/21 17:53
 */
public class EquipmentStatic {
    private int id;
    private String name;
    private int atk;
    private int durability;

    public EquipmentStatic(int id, String name, int atk, int durability) {
        this.id = id;
        this.name = name;
        this.atk = atk;
        this.durability = durability;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
}
