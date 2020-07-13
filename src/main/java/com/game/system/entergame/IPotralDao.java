package com.game.system.entergame;

import com.game.system.role.pojo.Role;

import java.util.ArrayList;

/**
 * @Author andy
 * @create 2020/7/7 12:16
 */
public interface IPotralDao {

    /**
     * 用户注册
     * @param username 用户名
     * @param password 用户密码
     * @return boolean
     */
    boolean insertRegister(String username, String password);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 用户密码
     * @return int
     */
    int selectLogin(String username, String password);

    /**
     * 获取该用户的所有角色
     * @param userId 用户id
     * @return ArrayList
     */
    ArrayList<Integer> selectRole(int userId);

    /**
     * 检查角色名
     * @param rolename 角色名
     * @return boolean
     */
    boolean checkRoleName(String rolename);

    /**
     * 角色注册
     * @param rolename 角色名
     * @param careerId 角色职业id
     */
    void insertRegisterRole(String rolename,int careerId);

    /**
     * 查找角色并登陆
     * @param roleId 角色id
     * @return Role
     */
    Role selectLoginRole(int roleId);

    /**
     * 更新角色信息
     * @param role 角色
     */
    void updateRoleInfo(Role role);

}
