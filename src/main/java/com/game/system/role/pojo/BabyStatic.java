package com.game.system.role.pojo;

/**
 * 宝宝类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/6/11 16:42
 */
public class BabyStatic {
    // 初始的静态数据
    // 宝宝id
    private int id;
    // 宝宝名
    private String name;
    // 宝宝初始血量
    private int hp;
    // 怪物初始攻击力
    private int atk;

    public BabyStatic(int id, String name, int hp, int atk) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.atk = atk;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }
}
