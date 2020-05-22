package com.game.entity.excel;

/**
 * @Author andy
 * @create 2020/5/21 17:54
 */
public class PotionStatic {
    private int id;
    private String name;
    private int addMp;
    private int addHp;

    public PotionStatic(int id, String name, int addMp, int addHp) {
        this.id = id;
        this.name = name;
        this.addMp = addMp;
        this.addHp = addHp;
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
}
