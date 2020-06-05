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
    // 场景id-静态属性
    private int sceneId;

    //该场景下的角色集合
    private ArrayList<Role> roleAll = new ArrayList<Role>();
    //该场景下的怪物集合
    private HashMap<Integer,Monster> monsterHashMap = new HashMap<Integer,Monster>();

    public Scene(int sceneId) {
        this.sceneId = sceneId;
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

    public HashMap<Integer, Monster> getMonsterHashMap() {
        return monsterHashMap;
    }

    public void setMonsterHashMap(HashMap<Integer, Monster> monsterHashMap) {
        this.monsterHashMap = monsterHashMap;
    }
}
