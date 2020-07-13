package com.game.system.achievement.subject;

import com.game.system.achievement.observer.FsJoinUnionOB;
import com.game.system.achievement.observer.TalkNpcOB;
import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author andy
 * @create 2020/7/13 10:11
 */
public class FsJoinUnionSB {
    private static FsJoinUnionOB fsJoinUnionOB;

    public static void registerObserver(FsJoinUnionOB observer){
        fsJoinUnionOB = observer;
    }

    public static void notifyObservers(String target, Role role) {
        fsJoinUnionOB.checkAchievement(target,role);
    }
}
