package com.game.entity;

import com.game.entity.excel.MonsterStatic;

/**
 * 怪物类
 * @Author andy
 * @create 2020/5/12 22:32
 */
public class Monster{
    // 怪物存活状态，1表示存活，0表示被消灭
    private int alive;
    //private MonsterStatic monsterStatic;
    // 怪物id-静态属性
    private int monsterId;

    public Monster(int monsterId) {
        this.monsterId = monsterId;
    }

    public int getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(int monsterId) {
        this.monsterId = monsterId;
    }
    /*    public Monster(MonsterStatic monsterStatic) {
        this.monsterStatic = monsterStatic;
    }

    public MonsterStatic getMonsterStatic() {
        return monsterStatic;
    }

    public void setMonsterStatic(MonsterStatic monsterStatic) {
        this.monsterStatic = monsterStatic;
    }*/

    public int getAlive() {
        return alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }
}
