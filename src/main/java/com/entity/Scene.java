package com.entity;

import java.util.ArrayList;

/**
 * 场景类
 * @Author andy
 * @create 2020/5/13 11:10
 */
public class Scene {
    private String name;
    private int x;
    private int y;

    private ArrayList<Npc> npcAll = new ArrayList<Npc>();
    private ArrayList<Monster> monsterAll = new ArrayList<Monster>();
    private ArrayList<Role> roleAll = new ArrayList<Role>();

    public Scene(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ArrayList<Npc> getNpcAll() {
        return npcAll;
    }

    public void setNpcAll(ArrayList<Npc> npcAll) {
        this.npcAll = npcAll;
    }

    public ArrayList<Monster> getMonsterAll() {
        return monsterAll;
    }

    public void setMonsterAll(ArrayList<Monster> monsterAll) {
        this.monsterAll = monsterAll;
    }

    public ArrayList<Role> getRoleAll() {
        return roleAll;
    }

    public void setRoleAll(ArrayList<Role> roleAll) {
        this.roleAll = roleAll;
    }
}
