package com.game.test.selfevent;

import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/11 20:03
 */
public class Notify {



    public static void notifyObserver(MyEvent event){
        //注入事件
        //= new MyEvent();
        event.setDesc("talkNpc");
        event.setId(102);
        event.setTargetId(20001);
        event.setCount(1);
        TalkListener talkListener = new TalkListener();//如果观察者多了，就用集合表示
        talkListener.checkAchievement(event);
    }
}
