package com.game.entity;

/**
 * NPC类
 * @Author andy
 * @create 2020/5/11 14:58
 */
public class Npc {
    private int id;
    private String name;
    private String alive;

    public Npc(int id,String name,String alive) {
        this.name = name;
        this.id = id;
        this.alive = alive;
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

    public String getAlive() {
        return alive;
    }

    public void setAlive(String alive) {
        this.alive = alive;
    }
}
