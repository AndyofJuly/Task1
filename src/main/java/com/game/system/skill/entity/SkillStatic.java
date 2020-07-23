package com.game.system.skill.entity;

/**
 * 技能类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/5/21 17:53
 */
public class SkillStatic {
    /** 技能的id */
    private Integer id;
    /** 技能的名字 */
    private String name;
    /** 技能的cd */
    private Integer cd;
    /** 技能的攻击力 */
    private Integer atk;
    /** 技能消耗的蓝 */
    private Integer useMp;
    /** 技能的类型 */
    private Integer typeId;
    /** 技能的加血量 */
    private Integer addHp;
    /** 技能的持续时间 */
    private Integer duration;
    /** 施法（吟唱）时间 */
    private Integer castTime;

    public SkillStatic(Integer id, String name, Integer cd, Integer atk, Integer useMp, Integer typeId, Integer addHp, Integer duration, Integer castTime) {
        this.id = id;
        this.name = name;
        this.cd = cd;
        this.atk = atk;
        this.useMp = useMp;
        this.typeId = typeId;
        this.addHp = addHp;
        this.duration = duration;
        this.castTime = castTime;
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

    public Integer getCd() {
        return cd;
    }

    public void setCd(Integer cd) {
        this.cd = cd;
    }

    public Integer getAtk() {
        return atk;
    }

    public void setAtk(Integer atk) {
        this.atk = atk;
    }

    public Integer getUseMp() {
        return useMp;
    }

    public void setUseMp(Integer useMp) {
        this.useMp = useMp;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getAddHp() {
        return addHp;
    }

    public void setAddHp(Integer addHp) {
        this.addHp = addHp;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getCastTime() {
        return castTime;
    }

    public void setCastTime(Integer castTime) {
        this.castTime = castTime;
    }
}
