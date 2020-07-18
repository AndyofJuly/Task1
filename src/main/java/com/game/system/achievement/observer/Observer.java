package com.game.system.achievement.observer;

import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/16 12:35
 */
public interface Observer {
    void checkAchievement(int targetId, Role role);

    void checkAchievement(String target, Role role);

    //void fireTalkNpc(Role role, int NpcId);

    //void fireKillMonster(Role role, int monsterId);
}
