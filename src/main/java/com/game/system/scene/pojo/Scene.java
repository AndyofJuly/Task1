package com.game.system.scene.pojo;

import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 场景类
 * @Author andy
 * @create 2020/5/13 11:10
 */
public class Scene {

    /** 具体随机场景id */
    private int id;
    /** 具体场景随机Name */
    private String name;
    /** 资源类中的静态id */
    private int sceneId;
    /** 该场景下的角色集合 */
    private final ArrayList<Role> roleAll = new ArrayList<>();
    /** 该场景下的怪物集合 */
    private final HashMap<String, Monster> monsterHashMap = new HashMap<>();
    /** 该场景下的网格集合 */
    private final HashMap<Integer, Grid> gridHashMap = new HashMap<>();

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

    public HashMap<String, Monster> getMonsterHashMap() {
        return monsterHashMap;
    }

    public HashMap<Integer, Grid> getGridHashMap() {
        return gridHashMap;
    }

}
