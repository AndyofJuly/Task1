package com.game.service;

import com.game.entity.Role;

/**
 * @Author andy
 * @create 2020/7/7 10:41
 */
public interface ISkillService {

    /**
     * 普通攻击技能-怪物
     * @param skillId 技能id
     * @return int
     */
    int normalAttackSkill(int skillId);

    /**
     * 获取当前角色的技能列表
     * @param role 角色
     * @return String
     */
    String getSkillInfo(Role role);

    /**
     * 普通攻击技能
     * @param skillId 技能id
     * @param monsterId 怪物id
     * @param role 角色
     * @return String
     */
    String useSkillAttack(int skillId, String monsterId, Role role);

    /**
     * 嘲讽技能
     * @param role 角色
     */
    void tauntSkill(Role role);

    /**
     * 群体技能
     * @param skillId 技能id
     * @param role 角色
     */
    void groupAtkSkill(int skillId, Role role);

    /**
     * 群体治疗
     * @param skillId 技能id
     * @param role 角色
     */
    void groupCureSkill(int skillId, Role role);

    /**
     * 召唤术
     * @param monsterId 召唤出宝宝所要攻击的怪物id
     * @param role 角色
     */
    void summonSkill(int monsterId, Role role);

    /**
     * 宝宝攻击
     * @param monsterId 所要攻击的怪物id
     * @param sceneId 场景id
     */
    void babyAttackMonster(String monsterId, int sceneId);

    /**
     * 普攻
     * @param monsterId 怪物id
     * @param role 角色
     * @return String
     */
    String normalAttack(String monsterId, Role role);

}
