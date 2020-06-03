package com.game.entity.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;
import com.game.entity.excel.PotionStatic;

import java.util.HashMap;

/**
 * 药品类静态配置数据
 * @Author andy
 * @create 2020/6/3 21:43
 */
public class PotionResource {
    public static HashMap<Integer, PotionStatic> potionStaticHashMap = new HashMap<Integer, PotionStatic>();

    static JSONArray result;
    static JSONObject jsonObject;

    static {
        result = ExcelToJson.getNeed(Const.POTION_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            PotionStatic potionStatic = JSON.parseObject(jsonObject.toJSONString(), PotionStatic.class);
            potionStaticHashMap.put(potionStatic.getId(),potionStatic);
        }
    }
}
