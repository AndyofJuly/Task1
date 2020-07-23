package com.game.system.achievement.observer;

import com.game.system.achievement.Achievement;
import com.game.system.achievement.AchievementService;
import com.game.system.achievement.entity.Subject;
import com.game.system.role.entity.Role;

/**
 * 成就观察者：通关某个副本
 * @Author andy
 * @create 2020/7/13 10:08
 */
public class DungeonsOb implements Observer{
    public DungeonsOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){

        AchievementService.countAchievementWithTarget(targetId, Achievement.passDungeons.getDesc(),role);

        AchievementService.checkIfCompleteWithTarget(targetId,Achievement.passDungeons.getDesc(),role);

        SerialTaskOb.checkAchievement(0,role);

    }
}
