package com.game.test.move;

/**
 * 先写一个小demo，然后放到服务端代码中
 * @Author andy
 * @create 2020/7/8 9:54
 */
public class GuiTuSaiPao implements Runnable {

    private String name;
    private int length=0;
    public GuiTuSaiPao(){}
    public GuiTuSaiPao(String name){
        this.name=name;
    }

    @Override
    public void run(){
        while(true){
            //检测怪物存活状态，怪物死时break
            if(length>=100){
                System.out.println(name+",已经到达终点!");
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
            int x=(int)(Math.round(Math.random()*3+1));
            int y = (int)(Math.round(Math.random()*3+1));
            length+=10;
            System.out.println(name+"已跑了"+length+"米.");//打印怪物位置
        }
    }

    public static void main(String[] args) {
        GuiTuSaiPao wugui=new GuiTuSaiPao("乌龟");
        GuiTuSaiPao tuzi=new GuiTuSaiPao("兔子");
        Thread thread=new Thread(wugui);
        Thread thread2=new Thread(tuzi);
        //启动线程
        thread.start();
        thread2.start();
    }
}
