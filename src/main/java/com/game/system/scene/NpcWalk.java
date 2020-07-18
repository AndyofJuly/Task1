package com.game.system.scene;

import com.game.system.scene.pojo.Npc;

/**
 * Npc随机移动
 * @Author andy
 * @create 2020/7/14 11:24
 */
public class NpcWalk implements Runnable{

    private Npc npc;

    public NpcWalk(Npc npc) {
        this.npc = npc;
    }

    @Override
    public void run() {
        while(true) {

            //每隔一段时间运动一次
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //随机移动一小段距离
            int add = (int) (Math.round(Math.random() * 5 + 1));
            int minus = -(int) (Math.round(Math.random() * 5 + 1));
            int[] arr = {add,minus};
            int index = (int) (Math.round(Math.random() * 1));
            int x =arr[index]+npc.getPosition()[0];
            int y = arr[index]+npc.getPosition()[1];

            //移动后更新格子
            //System.out.println(SceneService.refleshGrid(x,y,npc));

            int[] newPositon = {x,y};
            npc.setPosition(newPositon);
            System.out.println(npc.getNpcId() + "位置为" + npc.getPosition()[0]+","+npc.getPosition()[1]);
        }
    }
}
