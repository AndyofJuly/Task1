package com.game.system.achievement.subject;

import com.game.system.achievement.observer.TalkNpcOB;
import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author andy
 * @create 2020/7/13 10:05
 */
public class TalkNpcSB {
    private static TalkNpcOB talkNpcOB;

    public static void registerObserver(TalkNpcOB observer){
        talkNpcOB = observer;
    }

    public static void notifyObservers(int targetId, Role role) {
        talkNpcOB.checkAchievement(targetId,role);
    }
}
