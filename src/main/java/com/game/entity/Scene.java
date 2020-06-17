package com.game.entity;

import com.game.entity.excel.SceneStatic;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 场景类
 * @Author andy
 * @create 2020/5/13 11:10
 */
public class Scene {

    //具体场景id，原场景与静态场景id相同，临时场景与静态场景id则不同
    private int id;
    //具体场景Name，临时场景的name是随机的
    private String name;

    // 资源类中的静态id
    private int sceneId;
    //该场景下的角色集合
    private ArrayList<Role> roleAll = new ArrayList<Role>();
    //该场景下的怪物集合
    private HashMap<String,Monster> monsterHashMap = new HashMap<String,Monster>();

    public Scene(int id,String name,int sceneId) {
        this.id = id;
        this.name = name;
        this.sceneId = sceneId;
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

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public ArrayList<Role> getRoleAll() {
        return roleAll;
    }

    public void setRoleAll(ArrayList<Role> roleAll) {
        this.roleAll = roleAll;
    }

    public HashMap<String, Monster> getMonsterHashMap() {
        return monsterHashMap;
    }

    public void setMonsterHashMap(HashMap<String, Monster> monsterHashMap) {
        this.monsterHashMap = monsterHashMap;
    }

}
