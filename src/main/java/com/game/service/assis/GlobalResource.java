package com.game.service.assis;

import com.game.entity.Role;
import com.game.entity.Team;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 定义全局的共享资源的设置和获得方法
 * @Author andy
 * @create 2020/6/6 10:58
 */
public class GlobalResource {

    // 组队列表，key为teamId
    private static HashMap<String, Team> teamList = new HashMap<String, Team>();

    //临时副本id获取该副本内的临时怪物id，分别对应key和value
    private static HashMap<Integer,String> tempIdHashMap = new HashMap<>();

    private static ArrayList<Integer> intList  = new ArrayList<>();
    private static ArrayList<String> strList  = new ArrayList<>();

    private static HashMap<Integer, Role> roleHashMap = new HashMap<>();
    //技能计时
    private static Instant useTauntDate;




    public static Instant getUseTauntDate() {
        return useTauntDate;
    }

    public static void setUseTauntDate(Instant useTauntDate) {
        GlobalResource.useTauntDate = useTauntDate;
    }

    public static HashMap<String, Team> getTeamList() {
        return teamList;
    }

    public static void setTeamList(HashMap<String, Team> teamList) {
        GlobalResource.teamList = teamList;
    }

    public static HashMap<Integer, String> getTempIdHashMap() {
        return tempIdHashMap;
    }

    public static HashMap<Integer, Role> getRoleHashMap() {
        return roleHashMap;
    }

    public static void setRoleHashMap(HashMap<Integer, Role> roleHashMap) {
        GlobalResource.roleHashMap = roleHashMap;
    }

    public static ArrayList<Integer> getIntList() {
        return intList;
    }

    public static void setIntList(ArrayList<Integer> intList) {
        GlobalResource.intList = intList;
    }

    public static ArrayList<String> getStrList() {
        return strList;
    }

    public static void setStrList(ArrayList<String> strList) {
        GlobalResource.strList = strList;
    }
}
