package com.game.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 队伍类
 * @Author andy
 * @create 2020/6/7 11:39
 */
public class Team {

    //队伍id
    String id;
    //队伍中的角色集合，存放角色id
    ArrayList<Integer> roleList = new ArrayList<Integer>();
    //选择的副本id
    int dungeonsId;

/*    //队伍中的角色集合，存放角色id，第一个参数为teamId，第二个参数为角色id
    HashMap<String,Integer> teamHashMap = new HashMap<String,Integer>();*/

    public Team(String id, int dungeonsId) {
        this.id = id;
        this.dungeonsId = dungeonsId;
    }

    public ArrayList<Integer> getRoleList() {
        return roleList;
    }

    public void setRoleList(ArrayList<Integer> roleList) {
        this.roleList = roleList;
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

    public void setDungeonsId(int dungeonsId) {
        this.dungeonsId = dungeonsId;
    }
}
