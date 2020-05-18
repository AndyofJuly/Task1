package com.game.entity;

/**
 * NPC类
 * @Author andy
 * @create 2020/5/11 14:58
 */
public class Npc {
    //private int id;
    private String name;
    private int alive;

/*    public Npc(int id,String name) {
        this.name = name;
        this.id = id;
        this.alive = 1;
    }*/

    public Npc(String name) {
        this.name = name;
        //this.id = id;
        this.alive = 1;
    }
/*    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAlive() {
        return alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }
}
