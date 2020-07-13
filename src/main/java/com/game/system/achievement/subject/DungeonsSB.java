package com.game.system.achievement.subject;

import com.game.common.Const;
import com.game.system.achievement.observer.DungeonsOB;
import com.game.system.achievement.observer.TalkNpcOB;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author andy
 * @create 2020/7/13 10:08
 */
public class DungeonsSB {
    private static DungeonsOB dungeonsOB;

    public static void registerObserver(DungeonsOB observer){
        dungeonsOB = observer;
    }

    public static void notifyObservers(int targetId, Role role) {
        dungeonsOB.checkAchievement(targetId,role);
    }
}
