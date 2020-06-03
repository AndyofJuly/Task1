package com.game.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.entity.*;
import com.game.entity.excel.*;

import java.util.HashMap;

/**
 * 初始化对象的静态属性，利用ExcelToJson工具类从excel表中解析出的json数据，转换为java对象
 * @Author andy
 * @create 2020/5/21 12:08
 */

public class InitStaticResource {

    public static HashMap<Integer, Scene> scenes = new HashMap<Integer,Scene>();
    public static HashMap<Integer, Npc> npcs = new HashMap<Integer,Npc>();
    public static HashMap<Integer,Monster> monsters = new HashMap<Integer,Monster>();

    public static HashMap<Integer, SceneStatic> scenesStatics = new HashMap<Integer,SceneStatic>();
    public static HashMap<Integer, NpcStatic> npcsStatics = new HashMap<Integer,NpcStatic>();
    public static HashMap<Integer, MonsterStatic> monstersStatics = new HashMap<Integer,MonsterStatic>();
    public static HashMap<String, String[]> places= new HashMap<String,String[]>();
    public static int initSceneId;

    public static HashMap<Integer, SkillStatic> skillStaticHashMap = new HashMap<Integer, SkillStatic>();
    public static HashMap<Integer, EquipmentStatic> equipmentStaticHashMap = new HashMap<Integer, EquipmentStatic>();
    public static HashMap<Integer, PotionStatic> potionStaticHashMap = new HashMap<Integer, PotionStatic>();

    public static HashMap<Integer, RoleStatic> roleStaticHashMap = new HashMap<Integer, RoleStatic>();

    static JSONArray result;
    static JSONObject jsonObject;
    static {
        result = ExcelToJson.getNeed(Const.SCENE_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            SceneStatic sceneStatic = JSON.parseObject(jsonObject.toJSONString(), SceneStatic.class);
            scenesStatics.put(sceneStatic.getId(),sceneStatic);
            scenes.put(sceneStatic.getId(),new Scene(sceneStatic.getId()));
            places.put(sceneStatic.getName(),sceneStatic.getSceneRelation());
        }

        //角色第一次开始游戏时的初始化场景
        //initSceneId = Integer.parseInt(ExcelToJson.getConfigPath());
        initSceneId = Const.INIT_SCENE;

        result = ExcelToJson.getNeed(Const.NPC_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            NpcStatic npcStatic = JSON.parseObject(jsonObject.toJSONString(), NpcStatic.class);
            npcsStatics.put(npcStatic.getId(),npcStatic);
            npcs.put(npcStatic.getId(),new Npc(npcStatic.getId()));
        }

        result = ExcelToJson.getNeed(Const.MONSTER_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            MonsterStatic monsterStatic = JSON.parseObject(jsonObject.toJSONString(), MonsterStatic.class);
            monstersStatics.put(monsterStatic.getId(),monsterStatic);
            monsters.put(monsterStatic.getId(),new Monster(monsterStatic.getId()));
        }

        result = ExcelToJson.getNeed(Const.SKILL_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            SkillStatic skillStatic = JSON.parseObject(jsonObject.toJSONString(), SkillStatic.class);
            skillStaticHashMap.put(skillStatic.getId(),skillStatic);
        }

        result = ExcelToJson.getNeed(Const.EQUIPMENT_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            EquipmentStatic equipmentStatic = JSON.parseObject(jsonObject.toJSONString(), EquipmentStatic.class);
            equipmentStaticHashMap.put(equipmentStatic.getId(),equipmentStatic);
        }

        result = ExcelToJson.getNeed(Const.POTION_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            PotionStatic potionStatic = JSON.parseObject(jsonObject.toJSONString(), PotionStatic.class);
            potionStaticHashMap.put(potionStatic.getId(),potionStatic);
        }

        result = ExcelToJson.getNeed(Const.ROLE_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            RoleStatic roleStatic = JSON.parseObject(jsonObject.toJSONString(), RoleStatic.class);
            roleStaticHashMap.put(roleStatic.getLevelId(),roleStatic);
        }

    }
}
