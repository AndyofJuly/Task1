package com.game.system.achievement.subject;

import com.game.system.achievement.observer.LevelOB;
import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author andy
 * @create 2020/7/13 10:03
 */
public class LevelSB {
    private static LevelOB levelOB;

    public static void registerObserver(LevelOB observer){
        levelOB = observer;
    }

    public static void notifyObservers(int targetId, Role role) {
        levelOB.checkAchievement(targetId,role);
    }
}
