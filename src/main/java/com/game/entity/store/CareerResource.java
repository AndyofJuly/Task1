package com.game.entity.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;
import com.game.entity.excel.CareerStatic;

import java.util.HashMap;

/**
 * 角色职业类静态配置数据-对应的实体类为Role，在Role类中进行封装
 * @Author andy
 * @create 2020/6/8 10:35
 */
public class CareerResource {
    //数值配置表对应的角色类，key为id
    private static HashMap<Integer, CareerStatic> careerStaticHashMap = new HashMap<Integer, CareerStatic>();

    static {
        JSONArray result = ExcelToJson.getNeed(Const.CAREER_CONST_PATH);
        for(int i=0;i<result.size();i++){
            JSONObject jsonObject = result.getJSONObject(i);
            CareerStatic careerStatic = JSON.parseObject(jsonObject.toJSONString(), CareerStatic.class);
            careerStaticHashMap.put(careerStatic.getId(),careerStatic);
        }
    }

    public static HashMap<Integer, CareerStatic> getCareerStaticHashMap() {
        return careerStaticHashMap;
    }
}
