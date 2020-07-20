package com.game.system.achievement.observer;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.achievement.subject.SerialTaskSB;
import com.game.system.achievement.subject.Subject;
import com.game.system.assist.AssistService;
import com.game.system.bag.pojo.EquipmentResource;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:09
 */
public class BodyEquipLvOb implements Observer{
    public BodyEquipLvOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){
        int sumLevel = 0;
        for(Integer key : role.getEquipmentHashMap().keySet()){
            int staticId = AssistService.getStaticEquipId(role.getEquipmentHashMap().get(key));
            sumLevel+= EquipmentResource.getEquipmentStaticHashMap().get(staticId).getLevel();
        }

        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = Const.achieve.TASK_EQUIP_LEVEL.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean euipCompare = sumLevel>=AchieveResource.getAchieveStaticHashMap().get(achievId).getCount();
            if(staticSearch && euipCompare){
                role.getAchievementBo().getAchievementHashMap().put(achievId,true);
            }
        }

        SerialTaskOb.checkAchievement(0,role);
    }
}
