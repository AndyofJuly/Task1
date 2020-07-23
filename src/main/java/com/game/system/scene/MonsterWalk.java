package com.game.system.scene;

import com.game.netty.server.ServerHandler;
import com.game.system.gameserver.AssistService;
import com.game.system.gameserver.GlobalInfo;
import com.game.system.role.RoleService;
import com.game.system.scene.entity.Monster;
import com.game.system.role.entity.Role;

/**
 * 怪物随机移动
 * @Author andy
 * @create 2020/7/8 10:45
 */
public class MonsterWalk implements Runnable{

    private Monster monster;

    public MonsterWalk(Monster monster) {
        this.monster = monster;
    }

    @Override
    public void run() {
        while(true) {
            //检测怪物存活状态
            if (monster.getAlive() == 0) {
                break;
            }
            checkAtkDistance();

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //随机移动一小段距离
            int add = (int) (Math.round(Math.random() * 2 + 1));
            int minus = -(int) (Math.round(Math.random() * 2 + 1));
            int[] arr = {add,minus};
            int index = (int) (Math.round(Math.random() * 1));
            int x = arr[index]+monster.getPosition()[0];
            int y = arr[index]+monster.getPosition()[1];

            //移动后更新格子

            //System.out.println(SceneService.refleshGrid(x,y,monster));//配合InitGame中的Sleep使用
            Integer[] newPositon = {x,y};
            monster.setPosition(newPositon);
            System.out.println(+monster.getMonsterId() + "位置为" + monster.getPosition()[0]+","+monster.getPosition()[1]);
        }
    }

    /** 查看攻击距离 */
    private void checkAtkDistance(){
        //先判断怪物有无仇恨目标，有则判断距离，在视野范围内再进行攻击
        if(monster.getAtkTargetId()==0) {return;}
        Role role = GlobalInfo.getRoleHashMap().get(monster.getAtkTargetId());
        boolean result = AssistService.isOutOfInteraction(role, monster);
        if (!result) {
            //循环攻击角色
            while (monster.getAlive() != 0 && !AssistService.isOutOfInteraction(role, monster) && role.getHp()>0 && role.getNowScenesId().equals(monster.getSceneId())) {
                RoleService.checkAndSetHp(role.getHp() - monster.getAtk(),role);
                ServerHandler.notifySelf(role.getId(),monster.getMonsterId()+
                        "使用了普通攻击，你血量剩余：" + role.getHp());
                System.out.println(monster.getMonsterId()+
                        "攻击了角色，角色血量剩余：" + role.getHp());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
