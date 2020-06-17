package com.game.service;

import com.game.controller.RoleController;
import com.game.dao.ConnectSql;
import com.game.entity.Role;
import com.game.service.assis.InitGame;
import com.game.service.assis.InitRole;


/**
 * 用户一些功能的方法实现
 * @Author andy
 * @create 2020/5/11 21:50
 */

public class UserService {

    //角色登录
    public String loginRole(String rolename,int roleId){
        int careerId = ConnectSql.sql.selectLoginRole(rolename,roleId);
        if(careerId!=0){
            RoleController.roleHashMap.put(roleId,new Role(roleId,rolename,ConnectSql.sql.selectRoleScenesId(rolename)));
            //登录时职业从数据库中查，设置到缓存中
            RoleController.roleHashMap.get(roleId).setCareerId(careerId);
            //角色所在的场景id，然后在该id场景中加入该角色
            InitGame.scenes.get(ConnectSql.sql.selectRoleScenesId(rolename)).getRoleAll().add(RoleController.roleHashMap.get(roleId));
            InitRole.enterSuccess = true;
            InitRole.init(roleId);
            return "登陆成功，您进入到了游戏世界";
        }else {
            return "您没有该角色，登陆失败";
        }
    }
}
