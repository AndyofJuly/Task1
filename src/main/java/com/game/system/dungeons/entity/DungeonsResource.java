package com.game.system.dungeons.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.game.common.Const;
import com.game.common.ExcelToJson;

import java.util.HashMap;

/**
 * 副本类静态配置数据
 * @Author andy
 * @create 2020/6/8 10:38
 */
public class DungeonsResource {
    /** 数值配置表对应的副本类，key为id */
    private static HashMap<Integer, DungeonsStatic> dungeonsStaticHashMap = new HashMap<Integer, DungeonsStatic>();

    static {
        JSONArray result = ExcelToJson.getNeed(Const.DUNGEONS_CONST_PATH);
        for(int i=0;i<result.size();i++){
            JSONObject jsonObject = result.getJSONObject(i);
            DungeonsStatic dungeonsStatic = JSON.parseObject(jsonObject.toJSONString(), DungeonsStatic.class);
            dungeonsStaticHashMap.put(dungeonsStatic.getId(),dungeonsStatic);
        }
    }

    public static HashMap<Integer, DungeonsStatic> getDungeonsStaticHashMap() {
        return dungeonsStaticHashMap;
    }
}
