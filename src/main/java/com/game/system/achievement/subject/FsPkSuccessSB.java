package com.game.system.achievement.subject;

import com.game.system.achievement.observer.FsJoinUnionOB;
import com.game.system.achievement.observer.FsPkSuccessOB;
import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author andy
 * @create 2020/7/13 10:12
 */
public class FsPkSuccessSB {
    private static FsPkSuccessOB fsPkSuccessOB;

    public static void registerObserver(FsPkSuccessOB observer){
        fsPkSuccessOB = observer;
    }

    public static void notifyObservers(String target, Role role) {
        fsPkSuccessOB.checkAchievement(target,role);
    }
}
