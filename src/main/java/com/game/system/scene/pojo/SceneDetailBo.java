package com.game.system.scene.pojo;

import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 场景中的实体
 * @Author andy
 * @create 2020/6/17 16:16
 */
public class SceneDetailBo {
    /** 场景名 */
    private String sceneName;
    /** 角色集合 */
    private ArrayList<Role> roleArrayList;
    /** npc集合 */
    private HashMap<Integer, Npc> npcHashMap;
    /** 怪物集合 */
    private HashMap<String, Monster> monsterHashMap;

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public ArrayList<Role> getRoleArrayList() {
        return roleArrayList;
    }

    public void setRoleArrayList(ArrayList<Role> roleArrayList) {
        this.roleArrayList = roleArrayList;
    }

    public HashMap<Integer, Npc> getNpcHashMap() {
        return npcHashMap;
    }

    public void setNpcHashMap(HashMap<Integer, Npc> npcHashMap) {
        this.npcHashMap = npcHashMap;
    }

    public HashMap<String, Monster> getMonsterHashMap() {
        return monsterHashMap;
    }

    public void setMonsterHashMap(HashMap<String, Monster> monsterHashMap) {
        this.monsterHashMap = monsterHashMap;
    }
}
