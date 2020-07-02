package com.game.dao;

import com.game.dao.sql.RecordSql;
import com.game.entity.Role;

import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/7/1 17:37
 */
public class RecordMapper {

    RecordSql sql = new RecordSql();

    public void selectAchievement(Role role){
        sql.selectAchievement(role);
    }
    public void updateAchievement(Role role){
        sql.updateAchievement(role);
    }

    public HashMap<Integer, HashMap<Integer,Integer>> selectBuyRecord(int roleId){
        return sql.selectBuyRecord(roleId);
    }

    public void insertBuyRecord(int roleId,int goodsid,int num){
        sql.insertBuyRecord(roleId,goodsid,num);
    }
}
