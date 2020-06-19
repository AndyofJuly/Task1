package com.game.entity.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;
import com.game.entity.excel.MonsterStatic;

import java.util.HashMap;

/**
 * 怪物类静态配置数据
 * @Author andy
 * @create 2020/6/3 21:42
 */
public class MonsterResource {

    //数值配置表对应的怪物类，key为id
    private static HashMap<Integer, MonsterStatic> monstersStatics = new HashMap<Integer,MonsterStatic>();
    //为避免再使用循环来根据名称查找id，建立名字与id的键值对

    static JSONArray result;
    static JSONObject jsonObject;

    static {
        result = ExcelToJson.getNeed(Const.MONSTER_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            MonsterStatic monsterStatic = JSON.parseObject(jsonObject.toJSONString(), MonsterStatic.class);
            monstersStatics.put(monsterStatic.getId(),monsterStatic);
/*            public static HashMap<String, Integer> monstersGetIdMap = new HashMap<String, Integer>();
            monstersGetIdMap.put(monsterStatic.getName(),monsterStatic.getId());*/
        }
    }

    public static HashMap<Integer, MonsterStatic> getMonstersStatics() {
        return monstersStatics;
    }
}
