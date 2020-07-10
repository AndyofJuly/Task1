package com.game.entity.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;
import com.game.entity.excel.AchieveStatic;
import com.game.entity.excel.JobStatic;

import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/7/1 15:22
 */
public class AchieveResource {

    //数值配置表对应的职务类，key为id
    private static HashMap<Integer, AchieveStatic> achieveStaticHashMap = new HashMap<Integer, AchieveStatic>();

    static {
        JSONArray result = ExcelToJson.getNeed(Const.ACHIEVE_CONST_PATH);
        for(int i=0;i<result.size();i++){
            JSONObject jsonObject = result.getJSONObject(i);
            AchieveStatic achieveStatic = JSON.parseObject(jsonObject.toJSONString(), AchieveStatic.class);
            achieveStaticHashMap.put(achieveStatic.getId(),achieveStatic);
        }
    }

    public static HashMap<Integer, AchieveStatic> getAchieveStaticHashMap() {
        return achieveStaticHashMap;
    }
}
