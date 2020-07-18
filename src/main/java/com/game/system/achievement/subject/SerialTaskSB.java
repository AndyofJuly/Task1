package com.game.system.achievement.subject;

import com.game.system.achievement.observer.SerialTaskOb;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:13
 */
public class SerialTaskSB {
    private static SerialTaskOb serialTaskOB = new SerialTaskOb();

    public static void notifyObservers(int targetId, Role role) {
        serialTaskOB.checkAchievement(targetId,role);
    }
}
