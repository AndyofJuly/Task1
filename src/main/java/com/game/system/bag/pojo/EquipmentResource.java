package com.game.system.bag.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;

import java.util.HashMap;

/**
 * 装备类静态配置数据
 * @Author andy
 * @create 2020/6/3 21:42
 */
public class EquipmentResource {
    /** 数值配置表对应的装备类，key为id*/
    private static HashMap<Integer, EquipmentStatic> equipmentStaticHashMap = new HashMap<Integer, EquipmentStatic>();

    static {
        JSONArray result = ExcelToJson.getNeed(Const.EQUIPMENT_CONST_PATH);
        for(int i=0;i<result.size();i++){
            JSONObject jsonObject = result.getJSONObject(i);
            EquipmentStatic equipmentStatic = JSON.parseObject(jsonObject.toJSONString(), EquipmentStatic.class);
            equipmentStaticHashMap.put(equipmentStatic.getId(),equipmentStatic);
        }
    }

    public static HashMap<Integer, EquipmentStatic> getEquipmentStaticHashMap() {
        return equipmentStaticHashMap;
    }
}
