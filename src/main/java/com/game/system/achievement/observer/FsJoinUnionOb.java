package com.game.system.achievement.observer;

import com.game.system.achievement.Achievement;
import com.game.system.achievement.AchievementService;
import com.game.system.achievement.entity.Subject;
import com.game.system.role.entity.Role;

/**
 * 成就观察者：第一次加入公会
 * @Author andy
 * @create 2020/7/13 10:11
 */
public class FsJoinUnionOb implements Observer{
    public FsJoinUnionOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role) {
        AchievementService.countAchievement(Achievement.firstJoinUnion.getDesc(),role);

        AchievementService.checkIfComplete(Achievement.firstJoinUnion.getDesc(),role);

        SerialTaskOb.checkAchievement(0,role);

    }
}
