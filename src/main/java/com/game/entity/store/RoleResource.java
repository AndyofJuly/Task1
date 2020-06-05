package com.game.entity.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;
import com.game.entity.excel.RoleStatic;

import java.util.HashMap;

/**
 * 角色类静态配置数据
 * @Author andy
 * @create 2020/6/3 21:43
 */
public class RoleResource {
    //数值配置表对应的角色类
    public static HashMap<Integer, RoleStatic> roleStaticHashMap = new HashMap<Integer, RoleStatic>();

    static JSONArray result;
    static JSONObject jsonObject;

    static {
        result = ExcelToJson.getNeed(Const.ROLE_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            RoleStatic roleStatic = JSON.parseObject(jsonObject.toJSONString(), RoleStatic.class);
            roleStaticHashMap.put(roleStatic.getLevelId(),roleStatic);
        }
    }
}
