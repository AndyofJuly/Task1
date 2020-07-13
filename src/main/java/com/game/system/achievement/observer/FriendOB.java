package com.game.system.achievement.observer;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.achievement.subject.FriendSB;
import com.game.system.achievement.subject.SerialTaskSB;
import com.game.system.achievement.subject.TalkNpcSB;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:10
 */
public class FriendOB {
/*    public FriendOB(FriendSB subject) {
        subject.registerObserver(this);
    }*/

    public void checkAchievement(int targetId, Role role){
        if(targetId!=0){return;}
        role.getAchievementBo().setCountFriend();

        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = Const.achieve.TASK_FRIEDN.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean euipCompare = role.getAchievementBo().getCountFriend()>=AchieveResource.getAchieveStaticHashMap().get(achievId).getCount();
            if(staticSearch && euipCompare){
                role.getAchievementBo().getAchievementHashMap().put(achievId,true);
            }
        }

        SerialTaskSB.notifyObservers(0,role);
    }
}
