package com.game.entity;

import com.game.entity.excel.SceneStatic;

import java.util.ArrayList;

/**
 * 场景类
 * @Author andy
 * @create 2020/5/13 11:10
 */
public class Scene {
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

    public ArrayList<Role> getRoleAll() {
        return roleAll;
    }

    public void setRoleAll(ArrayList<Role> roleAll) {
        this.roleAll = roleAll;
    }
}
