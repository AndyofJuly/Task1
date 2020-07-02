package com.game.service;

import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.dao.GoodsMapper;
import com.game.dao.RecordMapper;
import com.game.dao.RoleMapper;
import com.game.dao.UnionMapper;
import com.game.entity.Role;
import com.game.entity.Scene;
import com.game.entity.vo.GridVo;
import com.game.service.assis.GlobalResource;
import com.game.service.assis.InitGame;
import com.game.service.assis.InitRole;


/**
 * 用户一些功能的方法实现
 * @Author andy
 * @create 2020/5/11 21:50
 */

public class GameService {

    RoleMapper roleMapper = new RoleMapper();
    RecordMapper recordMapper = new RecordMapper();
    UnionMapper unionMapper = new UnionMapper();
    GoodsMapper goodsMapper = new GoodsMapper();

    //角色登录
    public String loginRole(int roleId){
        Role role = roleMapper.selectLoginRole(roleId);
        if(role.getCareerId()!=0){
            getData(role);
            GlobalResource.getRoleHashMap().put(roleId,role);
            new InitGame();
            GlobalResource.getScenes().get(role.getNowScenesId()).getRoleAll().add(role);
            InitRole.setEnterSuccess(true);
            InitRole.init(role);
            //网格中增加该角色
            Scene scene = GlobalResource.getScenes().get(role.getNowScenesId());
            scene.getGridHashMap().get(role.getCurGridId()).getGridRoleMap().put(roleId,role);
            //九宫格初始化
            role.setGridVo(new GridVo(roleId));
            return Const.start.LOGIN_SUCCESS;
        }else {
            return Const.start.LOGIN_FAILURE;
        }
    }

    public boolean registerRole(String roleName,int careerId){
        if(roleMapper.checkRoleName(roleName)){
            return false;
        }
        roleMapper.insertRegisterRole(roleName,careerId);
        return true;
    }

    public String saveDataBase(){
        saveData();
        return "已保存游戏";
    }

    private void getData(Role role){
        recordMapper.selectAchievement(role);
        goodsMapper.selectBodyEquipment(role);
        goodsMapper.selectPackage(role);
    }

    public void saveData(){
        for(Integer key : GlobalResource.getRoleHashMap().keySet()){
            Role role = GlobalResource.getRoleHashMap().get(key);
            roleMapper.updateRoleInfo(role);
            //roleMapper.insertRoleScenes(role.getNowScenesId(),role);
            recordMapper.updateAchievement(role);
            goodsMapper.updateRoleBodyEquipment(role);
            goodsMapper.updatePackage(role);
            unionMapper.updateUnion(role);
            unionMapper.updateUnionStore(role);
        }
    }
}
