package com.game.system.union;

import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/7 10:41
 */
public interface IUnionService {

    /**
     * 创建公会
     * @param unionName 公会名
     * @param role 角色
     * @return String
     */
    String createUnion(String unionName, Role role);

    /**
     * 解散公会
     * @param unionId 公会id
     * @param role 角色
     */
    void disbandUnion(int unionId, Role role);

    /**
     * 对某位公会成员任职
     * @param unionId 公会id
     * @param memberId 成员id
     * @param authorityLevel 职务和权限等级
     * @param role 角色
     */
    void appointCareer(int unionId, int memberId, int authorityLevel, Role role);

    /**
     * 申请参加公会
     * @param unionId 公会id
     * @param roleId 角色id
     */
    void applyFor(int unionId, int roleId);

    /**
     * 同意入会申请
     * @param unionId 公会id
     * @param applyRoleId 申请人id
     * @param role 角色
     */
    void agreeApply(int unionId, Integer applyRoleId, Role role);

    /**
     * 开除某位公会成员
     * @param unionId 公会id
     * @param memberId 成员id
     * @param role 角色
     */
    void fireMember(int unionId, Integer memberId, Role role);

    /**
     * 捐款给公会
     * @param unionId 公会id
     * @param money 捐赠数量
     * @param role 角色
     */
    void donateMoney(int unionId, int money, Role role);

    /**
     * 捐赠物品
     * @param unionId 公会id
     * @param goodsId 物品id
     * @param role 角色
     */
    void donateGoods(int unionId, int goodsId, Role role);

    /**
     * 拿取公会物品
     * @param unionId 公会id
     * @param goodsId 物品id
     * @param role 角色
     */
    void getGoods(int unionId, int goodsId, Role role);

    /**
     * 获得公会详细信息
     * @param role 角色
     * @return String
     */
    String getUnionInfo(Role role);

}
