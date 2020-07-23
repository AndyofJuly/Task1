package com.game.system.scene.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;

import java.util.HashMap;

/**
 * 怪物类静态配置数据
 * @Author andy
 * @create 2020/6/3 21:42
 */
public class MonsterResource {

    /** 数值配置表对应的怪物类，key为id */
    private static HashMap<Integer, MonsterStatic> monstersStatics = new HashMap<>();

    static {
        JSONArray result = ExcelToJson.getNeed(Const.MONSTER_CONST_PATH);
        for(int i=0;i<result.size();i++){
            JSONObject jsonObject = result.getJSONObject(i);
            MonsterStatic monsterStatic = JSON.parseObject(jsonObject.toJSONString(), MonsterStatic.class);
            monstersStatics.put(monsterStatic.getId(),monsterStatic);
        }
    }

    public static HashMap<Integer, MonsterStatic> getMonstersStatics() {
        return monstersStatics;
    }
}
