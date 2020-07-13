package com.game.system.achievement.subject;

import com.game.system.achievement.observer.BodyEquipLvOB;
import com.game.system.achievement.observer.TalkNpcOB;
import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author andy
 * @create 2020/7/13 10:10
 */
public class BodyEquipLvSB {
    private static BodyEquipLvOB bodyEquipLvOB;

    public static void registerObserver(BodyEquipLvOB observer){
        bodyEquipLvOB = observer;
    }

    public static void notifyObservers(int targetId, Role role) {
        bodyEquipLvOB.checkAchievement(targetId,role);
    }
}
