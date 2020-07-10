package com.game.service;

import com.game.entity.Role;

/**
 * @Author andy
 * @create 2020/7/7 10:41
 */
public interface IPackageService {

    /**
     * 放入背包，服务端使用
     * @param goodsId 物品id
     * @param number 数量
     * @param role 角色
     * @return boolean
     */
    boolean putIntoPackage(int goodsId, int number, Role role);

    /**
     * 从背包中取出物品
     * @param goodsId 物品id
     * @param number 数量
     * @param role 角色
     */
    void getFromPackage(int goodsId, int number, Role role);

    /**
     * 角色金钱增加
     * @param number 金钱数量
     * @param role 角色
     */
    void addMoney(int number, Role role);

    /**
     * 角色金钱减少
     * @param number 金钱数量
     * @param role 角色
     */
    void lostMoney(int number, Role role);

    /**
     * 背包减少该物品
     * @param goodsId 物品id
     * @param role 角色
     */
    void lostGoods(int goodsId,Role role);

}
