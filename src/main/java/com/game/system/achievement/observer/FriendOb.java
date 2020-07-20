package com.game.system.achievement.observer;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.achievement.subject.SerialTaskSB;
import com.game.system.achievement.subject.Subject;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:10
 */
public class FriendOb implements Observer{
    public FriendOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){
        if(targetId!=0){return;}
        role.getAchievementBo().setCountFriend(role.getAchievementBo().getCountFriend()+1);

        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = Const.achieve.TASK_FRIEDN.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean euipCompare = role.getAchievementBo().getCountFriend()>=AchieveResource.getAchieveStaticHashMap().get(achievId).getCount();
            if(staticSearch && euipCompare){
                role.getAchievementBo().getAchievementHashMap().put(achievId,true);
            }
        }

        SerialTaskOb.checkAchievement(0,role);
    }
}
