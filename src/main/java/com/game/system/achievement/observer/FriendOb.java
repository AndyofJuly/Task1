package com.game.system.achievement.observer;

import com.game.system.achievement.Achievement;
import com.game.system.achievement.AchievementService;
import com.game.system.achievement.pojo.Subject;
import com.game.system.role.pojo.Role;

/**
 * 成就观察者：添加一个好友
 * @Author andy
 * @create 2020/7/13 10:10
 */
public class FriendOb implements Observer{
    public FriendOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){

        AchievementService.countAchievement(Achievement.addFriend.getDesc(),role);

        AchievementService.checkIfComplete(Achievement.addFriend.getDesc(),role);

        SerialTaskOb.checkAchievement(0,role);

    }
}
