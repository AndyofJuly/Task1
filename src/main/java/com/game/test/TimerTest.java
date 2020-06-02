package com.game.test;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {
    public static void main(String[] args) {
        run();
        for(int i=0;i<100;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("hello");
        }

    }



    private static void run() {
        Timer timer = new Timer();
        NewTimerTask timerTask = new NewTimerTask();
        //程序运行后立刻执行任务，每隔2000ms执行一次
        timer.schedule(timerTask, 0, 2000);
    }
}
