package com.game.system.union;

import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/7 12:16
 */
public interface IUnionDao {

    /** 获取所有公会 */
    void selectUnion();

    /** 获取公会成员 */

    void selectUnionMemb();

    /** 获取公会仓库物品 */
    void selectUnionStore();

    /**
     * 更新所有公会
     * @param role 角色
     */
    void updateUnion(Role role);

    /**
     * 更新公会仓库
     * @param role 角色
     */
    void updateUnionStore(Role role);

    /** 更新公会成员 */
    void updateUnionMemb();

}
