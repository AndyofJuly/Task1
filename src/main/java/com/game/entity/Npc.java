package com.game.entity;

/**
 * NPC类
 * @Author andy
 * @create 2020/5/11 14:58
 */
public class Npc {
    /** NPC存活状态，1表示存活，0表示被消灭 */
    private int alive;
    /** 资源类中的静态id */
    private int npcId;

    public Npc(int npcId) {
        this.npcId = npcId;
    }

    public int getAlive() {
        return alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }

    public int getNpcId() {
        return npcId;
    }

    public void setNpcId(int npcId) {
        this.npcId = npcId;
    }
}
