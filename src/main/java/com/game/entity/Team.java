package com.game.entity;

import java.util.ArrayList;

/**
 * 队伍类
 * @Author andy
 * @create 2020/6/7 11:39
 */
public class Team {

    /** 队伍id */
    private String id;
    /** 队伍中的角色id集合 */
    private final ArrayList<Integer> roleList = new ArrayList<>();
    /** 选择的副本id */
    private int dungeonsId;

    public Team(String id, int dungeonsId) {
        this.id = id;
        this.dungeonsId = dungeonsId;
    }

    public ArrayList<Integer> getRoleList() {
        return roleList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDungeonsId() {
        return dungeonsId;
    }

}
