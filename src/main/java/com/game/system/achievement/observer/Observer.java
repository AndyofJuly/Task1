package com.game.system.achievement.observer;

import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/16 12:35
 */
public interface Observer {

    /** 观察者执行任务 */
    void checkAchievement(int targetId, Role role);

}
