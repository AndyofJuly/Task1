package com.game.system.bag.pojo;

/**
 * @Author andy
 * @create 2020/5/21 17:54
 */
public class PotionStatic {
    /** 药品id*/
    private int id;
    /** 药品名*/
    private String name;
    /** 药品增加的蓝量*/
    private int addMp;
    /** 药品增加的血量*/
    private int addHp;
    /** 药品的价格*/
    private int price;

    public PotionStatic(int id, String name, int addMp, int addHp, int price) {
        this.id = id;
        this.name = name;
        this.addMp = addMp;
        this.addHp = addHp;
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

    public int getAddMp() {
        return addMp;
    }

    public void setAddMp(int addMp) {
        this.addMp = addMp;
    }

    public int getAddHp() {
        return addHp;
    }

    public void setAddHp(int addHp) {
        this.addHp = addHp;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
