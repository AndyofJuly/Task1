package com.game.system.role.entity;

/**
 * 角色职业类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/6/8 10:22
 */
public class CareerStatic {

    /** 角色职业id */
    private Integer id;
    /** 职业名 */
    private String name;
    /** 职业初始攻击力 */
    private Integer atk;
    /** 职业初始防御力 */
    private Integer def;
    /** 职业初始hp */
    private Integer hp;
    /** 职业初始mp */
    private Integer mp;
    /** 职业技能 */
    private Integer[] skillId;

    public CareerStatic(Integer id, String name, Integer atk, Integer def, Integer hp, Integer mp, Integer[] skillId) {
        this.id = id;
        this.name = name;
        this.atk = atk;
        this.def = def;
        this.hp = hp;
        this.mp = mp;
        this.skillId = skillId;
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

    public Integer getDef() {
        return def;
    }

    public void setDef(Integer def) {
        this.def = def;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getMp() {
        return mp;
    }

    public void setMp(Integer mp) {
        this.mp = mp;
    }

    public Integer[] getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer[] skillId) {
        this.skillId = skillId;
    }
}
