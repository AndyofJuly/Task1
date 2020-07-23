package com.game.system.achievement.observer;

import com.game.system.role.entity.Role;

/**
 * 观察者接口
 * @Author andy
 * @create 2020/7/16 12:35
 */
public interface Observer {

    /**
     * 成就观察者执行任务
     * @param targetId 成就目标id，例如与id为20001的NPC对话成就
     * @param role 角色
     * @return 信息提示
     */
    void checkAchievement(int targetId, Role role);

}
