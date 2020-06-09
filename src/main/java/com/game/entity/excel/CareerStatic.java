package com.game.entity.excel;

/**
 * 角色职业类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/6/8 10:22
 */
public class CareerStatic {

    // 职业id
    private int id;
    // 职业名
    private String name;
    // 职业初始攻击力
    private String atk;
    // 职业初始防御力
    private String def;
    // 职业初始hp
    private int hp;
    // 职业初始mp
    private int mp;
    // 职业技能
    private String[] skillId;

    public CareerStatic(int id, String name, String atk, String def, int hp, int mp, String[] skillId) {
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

    public String getAtk() {
        return atk;
    }

    public void setAtk(String atk) {
        this.atk = atk;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
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

    public String[] getSkillId() {
        return skillId;
    }

    public void setSkillId(String[] skillId) {
        this.skillId = skillId;
    }
}
