package com.game.system.achievement.observer;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.achievement.subject.FsJoinUnionSB;
import com.game.system.achievement.subject.SerialTaskSB;
import com.game.system.achievement.subject.TalkNpcSB;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:11
 */
public class FsJoinUnionOB {
/*    public FsJoinUnionOB(FsJoinUnionSB subject) {
        subject.registerObserver(this);
    }*/

    public void checkAchievement(String target, Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            if(target.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc())){
                role.getAchievementBo().getAchievementHashMap().put(achievId,true);
            }
        }

        SerialTaskSB.notifyObservers(0,role);
    }
}
