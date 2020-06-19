package com.game.entity.vo;

import com.game.entity.Monster;
import com.game.entity.Npc;
import com.game.entity.Role;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 场景中的实体
 * @Author andy
 * @create 2020/6/17 16:16
 */
public class SceneDetailVo {
    private String sceneName;
    private ArrayList<Role> roleArrayList;
    private String[] npcIdArray;
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

    public String[] getNpcIdArray() {
        return npcIdArray;
    }

    public void setNpcIdArray(String[] npcIdArray) {
        this.npcIdArray = npcIdArray;
    }

    public HashMap<String, Monster> getMonsterHashMap() {
        return monsterHashMap;
    }

    public void setMonsterHashMap(HashMap<String, Monster> monsterHashMap) {
        this.monsterHashMap = monsterHashMap;
    }
}