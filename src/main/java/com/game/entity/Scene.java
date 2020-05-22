package com.game.entity;

import com.game.entity.excel.SceneStatic;

import java.util.ArrayList;

/**
 * 场景类
 * @Author andy
 * @create 2020/5/13 11:10
 */
public class Scene {
/*    private int id;
    private String name;
    private String[] sceneRelation;

    private String[] npcId;
    private String[] monsterId;*/
    private int id;
    private SceneStatic sceneStatic;
    private ArrayList<Role> roleAll = new ArrayList<Role>();

    public Scene(SceneStatic sceneStatic) {
        this.sceneStatic = sceneStatic;
        this.id = sceneStatic.getId();
    }

    public SceneStatic getSceneStatic() {
        return sceneStatic;
    }

    public void setSceneStatic(SceneStatic sceneStatic) {
        this.sceneStatic = sceneStatic;
    }
/*    public Scene(int id, String name, String[] sceneRelation, String[] npcId, String[] monsterId) {
        this.id = id;
        this.name = name;
        this.sceneRelation = sceneRelation;
        this.npcId = npcId;
        this.monsterId = monsterId;
    }

    public Scene(String name) {
        this.name = name;
    }

    public Scene(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Scene(int id, String name, String[] sceneRelation) {
        this.id = id;
        this.name = name;
        this.sceneRelation = sceneRelation;
    }
    public Scene(String name, String[] sceneRelation) {
        this.name = name;
        this.sceneRelation = sceneRelation;
    }

    public String[] getSceneRelation() {
        return sceneRelation;
    }

    public void setSceneRelation(String[] sceneRelation) {
        this.sceneRelation = sceneRelation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getNpcId() {
        return npcId;
    }

    public void setNpcId(String[] npcId) {
        this.npcId = npcId;
    }

    public String[] getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(String[] monsterId) {
        this.monsterId = monsterId;
    }*/

    public ArrayList<Role> getRoleAll() {
        return roleAll;
    }

    public void setRoleAll(ArrayList<Role> roleAll) {
        this.roleAll = roleAll;
    }
}
