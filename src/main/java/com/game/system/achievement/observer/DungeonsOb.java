package com.game.system.achievement.observer;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.achievement.subject.SerialTaskSB;
import com.game.system.achievement.subject.Subject;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:08
 */
public class DungeonsOb implements Observer{
    public DungeonsOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = Const.achieve.TASK_DUNGEONS.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean euipCompare = (targetId==AchieveResource.getAchieveStaticHashMap().get(achievId).getTargetId());
            if(staticSearch && euipCompare){
                role.getAchievementBo().getAchievementHashMap().put(achievId,true);
            }
        }

        SerialTaskOb.checkAchievement(0,role);
    }
}
