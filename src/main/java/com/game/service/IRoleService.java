package com.game.service;

import com.game.entity.Role;
import com.game.entity.bo.SceneDetailBo;

/**
 * @Author andy
 * @create 2020/7/7 10:41
 */
public interface IRoleService {

    /**
     * 移动切换场景
     * @param lastSceneId 目标场景id
     * @param role 角色
     * @return boolean
     */
    boolean moveTo(int lastSceneId, Role role);

    /**
     * 获取场景信息
     * @param sceneId 场景id
     * @return SceneDetailBo
     */
    SceneDetailBo placeDetail(int sceneId);

    /**
     * 维修装备
     * @param equipmentId 装备id
     * @param role 角色
     * @return String
     */
    String repairEquipment(int equipmentId,Role role);

    /**
     * 穿戴装备
     * @param equipmentId 装备id
     * @param role 角色
     * @return String
     */
    String putOnEquipment(int equipmentId,Role role);

    /**
     * 脱下装备
     * @param equipmentId 装备id
     * @param role 角色
     * @return String
     */
    String takeOffEquipment(int equipmentId,Role role);

    /**
     * 使用药品
     * @param potionId 药品id
     * @param role 角色
     * @return boolean
     */
    boolean useDrug(int potionId,Role role);

    /**
     * 与玩家pk
     * @param skillId 技能id
     * @param targetRoleId pk角色id
     * @param role 角色
     * @return String
     */
    String pkPlayer (int skillId, int targetRoleId, Role role);

    /**
     * 场景内走路移动
     * @param x 水平方向移动距离
     * @param y 垂直方向移动距离
     * @param role 角色
     * @return String
     */
    String walkTo(int x,int y,Role role);

    /**
     * 申请添加好友
     * @param friendId 好友id
     * @param role 角色
     */
    void askFriend(int friendId, Role role);

    /**
     * 接受好友申请
     * @param friendId 好友id
     * @param role 角色
     */
    void addFriend(int friendId, Role role);

    /**
     * 升级-测试用
     * @param level 等级
     * @param role 角色
     */
    void levelUp(int level,Role role);

    /**
     * 获取当前成就信息
     * @param role 角色
     * @return String
     */
    String getAchievment(Role role);

    /**
     * 获取背包物品
     * @param role 角色
     * @return String
     */
    String getPackage(Role role);

    /**
     * 获得身上装备信息
     * @param role 角色
     * @return String
     */
    String getBodyEquip(Role role);

    /**
     * 打印场景详细信息
     * @param sceneId 场景id
     * @return String
     */
    String printSceneDetail(int sceneId);

    /**
     * 获得当前视野信息
     * @param myRole 角色
     * @return String
     */
    String printViewDetail(Role myRole);

    /**
     * 与Npc对话
     * @param npcId npcId
     * @param role  角色
     * @return String
     */
    String getNpcReply(int npcId,Role role);

    /**
     * 获得角色自己当前信息
     * @param role 角色
     * @return String
     */
    String getRoleInfo(Role role);

    /**
     * 获得怪物信息
     * @param monsterId 怪物id
     * @param role 角色
     * @return String
     */
    String getMonsterInfo(int monsterId,Role role);

    /**
     * 测试
     * @param role 角色
     * @return String
     */
    String testCode(Role role);

    /**
     * 整理背包
     * @param role 角色
     * @return String
     */
    String orderPackage(Role role);

    /**
     * 背包物品随机存放
     * @param role 角色
     * @return String
     */
    String randPackage(Role role);

    /**
     * 获得背包格子信息
     * @param role 角色
     * @return String
     */
    String getPackageInfo(Role role);

}
