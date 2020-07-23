package com.game.system.achievement.observer;

import com.game.system.achievement.Achievement;
import com.game.system.achievement.AchievementService;
import com.game.system.achievement.entity.Subject;
import com.game.system.role.entity.Role;

/**
 * 成就观察者：击杀特定小怪N只
 * @Author andy
 * @create 2020/7/13 10:03
 */
public class SlayMonsterOb implements Observer{
    public SlayMonsterOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){

        AchievementService.countAchievementWithTarget(targetId, Achievement.killMonsterThief.getDesc(),role);

        AchievementService.checkIfCompleteWithTarget(targetId,Achievement.killMonsterThief.getDesc(),role);

        SerialTaskOb.checkAchievement(0,role);

    }
}
