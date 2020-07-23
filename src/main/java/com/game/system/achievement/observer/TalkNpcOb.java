package com.game.system.achievement.observer;

import com.game.system.achievement.Achievement;
import com.game.system.achievement.AchievementService;
import com.game.system.achievement.entity.Subject;
import com.game.system.role.entity.Role;

/**
 * 成就观察者：与指定NPC对话
 * @Author andy
 * @create 2020/7/13 10:04
 */
public class TalkNpcOb implements Observer{

    public TalkNpcOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){
        AchievementService.countAchievementWithTarget(targetId,Achievement.talkNpc.getDesc(),role);

        AchievementService.checkIfCompleteWithTarget(targetId,Achievement.talkNpc.getDesc(),role);

        SerialTaskOb.checkAchievement(0,role);

    }
}
