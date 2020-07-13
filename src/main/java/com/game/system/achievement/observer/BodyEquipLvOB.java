package com.game.system.achievement.observer;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.achievement.subject.BodyEquipLvSB;
import com.game.system.achievement.subject.SerialTaskSB;
import com.game.system.achievement.subject.TalkNpcSB;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:09
 */
public class BodyEquipLvOB {
/*    public BodyEquipLvOB(BodyEquipLvSB subject) {
        subject.registerObserver(this);
    }*/

    public void checkAchievement(int targetId, Role role){
        int sumLevel = 0;
        for(Integer equipmentId : role.getEquipmentHashMap().keySet()){
            sumLevel+=role.getEquipmentHashMap().get(equipmentId).getLevel();
        }

        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = Const.achieve.TASK_EQUIP_LEVEL.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean euipCompare = sumLevel>=AchieveResource.getAchieveStaticHashMap().get(achievId).getCount();
            if(staticSearch && euipCompare){
                role.getAchievementBo().getAchievementHashMap().put(achievId,true);
            }
        }

        SerialTaskSB.notifyObservers(0,role);
    }
}
