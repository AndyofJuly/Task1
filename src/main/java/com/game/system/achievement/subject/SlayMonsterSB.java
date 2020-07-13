package com.game.system.achievement.subject;

import com.game.common.Const;
import com.game.system.achievement.observer.SlayMonsterOB;
import com.game.system.achievement.observer.TalkNpcOB;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author andy
 * @create 2020/7/13 10:03
 */
public class SlayMonsterSB {
    private static SlayMonsterOB slayMonsterOB;

    public static void registerObserver(SlayMonsterOB observer){
        slayMonsterOB = observer;
    }

    public static void notifyObservers(int targetId, Role role) {
        slayMonsterOB.checkAchievement(targetId,role);
    }
}
