package com.game.system.achievement.observer;

import com.game.system.achievement.Achievement;
import com.game.system.achievement.AchievementService;
import com.game.system.achievement.entity.Subject;
import com.game.system.role.entity.Role;

/**
 * 成就观察者：获得N件极品装备
 * @Author andy
 * @create 2020/7/13 10:06
 */
public class BestEquipOb implements Observer{
    public BestEquipOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){

        AchievementService.countAchievement(Achievement.getBestEquip.getDesc(),role);

        AchievementService.checkIfComplete(Achievement.getBestEquip.getDesc(),role);

        SerialTaskOb.checkAchievement(0,role);

    }
}
