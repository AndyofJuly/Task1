package com.game.test.event;

public class MyRobotListener implements  RobotListener{
    @Override
    public void working(Even even) {
        Robot robot = even.getRobot();
        System.out.println("这里先检查是否击杀怪物");
    }

    @Override
    public void dancing(Even even) {
        Robot robot = even.getRobot();
        System.out.println("这里检查是否与NPC对话");
    }
}