package com.game.system.achievement.observer;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.achievement.subject.SerialTaskSB;
import com.game.system.achievement.subject.Subject;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:03
 */
public class SlayMonsterOb implements Observer{
    public SlayMonsterOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){
        if(targetId==0){return;}
        Object oldCount = role.getAchievementBo().getKillMonsterCountMap().get(targetId);
        if(oldCount==null){
            role.getAchievementBo().getKillMonsterCountMap().put(targetId,1);
        }else {
            role.getAchievementBo().getKillMonsterCountMap().put(targetId,(int)oldCount+1);
        }

        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = Const.achieve.TASK_MONSTER.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean euipCompare = (targetId==AchieveResource.getAchieveStaticHashMap().get(achievId).getTargetId());
            if(staticSearch && euipCompare){
                int nowCount = role.getAchievementBo().getKillMonsterCountMap().get(targetId);
                int targetCount = AchieveResource.getAchieveStaticHashMap().get(achievId).getCount();
                if(nowCount!= 0 && nowCount>=targetCount){
                    role.getAchievementBo().getAchievementHashMap().put(achievId,true);
                }
            }
        }

        SerialTaskOb.checkAchievement(0,role);
    }
}
