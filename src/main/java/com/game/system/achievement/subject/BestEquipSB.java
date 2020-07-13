package com.game.system.achievement.subject;

import com.game.system.achievement.observer.BestEquipOB;
import com.game.system.achievement.observer.TalkNpcOB;
import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author andy
 * @create 2020/7/13 10:06
 */
public class BestEquipSB {
    private static BestEquipOB bestEquipOB;

    public static void registerObserver(BestEquipOB observer){
        bestEquipOB=observer;
    }

    public static void notifyObservers(int targetId, Role role) {
        bestEquipOB.checkAchievement(targetId,role);
    }
}
