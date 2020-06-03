package com.game.entity.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;
import com.game.entity.Npc;
import com.game.entity.excel.NpcStatic;

import java.util.HashMap;

/**
 * NPC类静态配置数据
 * @Author andy
 * @create 2020/6/3 21:43
 */
public class NpcResource {
    public static HashMap<Integer, Npc> npcs = new HashMap<Integer,Npc>();
    public static HashMap<Integer, NpcStatic> npcsStatics = new HashMap<Integer,NpcStatic>();

    static JSONArray result;
    static JSONObject jsonObject;

    static {
        result = ExcelToJson.getNeed(Const.NPC_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            NpcStatic npcStatic = JSON.parseObject(jsonObject.toJSONString(), NpcStatic.class);
            npcsStatics.put(npcStatic.getId(),npcStatic);
            npcs.put(npcStatic.getId(),new Npc(npcStatic.getId()));
        }
    }
}
