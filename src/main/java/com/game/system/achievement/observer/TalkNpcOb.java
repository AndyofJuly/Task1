package com.game.system.achievement.observer;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.achievement.subject.SerialTaskSB;
import com.game.system.achievement.subject.Subject;
import com.game.system.role.RoleService;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:04
 */
public class TalkNpcOb implements Observer{

    public TalkNpcOb(Subject subject) {
        subject.registerObserver(this);//this指的是什么呢，你在外面new TalkNpcOb时对应的对象呗
    }

    @Override
    public void checkAchievement(int targetId, Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = Const.achieve.TASK_NPC.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean npcCompare = (targetId==AchieveResource.getAchieveStaticHashMap().get(achievId).getTargetId());
            if(staticSearch && npcCompare){
                role.getAchievementBo().getAchievementHashMap().put(achievId,true);
            }
        }

        SerialTaskOb.checkAchievement(0,role);
    }
}
