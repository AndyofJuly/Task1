package com.game.system.achievement.subject;

import com.game.system.achievement.observer.Observer;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/16 12:31
 */
public class Subject {

    public static void notifyObservers(int targetId, Role role, Observer observer) {
        observer.checkAchievement(targetId,role);
    }

    public static void notifyObservers(String target, Role role, Observer observer) {
        observer.checkAchievement(target,role);
    }

}
