package com.game.entity;

import com.game.entity.store.BabyResource;
import com.game.entity.store.MonsterResource;

/**
 * @Author andy
 * @create 2020/6/11 16:43
 */
public class Baby {

    //宝宝UUID，每个宝宝id唯一
    private String id;
    //宝宝攻击力
    private int babyAtk;
    //宝宝血量
    private int babyHp;
    //宝宝所在场景
    private int scenneId;
    //宝宝静态id
    private int babyId;
    //宝宝是否被召唤出来
    private boolean ifCall;
    //测试-所属角色
    private Role role;//似乎不需要

    public Baby(String id,int babyId,Role role) {
        this.id = id;
        this.babyId = babyId;
        this.role = role;
        //血量初始化
        this.babyHp= BabyResource.babyStatics.get(this.babyId).getHp();
        this.scenneId = role.getNowScenesId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBabyAtk() {
        return babyAtk;
    }

    public void setBabyAtk(int babyAtk) {
        this.babyAtk = babyAtk;
    }

    public int getBabyHp() {
        return babyHp;
    }

    public void setBabyHp(int babyHp) {
        this.babyHp = babyHp;
    }

    public int getScenneId() {
        return scenneId;
    }

    public void setScenneId(int scenneId) {
        this.scenneId = scenneId;
    }

    public int getBabyId() {
        return babyId;
    }

    public void setBabyId(int babyId) {
        this.babyId = babyId;
    }

    public boolean isIfCall() {
        return ifCall;
    }

    public void setIfCall(boolean ifCall) {
        this.ifCall = ifCall;
    }
}
