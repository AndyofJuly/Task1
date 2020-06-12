package com.game.service.assis;

import com.game.common.Const;

/**
 * 一些监听事件
 * @Author andy
 * @create 2020/6/2 12:10
 */
public class Listen {
    public static boolean monsterIsDead;
    //监听怪物是否已被打败，通知全局
    public static boolean isDead(){
        if(Listen.monsterIsDead==true){
            return true;
        }
        return false;
    }

    public static String mesg(){
        return Const.MONSTER_MESSEAGE;
    }

    public static void reset(){
        monsterIsDead=false;
    }

}
