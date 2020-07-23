package com.game.system.achievement.observer;

import com.game.system.achievement.Achievement;
import com.game.system.achievement.AchievementService;
import com.game.system.achievement.entity.Subject;
import com.game.system.gameserver.AssistService;
import com.game.system.bag.entity.EquipmentResource;
import com.game.system.role.entity.Role;

/**
 * 成就观察者：穿戴的装备等级总和达到指定等级
 * @Author andy
 * @create 2020/7/13 10:09
 */
public class BodyEquipLvOb implements Observer{
    public BodyEquipLvOb(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void checkAchievement(int targetId, Role role){
        for(Achievement achievement : Achievement.values()) {
            if (achievement.getDesc().equals(Achievement.sumEquipLevel.getDesc())) {
                int sumLevel = 0;
                for(Integer key : role.getEquipmentHashMap().keySet()){
                    int staticId = AssistService.getStaticEquipId(role.getEquipmentHashMap().get(key));
                    sumLevel+= EquipmentResource.getEquipmentStaticHashMap().get(staticId).getLevel();
                }
                role.getAchievementCountMap().put(achievement.getId(),sumLevel);
            }
        }

        AchievementService.checkIfComplete(Achievement.sumEquipLevel.getDesc(),role);

        SerialTaskOb.checkAchievement(0,role);

    }
}
