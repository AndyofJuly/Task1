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
    // 装备等级
    private int level;
    // 装备血量增益
    private int addHp;
    // 装备蓝量增益
    private int addMp;
    // 装备类别；1为武器；2为手环；3为项链
    private int type;

    public EquipmentStatic(int id, String name, int atk, int durability, int price, int level, int addHp, int addMp, int type) {
        this.id = id;
        this.name = name;
        this.atk = atk;
        this.durability = durability;
        this.price = price;
        this.level = level;
        this.addHp = addHp;
        this.addMp = addMp;
        this.type = type;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAddHp() {
        return addHp;
    }

    public void setAddHp(int addHp) {
        this.addHp = addHp;
    }

    public int getAddMp() {
        return addMp;
    }

    public void setAddMp(int addMp) {
        this.addMp = addMp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
