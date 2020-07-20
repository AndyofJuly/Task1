package com.game.system.bag.pojo;

/**
 * 装备类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/5/21 17:53
 */
public class EquipmentStatic {

    /** 装备id*/
    private Integer id;
    /** 装备名*/
    private String name;
    /** 装备攻击力*/
    private Integer atk;
    /** 装备耐久*/
    private Integer durability;
    /** 装备价格*/
    private Integer price;
    /** 装备等级*/
    private Integer level;
    /** 装备血量增益*/
    private Integer addHp;
    /** 装备蓝量增益*/
    private Integer addMp;
    /** 装备类别；1为武器；2为手环；3为项链；4为身体；5为帽子；6为鞋子*/
    private Integer type;
    /** 装备稀有度：0为普通武器；1为极品武器*/
    private Integer quality;

    public EquipmentStatic(Integer id, String name, Integer atk, Integer durability, Integer price, Integer level, Integer addHp, Integer addMp, Integer type,Integer quality) {
        this.id = id;
        this.name = name;
        this.atk = atk;
        this.durability = durability;
        this.price = price;
        this.level = level;
        this.addHp = addHp;
        this.addMp = addMp;
        this.type = type;
        this.quality = quality;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAtk() {
        return atk;
    }

    public void setAtk(Integer atk) {
        this.atk = atk;
    }

    public Integer getDurability() {
        return durability;
    }

    public void setDurability(Integer durability) {
        this.durability = durability;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getAddHp() {
        return addHp;
    }

    public void setAddHp(Integer addHp) {
        this.addHp = addHp;
    }

    public Integer getAddMp() {
        return addMp;
    }

    public void setAddMp(Integer addMp) {
        this.addMp = addMp;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }
}
