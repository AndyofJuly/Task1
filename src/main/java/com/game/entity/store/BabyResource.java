package com.game.entity.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;
import com.game.entity.excel.BabyStatic;
import com.game.entity.excel.MonsterStatic;

import java.util.HashMap;

/**
 * 宝宝类静态配置数据
 * @Author andy
 * @create 2020/6/11 16:43
 */
public class BabyResource {

    //数值配置表对应的宝宝类，key为id
    private static HashMap<Integer, BabyStatic> babyStatics = new HashMap<Integer,BabyStatic>();

    static JSONArray result;
    static JSONObject jsonObject;

    static {
        result = ExcelToJson.getNeed(Const.BABY_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            BabyStatic babyStatic = JSON.parseObject(jsonObject.toJSONString(), BabyStatic.class);
            babyStatics.put(babyStatic.getId(),babyStatic);
        }
    }

    public static HashMap<Integer, BabyStatic> getBabyStatics() {
        return babyStatics;
    }
}
