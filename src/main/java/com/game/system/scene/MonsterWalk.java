package com.game.system.scene;

import com.game.system.assist.AssistService;
import com.game.system.assist.GlobalInfo;
import com.game.system.scene.pojo.Monster;
import com.game.system.role.pojo.Role;
import com.game.system.scene.pojo.MonsterResource;

/**
 * 怪物自由走动
 * @Author andy
 * @create 2020/7/8 10:45
 */
public class MonsterWalk implements Runnable{

    private Monster monster;

    public MonsterWalk(Monster monster) {
        this.monster = monster;
    }

    //怪物定时移动，漫步
    @Override
    public void run() {
        while(true) {
            //检测怪物存活状态，怪物死时break
            if (monster.getAlive() == 0) {
                break;
            }
            checkAtkDistance();
            //每次停2000毫秒
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //随机移动一小段距离，怪物位置x和y都加随机整数1-3；
            int add = (int) (Math.round(Math.random() * 2 + 1));
            int minus = -(int) (Math.round(Math.random() * 2 + 1));
            int[] arr = {add,minus};
            int index = (int) (Math.round(Math.random() * 1));
            int[] newPositon = {arr[index]+monster.getPosition()[0],arr[index]+monster.getPosition()[1]};
            monster.setPosition(newPositon);
            System.out.println(monster.getMonsterId() + "位置为" + monster.getPosition()[0]+","+monster.getPosition()[1]);
        }
    }

    private void checkAtkDistance(){
        //先看有没有角色，有则计算距离，如果距离近，则调进行循环的普通攻击（线程中再进行距离判断，如果距离远了，结束线程）
        if(monster.getAtkTargetId()!=0) {
            Role role = GlobalInfo.getRoleHashMap().get(monster.getAtkTargetId());
            boolean result = AssistService.checkDistance(role, monster);
            if (result) {
                //循环攻击角色
                while (monster.getAlive() != 0 && AssistService.checkDistance(role, monster) && role.getHp()>0) {
                    //不断地攻击角色
                    role.setHp(role.getHp() - monster.getAtk());
                    System.out.println(MonsterResource.getMonstersStatics().get(monster.getMonsterId()).getName()+
                            "攻击了角色，角色血量剩余：" + role.getHp());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
