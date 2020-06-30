package com.game.dao;

import com.game.entity.Role;

import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/6/17 18:30
 */
public class RoleMapper {
    ConnectSql sql = new ConnectSql();

    public boolean insertRegister(String username, String password) {
        return sql.insertRegister(username,password);
    }

    public boolean selectLogin(String username, String password) {
        return sql.selectLogin(username,password);
    }

    public boolean insertRegisterRole(String rolename,int careerId){
        return sql.insertRegisterRole(rolename,careerId);
    }

    public int selectLoginRole(String rolename,int roleId){
        return sql.selectLoginRole(rolename,roleId);
    }

    public void insertRoleScenes(int scenesId, Role role){
        sql.insertRoleScenes(scenesId,role);
    }

    public int selectRoleIdByName(String rolename){
        return sql.selectRoleIdByName(rolename);
    }

    public int selectRoleScenesId(String rolename){
        return sql.selectRoleScenesId(rolename);
    }

    public HashMap<Integer, HashMap<Integer,Integer>> selectBuyRecord(int roleId){
        return sql.selectBuyRecord(roleId);
    }

    public void insertBuyRecord(int roleId,int goodsid,int num){
        sql.insertBuyRecord(roleId,goodsid,num);
    }

}
