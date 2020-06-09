package com.game.entity.excel;

/**
 * @Author andy
 * @create 2020/6/8 10:40
 */
public class DungeonsStatic {

    private int id;

    private String name;

    private int deadTime;

    private int bossId;

    private int sceneId;

    public DungeonsStatic(int id, String name, int deadTime, int bossId, int sceneId) {
        this.id = id;
        this.name = name;
        this.deadTime = deadTime;
        this.bossId = bossId;
        this.sceneId = sceneId;
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

    public int getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(int deadTime) {
        this.deadTime = deadTime;
    }

    public int getBossId() {
        return bossId;
    }

    public void setBossId(int bossId) {
        this.bossId = bossId;
    }

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }
}
