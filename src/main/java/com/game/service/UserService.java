package com.game.service;

import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.dao.RoleMapper;
import com.game.entity.Role;
import com.game.entity.vo.GridVo;
import com.game.service.assis.GlobalResource;
import com.game.service.assis.InitGame;
import com.game.service.assis.InitRole;


/**
 * 用户一些功能的方法实现
 * @Author andy
 * @create 2020/5/11 21:50
 */

public class UserService {

    RoleMapper roleMapper = new RoleMapper();
    //角色登录
    public String loginRole(String rolename,int roleId){
        int careerId = roleMapper.selectLoginRole(rolename,roleId);
        if(careerId!=0){
            GlobalResource.getRoleHashMap().put(roleId,new Role(roleId,rolename, roleMapper.selectRoleScenesId(rolename)));
            //登录时职业从数据库中查，设置到缓存中
            Role role = GlobalResource.getRoleHashMap().get(roleId);
            role.setCareerId(careerId);
            //角色所在的场景id，然后在该id场景中加入该角色-此处报错说明角色进入了被回收的场景中，修改数据库
            new InitGame();
            GlobalResource.getScenes().get(roleMapper.selectRoleScenesId(rolename)).getRoleAll().add(role);
            InitRole.setEnterSuccess(true);
            InitRole.init(roleId);
            //网格中增加该角色
            GlobalResource.getScenes().get(role.getNowScenesId()).getGridHashMap().get(role.getCurGridId()).getGridRoleMap().put(roleId,role);
            //九宫格初始化
            role.setGridVo(new GridVo(roleId));

            return Const.start.LOGIN_SUCCESS;
        }else {
            return Const.start.LOGIN_FAILURE;
        }
    }
}
