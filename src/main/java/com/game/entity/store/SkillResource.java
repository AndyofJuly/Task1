package com.game.entity.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;
import com.game.entity.Monster;
import com.game.entity.Npc;
import com.game.entity.Scene;
import com.game.entity.excel.*;

import java.util.HashMap;

/**
 * 技能类静态配置数据
 * @Author andy
 * @create 2020/6/3 21:44
 */
public class SkillResource {
    //数值配置表对应的技能类，key为id
    public static HashMap<Integer, SkillStatic> skillStaticHashMap = new HashMap<Integer, SkillStatic>();

    static JSONArray result;
    static JSONObject jsonObject;

    static {
        result = ExcelToJson.getNeed(Const.SKILL_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            SkillStatic skillStatic = JSON.parseObject(jsonObject.toJSONString(), SkillStatic.class);
            skillStaticHashMap.put(skillStatic.getId(),skillStatic);
        }
    }
}
