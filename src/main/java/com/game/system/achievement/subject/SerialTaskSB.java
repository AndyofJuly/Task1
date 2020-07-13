package com.game.system.achievement.subject;

import com.game.system.achievement.observer.SerialTaskOB;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:13
 */
public class SerialTaskSB {
    private static SerialTaskOB serialTaskOB = new SerialTaskOB();

    public static void notifyObservers(int targetId, Role role) {
        serialTaskOB.checkAchievement(targetId,role);
    }
}
