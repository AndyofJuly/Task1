package com.game.entity.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;
import com.game.entity.Monster;
import com.game.entity.excel.MonsterStatic;

import java.util.HashMap;

/**
 * 怪物类静态配置数据
 * @Author andy
 * @create 2020/6/3 21:42
 */
public class MonsterResource {

    //数值配置表对应的怪物类
    public static HashMap<Integer, MonsterStatic> monstersStatics = new HashMap<Integer,MonsterStatic>();

    static JSONArray result;
    static JSONObject jsonObject;

    static {
        result = ExcelToJson.getNeed(Const.MONSTER_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            MonsterStatic monsterStatic = JSON.parseObject(jsonObject.toJSONString(), MonsterStatic.class);
            monstersStatics.put(monsterStatic.getId(),monsterStatic);
        }
    }
}
