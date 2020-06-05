package com.game.entity;

import com.game.entity.excel.NpcStatic;

/**
 * NPC类
 * @Author andy
 * @create 2020/5/11 14:58
 */
public class Npc {
    // NPC存活状态，1表示存活，0表示被消灭
    private int alive;
    // NPC的id-静态属性
    private int NPcId;

    public Npc(int NPcId) {
        this.NPcId = NPcId;
    }

    public int getNPcId() {
        return NPcId;
    }

    public void setNPcId(int NPcId) {
        this.NPcId = NPcId;
    }

    public int getAlive() {
        return alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }
}
