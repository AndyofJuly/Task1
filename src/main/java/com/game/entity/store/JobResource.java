package com.game.entity.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.Const;
import com.game.common.ExcelToJson;
import com.game.entity.excel.JobStatic;
import com.game.entity.excel.PotionStatic;

import java.util.HashMap;

/**
 * 公会职务类静态配置数据
 * @Author andy
 * @create 2020/6/30 11:40
 */
public class JobResource {
    //数值配置表对应的职务类，key为id
    private static HashMap<Integer, JobStatic> jobStaticHashMap = new HashMap<Integer, JobStatic>();

    static JSONArray result;
    static JSONObject jsonObject;

    static {
        result = ExcelToJson.getNeed(Const.JOB_CONST_PATH);
        for(int i=0;i<result.size();i++){
            jsonObject = result.getJSONObject(i);
            JobStatic jobStatic = JSON.parseObject(jsonObject.toJSONString(), JobStatic.class);
            jobStaticHashMap.put(jobStatic.getId(),jobStatic);
        }
    }

    public static HashMap<Integer, JobStatic> getJobStaticHashMap() {
        return jobStaticHashMap;
    }
}
