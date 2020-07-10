package com.game.service;

/**
 * @Author andy
 * @create 2020/7/7 10:41
 */
public interface IGameService {

    /**
     * 角色登录
     * @param roleId 角色id
     * @return String
     */
    String loginRole(int roleId);

    /**
     * 角色注册
     * @param roleName 角色名
     * @param careerId 角色职业id
     * @return boolean
     */
    boolean registerRole(String roleName,int careerId);

    /**
     * 数据持久化到数据库
     * @return String
     */
    String saveDataBase();

}
