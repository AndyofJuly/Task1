package com.game.system.achievement.subject;

import com.game.system.achievement.observer.FsJoinTeamOB;
import com.game.system.achievement.observer.FsJoinUnionOB;
import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author andy
 * @create 2020/7/13 10:10
 */
public class FsJoinTeamSB {
    private static FsJoinTeamOB fsJoinTeamOB;

    public static void registerObserver(FsJoinTeamOB observer){
        fsJoinTeamOB = observer;
    }

    public static void notifyObservers(String target, Role role) {
        fsJoinTeamOB.checkAchievement(target,role);
    }
}
