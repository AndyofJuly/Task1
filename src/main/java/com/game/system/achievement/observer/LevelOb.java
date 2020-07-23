package com.game.system.achievement.observer;

import com.game.system.achievement.Achievement;
import com.game.system.achievement.AchievementService;
import com.game.system.achievement.entity.Subject;
import com.game.system.role.entity.Role;

/**
 * 成就观察者：等级提升到N级
 * @Author andy
 * @create 2020/7/13 10:03
 */
public class LevelOb implements Observer{
    public LevelOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){
        for(Achievement achievement : Achievement.values()){
            if(achievement.getDesc().equals(Achievement.levelUpA.getDesc())){
                role.getAchievementCountMap().put(achievement.getId(),role.getLevel());
            }
        }

        AchievementService.checkIfComplete(Achievement.levelUpA.getDesc(),role);

        SerialTaskOb.checkAchievement(0,role);

    }
}
