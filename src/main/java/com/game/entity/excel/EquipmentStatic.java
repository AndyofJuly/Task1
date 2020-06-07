package com.game.entity.excel;

/**
 * 装备类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/5/21 17:53
 */
public class EquipmentStatic {

    // 装备id
    private int id;
    // 装备名
    private String name;
    // 装备攻击力
    private int atk;
    // 装备耐久
    private int durability;
    // 装备价格
    private int price;

    public EquipmentStatic(int id, String name, int atk, int durability ,int price) {
        this.id = id;
        this.name = name;
        this.atk = atk;
        this.durability = durability;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
