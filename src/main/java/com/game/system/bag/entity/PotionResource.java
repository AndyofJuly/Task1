package com.game.system.bag.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;

import java.util.HashMap;

/**
 * 药品类静态配置数据
 * @Author andy
 * @create 2020/6/3 21:43
 */
public class PotionResource {
    /** 数值配置表对应的药品类，key为id */
    private static final HashMap<Integer, PotionStatic> potionStaticHashMap = new HashMap<>();

    static {
        JSONArray result = ExcelToJson.getNeed(Const.POTION_CONST_PATH);
        for(int i=0;i<result.size();i++){
            JSONObject jsonObject = result.getJSONObject(i);
            PotionStatic potionStatic = JSON.parseObject(jsonObject.toJSONString(), PotionStatic.class);
            potionStaticHashMap.put(potionStatic.getId(),potionStatic);
        }
    }

    public static HashMap<Integer, PotionStatic> getPotionStaticHashMap() {
        return potionStaticHashMap;
    }
}
