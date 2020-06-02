package com.game.service;

import com.game.common.Const;
import com.game.common.ExcelToJson;

/**
 * @Author andy
 * @create 2020/6/2 12:10
 */
public class Listen {
    public static boolean monsterIsDead;

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
