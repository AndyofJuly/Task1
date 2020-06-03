package com.game.entity.excel;

/**
 * 怪物类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/5/20 20:45
 */
public class MonsterStatic {
    // 怪物id
    private int id;
    // 怪物名
    private String name;
    // 怪物血量
    private int hp;
    // 怪物所在场景id
    private int sceneId;
    // 怪物攻击力
    private int atk;

    public MonsterStatic(int id, String name, int hp, int sceneId, int atk) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.sceneId = sceneId;
        this.atk = atk;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
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
}
