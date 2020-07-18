package com.game.system.achievement.observer;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.achievement.subject.SerialTaskSB;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:03
 */
public class LevelOb implements Observer{
/*    public LevelOb(LevelSB subject) {
        subject.registerObserver(this);
    }*/

    @Override
    public void checkAchievement(int targetId, Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = Const.achieve.TASK_LEVEL.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean levelCompare = (role.getLevel()>=AchieveResource.getAchieveStaticHashMap().get(achievId).getCount());
            if(staticSearch && levelCompare){
                role.getAchievementBo().getAchievementHashMap().put(achievId,true);
            }
        }

        SerialTaskSB.notifyObservers(0,role);
    }

    @Override
    public void checkAchievement(String target, Role role) {

    }
}
