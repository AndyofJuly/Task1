package com.game.system.achievement.observer;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.achievement.subject.SerialTaskSB;
import com.game.system.achievement.subject.Subject;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:12
 */
public class SumMoneyOb implements Observer{
    public SumMoneyOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = Const.achieve.TASK_MONEY.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean euipCompare = (role.getMoney()>=AchieveResource.getAchieveStaticHashMap().get(achievId).getCount());
            if(staticSearch && euipCompare){
                role.getAchievementBo().getAchievementHashMap().put(achievId,true);
            }
        }

        SerialTaskOb.checkAchievement(0,role);
    }
}
