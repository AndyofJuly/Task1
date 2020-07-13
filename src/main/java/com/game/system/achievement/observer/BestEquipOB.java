package com.game.system.achievement.observer;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.achievement.subject.BestEquipSB;
import com.game.system.achievement.subject.SerialTaskSB;
import com.game.system.achievement.subject.TalkNpcSB;
import com.game.system.bag.pojo.EquipmentResource;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:06
 */
public class BestEquipOB {
/*    public BestEquipOB(BestEquipSB subject) {
        subject.registerObserver(this);
    }*/

    public void checkAchievement(int targetId, Role role){
        if(targetId==0){return;}
        int quality = EquipmentResource.getEquipmentStaticHashMap().get(targetId).getQuality();
        if(quality==1){
            role.getMyPackageBo().setSumBestNum();
        }
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = Const.achieve.TASK_EQUIP.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean euipCompare = role.getMyPackageBo().getBestNum()>=AchieveResource.getAchieveStaticHashMap().get(achievId).getCount();
            if(staticSearch && euipCompare){
                role.getAchievementBo().getAchievementHashMap().put(achievId,true);
            }
        }

        SerialTaskSB.notifyObservers(0,role);
    }
}
