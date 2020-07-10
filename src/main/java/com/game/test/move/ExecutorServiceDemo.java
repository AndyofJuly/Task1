package com.game.test.move;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MyRunnable implements Runnable {
    private String name;
    private int length=0;
    public MyRunnable(){}
    public MyRunnable(String name){
        this.name=name;
    }


    @Override
    public void run() {
/*        for (int x = 0; x < 50; x++) {
            System.out.println(Thread.currentThread().getName() + ":" + x);
       }*/
        while(true) {
            //检测怪物存活状态，怪物死时break
            if (length >= 50) {
                System.out.println(name + ",已经到达终点!");
                //结束赛跑,break
                break;
            }
            //检测怪物锁定的角色是否有记录，有则阻塞移动，直到怪物与玩家距离超过某个值时，恢复自由移动；另外开一个线程检测距离？并唤醒该线程
            //if(monster.getAtkTargetId!=0){wait}
            //每次停500毫秒
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //随机移动一小段距离，怪物位置x和y都加随机整数1-3；
            int x = (int) (Math.round(Math.random() * 3 + 1));
            int y = (int) (Math.round(Math.random() * 3 + 1));
            length += x + y;
            System.out.println(name + "已跑了" + length + "米.");//打印怪物位置
        }
    }
}

public class ExecutorServiceDemo {
    public static void main(String[] args) {
        // 创建一个线程池对象，控制要创建几个线程对象。
        // public static ExecutorService newFixedThreadPool(int nThreads)
        ExecutorService pool = Executors.newCachedThreadPool();

        // 可以执行Runnable对象或者Callable对象代表的线程
        pool.submit(new MyRunnable("乌龟"));
        pool.submit(new MyRunnable("兔子"));
        pool.submit(new MyRunnable("小鸟"));
        pool.submit(new MyRunnable("蚂蚁"));
        //结束线程池
        pool.shutdown();
    }
}