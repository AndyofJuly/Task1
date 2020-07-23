package com.game.system.gameserver;

import com.game.system.bag.entity.Equipment;
import com.game.system.role.entity.Role;
import com.game.system.scene.entity.Scene;
import com.game.system.dungeons.entity.Team;

import java.time.Instant;
import java.util.HashMap;

/**
 * 定义全局的共享资源的设置和获得方法
 * @Author andy
 * @create 2020/6/6 10:58
 */
public class GlobalInfo {

    /** 组队列表，key为teamId*/
    private static HashMap<String, Team> teamList = new HashMap<String, Team>();
    /** 临时副本id获取该副本内的临时怪物id，分别对应key和value*/
    private static HashMap<Integer,String> tempIdHashMap = new HashMap<>();
    /** 游戏中的所有角色*/
    private static HashMap<Integer, Role> roleHashMap = new HashMap<>();
    /** 游戏中的所有场景*/
    private static HashMap<Integer, Scene> scenes = new HashMap<Integer,Scene>();
    /** 游戏中的所有已存在装备*/
    private static HashMap<Integer, Equipment> equipmentHashMap = new HashMap<>();
    /** 技能计时*/
    private static Instant useTauntDate;

    public static Instant getUseTauntDate() {
        return useTauntDate;
    }

    public static void setUseTauntDate(Instant useTauntDate) {
        GlobalInfo.useTauntDate = useTauntDate;
    }

    public static HashMap<String, Team> getTeamList() {
        return teamList;
    }

    public static HashMap<Integer, String> getTempIdHashMap() {
        return tempIdHashMap;
    }

    public static HashMap<Integer, Role> getRoleHashMap() {
        return roleHashMap;
    }

    public static HashMap<Integer, Scene> getScenes() {
        return scenes;
    }

    public static HashMap<Integer, Equipment> getEquipmentHashMap() {
        return equipmentHashMap;
    }

    public static void setEquipmentHashMap(HashMap<Integer, Equipment> equipmentHashMap) {
        GlobalInfo.equipmentHashMap = equipmentHashMap;
    }
}
