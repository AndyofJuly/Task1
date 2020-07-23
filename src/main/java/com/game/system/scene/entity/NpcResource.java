package com.game.system.scene.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;

import java.util.HashMap;

/**
 * NPC类静态配置数据
 * @Author andy
 * @create 2020/6/3 21:43
 */
public class NpcResource {
    /** 数值配置表对应的npc类 */
    private static final HashMap<Integer, NpcStatic> npcsStatics = new HashMap<>();

    static {
        JSONArray result = ExcelToJson.getNeed(Const.NPC_CONST_PATH);
        for(int i=0;i<result.size();i++){
            JSONObject jsonObject = result.getJSONObject(i);
            NpcStatic npcStatic = JSON.parseObject(jsonObject.toJSONString(), NpcStatic.class);
            npcsStatics.put(npcStatic.getId(),npcStatic);
        }
    }

    public static HashMap<Integer, NpcStatic> getNpcsStatics() {
        return npcsStatics;
    }
}
