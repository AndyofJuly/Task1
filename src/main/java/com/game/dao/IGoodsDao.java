package com.game.dao;

import com.game.entity.Role;

/**
 * @Author andy
 * @create 2020/7/7 12:16
 */
public interface IGoodsDao {

    /**
     * 获取角色身穿装备
     * @param role 角色
     */
    void selectBodyEquipment(Role role);

    /**
     * 更新角色身穿装备
     * @param role 角色
     */
    void updateRoleBodyEquipment(Role role);

    /**
     * 获取角色背包物品
     * @param role 角色
     */
    void selectPackage(Role role);

    /**
     * 更新角色背包物品
     * @param role 角色
     */
    void updatePackage(Role role);

}
