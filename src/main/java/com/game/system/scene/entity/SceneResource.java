package com.game.system.scene.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;

import java.util.HashMap;

/**
 * 场景类静态配置数据
 * @Author andy
 * @create 2020/6/3 21:43
 */
public class SceneResource {

    /** 数值配置表对应的场景类，key为id */
    private static HashMap<Integer, SceneStatic> scenesStatics = new HashMap<Integer,SceneStatic>();
    /** 场景地点关系，key为场景id */
    private static HashMap<Integer, Integer[]> places= new HashMap<Integer,Integer[]>();

    static {
        JSONArray result = ExcelToJson.getNeed(Const.SCENE_CONST_PATH);
        for(int i=0;i<result.size();i++){
            JSONObject jsonObject = result.getJSONObject(i);
            SceneStatic sceneStatic = JSON.parseObject(jsonObject.toJSONString(), SceneStatic.class);
            scenesStatics.put(sceneStatic.getId(),sceneStatic);
            places.put(sceneStatic.getId(),sceneStatic.getSceneRelation());
        }
    }

    public static HashMap<Integer, SceneStatic> getScenesStatics() {
        return scenesStatics;
    }

    public static HashMap<Integer, Integer[]> getPlaces() {
        return places;
    }

}
