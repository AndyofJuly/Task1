package com.game.test.selfevent;

import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/11 18:41
 */
public class MySource {

    //事件源-发起事件所在的方法-talkToNpc-Subject

    public void talkToNpc(){
/*        System.out.println("hello-man");
        MyEvent event = new MyEvent();
        Notify.notifyObserver(event);*/
    }

    public static void main(String[] args) {
        MySource mySource = new MySource();
        mySource.talkToNpc();
    }

}
