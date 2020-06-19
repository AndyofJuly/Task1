package com.game.entity.excel;

/**
 * @Author andy
 * @create 2020/5/21 17:53
 */
public class SkillStatic {
    // 技能的id
    private int id;
    // 技能的名字
    private String name;
    // 技能的cd
    private int cd;
    // 技能的攻击力
    private int atk;
    // 技能消耗的蓝
    private int useMp;
    // 技能的类型
    private int typeId;
    // 技能的加血量
    private int addHp;
    // 技能的持续时间
    private int duration;
    // 施法（吟唱）时间
    private int castTime;

    public SkillStatic(int id, String name, int cd, int atk, int useMp, int typeId, int addHp, int duration, int castTime) {
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

    public int getCd() {
        return cd;
    }

    public void setCd(int cd) {
        this.cd = cd;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getUseMp() {
        return useMp;
    }

    public void setUseMp(int useMp) {
        this.useMp = useMp;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getAddHp() {
        return addHp;
    }

    public void setAddHp(int addHp) {
        this.addHp = addHp;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCastTime() {
        return castTime;
    }

    public void setCastTime(int castTime) {
        this.castTime = castTime;
    }
}
