package com.game.entity;

import java.util.ArrayList;

/**
 * 场景类
 * @Author andy
 * @create 2020/5/13 11:10
 */
public class Scene {
    //private int id;
    private String name;
    private int[] sceneRelation;

    private Npc[] npcAll = new Npc[]{};
    private Monster[] monsterAll = new Monster[]{};
    private ArrayList<Role> roleAll = new ArrayList<Role>();

/*    public Scene(int id, String name, int[] sceneRelation) {
        this.id = id;
        this.name = name;
        this.sceneRelation = sceneRelation;
    }*/
    public Scene(String name, int[] sceneRelation) {
        this.name = name;
        this.sceneRelation = sceneRelation;
    }

    public int[] getSceneRelation() {
        return sceneRelation;
    }

    public void setSceneRelation(int[] sceneRelation) {
        this.sceneRelation = sceneRelation;
    }

/*    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Npc[] getNpcAll() {
        return npcAll;
    }

    public void setNpcAll(Npc[] npcAll) {
        this.npcAll = npcAll;
    }

    public Monster[] getMonsterAll() {
        return monsterAll;
    }

    public void setMonsterAll(Monster[] monsterAll) {
        this.monsterAll = monsterAll;
    }

    public ArrayList<Role> getRoleAll() {
        return roleAll;
    }

    public void setRoleAll(ArrayList<Role> roleAll) {
        this.roleAll = roleAll;
    }
}
