package com.game.dao;

import com.game.dao.sql.GoodsSql;
import com.game.entity.Role;

/**
 * @Author andy
 * @create 2020/7/1 17:51
 */
public class GoodsMapper {

    GoodsSql sql = new GoodsSql();

    public void selectBodyEquipment(Role role){
        sql.selectBodyEquipment(role);
    }

    public void updateRoleBodyEquipment(Role role){
        sql.updateRoleBodyEquipment(role);
    }

    public void selectPackage(Role role){
        sql.selectPackage(role);
    }

    public void updatePackage(Role role){
        sql.updatePackage(role);
    }

}
