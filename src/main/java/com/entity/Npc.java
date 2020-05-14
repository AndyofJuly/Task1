package com.entity;

/**
 * NPC类
 * @Author andy
 * @create 2020/5/11 14:58
 */
public class Npc {
    private String name;
    private String nowScenes;
    private int alive;

    public Npc(String name, String nowScenes, int alive) {
        this.name = name;
        this.nowScenes = nowScenes;
        this.alive = alive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNowScenes() {
        return nowScenes;
    }

    public void setNowScenes(String nowScenes) {
        this.nowScenes = nowScenes;
    }

    public int getAlive() {
        return alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }
}
