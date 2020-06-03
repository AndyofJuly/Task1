package com.game.entity.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;
import com.game.entity.Scene;
import com.game.entity.excel.SceneStatic;

import java.util.HashMap;

/**
 * 场景类静态配置数据
 * @Author andy
 * @create 2020/6/3 21:43
 */
public class SceneResource {
    public static HashMap<Integer, Scene> scenes = new HashMap<Integer,Scene>();
    public static HashMap<Integer, SceneStatic> scenesStatics = new HashMap<Integer,SceneStatic>();
    public static HashMap<String, String[]> places= new HashMap<String,String[]>();
    public static int initSceneId;

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
    }
}
