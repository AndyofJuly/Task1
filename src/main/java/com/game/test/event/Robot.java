package com.game.test.event;


/**
 * 事件源：机器人
 */
public class Robot {

    private RobotListener listener;

    /**
     * 注册机器人监听器
     * @param listener
     */
    public void registerListener(RobotListener listener){
        this.listener  = listener;
    }

    /**
     * 工作
     */
    public void working(){
        if(listener!=null){
            Even even = new Even(this);
            this.listener.working(even);
        }
        System.out.println("打死了一只怪物");
    }

    /**
     * 跳舞
     */
    public void dancing(){
        if(listener!=null){
            Even even = new Even(this);
            this.listener.dancing(even);
        }
        System.out.println("与NPC对话");
    }


}