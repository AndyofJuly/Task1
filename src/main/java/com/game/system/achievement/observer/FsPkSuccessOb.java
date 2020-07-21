package com.game.system.achievement.observer;

import com.game.system.achievement.Achievement;
import com.game.system.achievement.AchievementService;
import com.game.system.achievement.pojo.Subject;
import com.game.system.role.pojo.Role;

/**
 * 成就观察者：第一次在pk中战胜
 * @Author andy
 * @create 2020/7/13 10:12
 */
public class FsPkSuccessOb implements Observer{
    public FsPkSuccessOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role) {
        AchievementService.countAchievement(Achievement.firstPkSuccess.getDesc(),role);

        AchievementService.checkIfComplete(Achievement.firstPkSuccess.getDesc(),role);

        SerialTaskOb.checkAchievement(0,role);

    }
}
