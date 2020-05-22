package com.game.entity;

import com.game.entity.excel.MonsterStatic;

/**
 * 怪物类
 * @Author andy
 * @create 2020/5/12 22:32
 */
public class Monster{
    private int alive;
    private MonsterStatic monsterStatic;

    public Monster(MonsterStatic monsterStatic) {
        this.monsterStatic = monsterStatic;
    }

    public MonsterStatic getMonsterStatic() {
        return monsterStatic;
    }

    public void setMonsterStatic(MonsterStatic monsterStatic) {
        this.monsterStatic = monsterStatic;
    }

    public int getAlive() {
        return alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }
}
