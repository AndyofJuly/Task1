package com.game.dao;

import com.game.dao.sql.RoleSql;
import com.game.entity.Role;

import java.util.ArrayList;

/**
 * @Author andy
 * @create 2020/6/17 18:30
 */
public class RoleMapper {
    RoleSql sql = new RoleSql();

    public boolean insertRegister(String username, String password) {
        return sql.insertRegister(username,password);
    }

    public int selectLogin(String username, String password) {
        return sql.selectLogin(username,password);
    }

    public ArrayList<Integer> selectRole(int userId){
        return sql.selectRole(userId);
    }

    public boolean insertRegisterRole(String rolename,int careerId){
        return sql.insertRegisterRole(rolename,careerId);
    }

    public boolean checkRoleName(String rolename){
        return sql.checkRoleName(rolename);
    }

    public Role selectLoginRole(int roleId){
        return sql.selectLoginRole(roleId);
    }

/*    public void insertRoleScenes(int scenesId, Role role){
        sql.insertRoleScenes(scenesId,role);
    }*/

    public void updateRoleInfo(Role role){
        sql.updateRoleInfo(role);
    }

}
