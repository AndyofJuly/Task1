package com.game.system.achievement.observer;

import com.game.system.achievement.Achievement;
import com.game.system.achievement.AchievementService;
import com.game.system.role.entity.Role;

/**
 * 成就观察者：完成某一系列任务
 * @Author andy
 * @create 2020/7/13 10:13
 */
public class SerialTaskOb{

    public static void checkAchievement(int targetId, Role role){
        AchievementService.completSeriaAchievement(Achievement.completeTask.getDesc(),role);
    }
}
