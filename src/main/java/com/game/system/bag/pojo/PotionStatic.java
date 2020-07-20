package com.game.system.bag.pojo;

/**
 * @Author andy
 * @create 2020/5/21 17:54
 */
public class PotionStatic {
    /** 药品id*/
    private Integer id;
    /** 药品名*/
    private String name;
    /** 药品增加的蓝量*/
    private Integer addMp;
    /** 药品增加的血量*/
    private Integer addHp;
    /** 药品的价格*/
    private Integer price;

    public PotionStatic(Integer id, String name, Integer addMp, Integer addHp, Integer price) {
        this.id = id;
        this.name = name;
        this.addMp = addMp;
        this.addHp = addHp;
        this.price = price;
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

    public Integer getAddMp() {
        return addMp;
    }

    public void setAddMp(Integer addMp) {
        this.addMp = addMp;
    }

    public Integer getAddHp() {
        return addHp;
    }

    public void setAddHp(Integer addHp) {
        this.addHp = addHp;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
