package com.game.system.achievement.observer;

import com.game.system.achievement.Achievement;
import com.game.system.achievement.AchievementService;
import com.game.system.achievement.entity.Subject;
import com.game.system.role.entity.Role;

/**
 * 成就观察者：第一次与玩家交易
 * @Author andy
 * @create 2020/7/13 10:11
 */
public class FsTradeOb implements Observer{

    public FsTradeOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){
        AchievementService.countAchievement(Achievement.firstTrade.getDesc(),role);

        AchievementService.checkIfComplete(Achievement.firstTrade.getDesc(),role);

        SerialTaskOb.checkAchievement(0,role);
    }
}
