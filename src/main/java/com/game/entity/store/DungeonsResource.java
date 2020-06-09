package com.game.entity.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.game.common.Const;
import com.game.common.ExcelToJson;
import com.game.entity.excel.DungeonsStatic;

import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/6/8 10:38
 */
public class DungeonsResource {
    //数值配置表对应的副本类
    public static HashMap<Integer, DungeonsStatic> dungeonsStaticHashMap = new HashMap<Integer, DungeonsStatic>();

    static JSONArray result;
    static JSONObject jsonObject;

    static {
        result = ExcelToJson.getNeed(Const.DUNGEONS_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            DungeonsStatic dungeonsStatic = JSON.parseObject(jsonObject.toJSONString(), DungeonsStatic.class);
            dungeonsStaticHashMap.put(dungeonsStatic.getId(),dungeonsStatic);
        }
    }
}
