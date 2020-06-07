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

/*    //监听用户输入，记录用户id，在进行其他操作时带上此id，从而实现自动区分每个客户端的角色是哪个id
    public static String getRoleIdFromInput(String msg,String roleId){
        if(msg.startsWith("loginR")||msg.startsWith("registerR")){
            String[] s = msg.split(" ");
            roleId = " "+ ConnectSql.sql.selectRoleIdByName(s[1]);
        }
        return roleId;
    }*/
}
