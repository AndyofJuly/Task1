package com.game.system.achievement.subject;

import com.game.system.achievement.observer.Observer;
import com.game.system.role.pojo.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author andy
 * @create 2020/7/16 12:31
 */
public class Subject {

/*    public static void notifyObservers(int targetId, Role role, Observer observer) {
        observer.checkAchievement(targetId,role);
    }

    public static void notifyObservers(String target, Role role, Observer observer) {
        observer.checkAchievement(target,role);
    }*/

    private List<Observer> observers;

    public Subject() {
        this.observers = new ArrayList<Observer>();
    }

    //增加一个观察者
    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    //通知所有观察者
    public void notifyObserver(int target,Role role) {
        for(Observer observer : this.observers) {
            observer.checkAchievement(target,role);//通知所有观察者执行自己的任务；目前只有查看成就这一个观察者，后续还可能有例如检查任务的观察者
        }
    }
}
