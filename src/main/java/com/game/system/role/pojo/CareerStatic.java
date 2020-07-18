package com.game.system.role.pojo;

/**
 * 角色职业类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/6/8 10:22
 */
public class CareerStatic {

    /** 角色职业id */
    private int id;
    /** 职业名 */
    private String name;
    /** 职业初始攻击力 */
    private int atk;
    /** 职业初始防御力 */
    private int def;
    /** 职业初始hp */
    private int hp;
    /** 职业初始mp */
    private int mp;
    /** 职业技能 */
    private int[] skillId;

    public CareerStatic(int id, String name, int atk, int def, int hp, int mp, int[] skillId) {
        this.id = id;
        this.name = name;
        this.atk = atk;
        this.def = def;
        this.hp = hp;
        this.mp = mp;
        this.skillId = skillId;
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

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int[] getSkillId() {
        return skillId;
    }

    public void setSkillId(int[] skillId) {
        this.skillId = skillId;
    }
}
