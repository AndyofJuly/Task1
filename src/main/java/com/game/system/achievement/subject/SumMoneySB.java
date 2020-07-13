package com.game.system.achievement.subject;

import com.game.system.achievement.observer.SumMoneyOB;
import com.game.system.achievement.observer.TalkNpcOB;
import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author andy
 * @create 2020/7/13 10:12
 */
public class SumMoneySB {
    private static SumMoneyOB sumMoneyOB;

    public static void registerObserver(SumMoneyOB observer){
        sumMoneyOB = observer;
    }

    public static void notifyObservers(int targetId, Role role) {
        sumMoneyOB.checkAchievement(targetId,role);
    }
}
