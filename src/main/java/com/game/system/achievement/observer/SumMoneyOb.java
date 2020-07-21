package com.game.system.achievement.observer;

import com.game.system.achievement.Achievement;
import com.game.system.achievement.AchievementService;
import com.game.system.achievement.pojo.Subject;
import com.game.system.role.pojo.Role;

/**
 * 成就观察者：当前金币达到指定数目
 * @Author andy
 * @create 2020/7/13 10:12
 */
public class SumMoneyOb implements Observer{
    public SumMoneyOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){

        for(Achievement achievement : Achievement.values()) {
            if (achievement.getDesc().equals(Achievement.sumMoney.getDesc())) {
                role.getAchievementCountMap().put(achievement.getId(),role.getMoney());
            }
        }

        AchievementService.checkIfComplete(Achievement.sumMoney.getDesc(),role);

        SerialTaskOb.checkAchievement(0,role);
    }
}
