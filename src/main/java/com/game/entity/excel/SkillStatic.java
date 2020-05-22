package com.game.entity.excel;

/**
 * @Author andy
 * @create 2020/5/21 17:53
 */
public class SkillStatic {
    private int id;
    private String name;
    private int cd;
    private int atk;
    private int useMp;

    public SkillStatic(int id, String name, int cd, int atk, int useMp) {
        this.id = id;
        this.name = name;
        this.cd = cd;
        this.atk = atk;
        this.useMp = useMp;
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
}
