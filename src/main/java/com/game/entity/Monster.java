package com.game.entity;

import com.game.entity.excel.MonsterStatic;
import com.game.entity.store.MonsterResource;

/**
 * 怪物类
 * @Author andy
 * @create 2020/5/12 22:32
 */
public class Monster{
    //具体对象的id，每个怪物的id不相同
    private String id;
    // 怪物存活状态，1表示存活，0表示被消灭
    private int alive=1;
    //private MonsterStatic monsterStatic;
    // 资源类中的静态id
    private int monsterId;
    // 怪物血量
    private int monsterHp;
    // 怪物位置-动态
    private int[] position;

    public Monster(String id, int monsterId) {
        this.id = id;
        this.monsterId = monsterId;
        this.monsterHp = MonsterResource.getMonstersStatics().get(this.monsterId).getHp();
        this.position = MonsterResource.getMonstersStatics().get(this.monsterId).getPosition();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(int monsterId) {
        this.monsterId = monsterId;
    }

    public int getAlive() {
        return alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }

    public int getMonsterHp() {
        return monsterHp;
    }

    public void setMonsterHp(int monsterHp) {
        this.monsterHp = monsterHp;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }
}
