package com.game.system.achievement.observer;

import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.achievement.subject.SerialTaskSB;
import com.game.system.achievement.subject.Subject;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:11
 */
public class FsTradeOb implements Observer{

    public FsTradeOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            if("firstTrade".equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc())){
                role.getAchievementBo().getAchievementHashMap().put(achievId,true);
            }
        }

        SerialTaskOb.checkAchievement(0,role);
    }
}
