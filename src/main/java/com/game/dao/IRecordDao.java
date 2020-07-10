package com.game.dao;

import com.game.entity.Role;

import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/7/7 12:16
 */
public interface IRecordDao {

    /**
     * 查找角色购买记录
     * @param roleId 角色id
     * @return HashMap
     */
    HashMap<Integer, HashMap<Integer,Integer>> selectBuyRecord(int roleId);

    /**
     * 更新角色购买记录
     * @param roleId 角色id
     * @param goodsid 物品id
     * @param num 数量
     */
    void insertBuyRecord(int roleId, int goodsid, int num);

    /**
     * 获取角色成就
     * @param role 角色
     */
    void selectAchievement(Role role);

    /**
     * 更新角色成就
     * @param role 角色
     */
    void updateAchievement(Role role);
}
