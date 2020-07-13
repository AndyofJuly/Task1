package com.game.system.achievement.subject;

import com.game.system.achievement.observer.FriendOB;
import com.game.system.achievement.observer.TalkNpcOB;
import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author andy
 * @create 2020/7/13 10:10
 */
public class FriendSB {
    private static FriendOB friendOB;

    public static void registerObserver(FriendOB observer){
        friendOB = observer;
    }

    public static void notifyObservers(int targetId, Role role) {
        friendOB.checkAchievement(targetId,role);
    }
}
