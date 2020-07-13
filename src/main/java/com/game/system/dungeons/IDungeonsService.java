package com.game.system.dungeons;

import com.game.system.role.pojo.Role;

import java.util.ArrayList;

/**
 * @Author andy
 * @create 2020/7/7 10:41
 */
public interface IDungeonsService {

    /**
     * 创建队伍
     * @param dungeonesId 副本id
     * @param role 角色
     * @return String
     */
    String createTeam(int dungeonesId, Role role);

    /**
     * 参加队伍
     * @param teamId 队伍id
     * @param role 角色
     */
    void joinTeam(int teamId, Role role);

    /**
     * 获得队伍中角色列表
     * @param teamId 队伍id
     * @param role 角色
     * @return String
     */
    String getTeamRoleList(int teamId,Role role);

    /**
     * 开始副本
     * @param teamId 队伍id
     * @param role 角色
     */
    void startDungeons (String teamId, Role role);

    /**
     * Boss定时使用技能攻击角色
     * @param teamId 队伍id
     * @param dungeonsId 副本id
     * @param sceneId 场景id
     */
    void bossAttackRole(String teamId,int dungeonsId,int sceneId);

    /**
     * 获得队伍中角色列表
     * @param teamId 队伍id
     * @return ArrayList
     */
    ArrayList<Integer> getRoleList(String teamId);

    /**
     * 获得所有组队列表
     * @return String
     */
    String getTeamList();

    /**
     * 获得所有副本列表
     * @return String
     */
    String getStaticDungeonsList();

}
