package com.game.system.achievement.subject;

import com.game.system.achievement.observer.FsJoinUnionOB;
import com.game.system.achievement.observer.FsTradeOB;
import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author andy
 * @create 2020/7/13 10:12
 */
public class FsTradeSB {
    private static FsTradeOB fsTradeOB;

    public static void registerObserver(FsTradeOB observer){
        fsTradeOB = observer;
    }

    public static void notifyObservers(String target, Role role) {
        fsTradeOB.checkAchievement(target,role);
    }
}
