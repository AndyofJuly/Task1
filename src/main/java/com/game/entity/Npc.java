package com.game.entity;

import com.game.entity.excel.NpcStatic;

/**
 * NPC类
 * @Author andy
 * @create 2020/5/11 14:58
 */
public class Npc {
    private int alive;
    private NpcStatic npcStatic;

    public Npc(NpcStatic npcStatic) {
        this.npcStatic = npcStatic;
    }

    public NpcStatic getNpcStatic() {
        return npcStatic;
    }

    public void setNpcStatic(NpcStatic npcStatic) {
        this.npcStatic = npcStatic;
    }

    public int getAlive() {
        return alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }
}
