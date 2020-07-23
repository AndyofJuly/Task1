package com.game.system.role.entity;

/**
 * 宝宝类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/6/11 16:42
 */
public class BabyStatic {
    /** 宝宝id */
    private Integer id;
    /** 宝宝名 */
    private String name;
    /** 宝宝初始血量 */
    private Integer hp;
    /** 怪物初始攻击力 */
    private Integer atk;

    public BabyStatic(Integer id, String name, Integer hp, Integer atk) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.atk = atk;
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

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getAtk() {
        return atk;
    }

    public void setAtk(Integer atk) {
        this.atk = atk;
    }
}
