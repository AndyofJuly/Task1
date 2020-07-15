package com.game.system.msg;

import com.game.common.Const;
import com.game.common.MyAnnontation;
import com.game.common.ResponseInf;
import com.game.netty.server.ServerHandler;
import com.game.system.assist.GlobalInfo;
import com.game.system.role.RoleController;
import com.game.system.role.pojo.Role;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

/**
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Controller
public class ChatController {

    private ArrayList<String> strList = RoleController.getStrList();
    private ArrayList<Integer> intList = RoleController.getIntList();


    // 全服聊天：在netty服务端进行处理：say words...
    // 私人聊天：在netty服务端进行处理：sayTo roleName words...
    // 邮件：在netty服务端进行处理：
    //发送邮件，使用举例：email ss(roleId) words... goods number；
/*    @MyAnnontation(MethodName = "email")
    public String emailToPlayer(){
        return ChatService.emailToPlayer(Integer.parseInt(strings[1]),strings[2],strings[3],Integer.parseInt(strings[4]),Integer.parseInt(strings[5]));
    }*/

    // say msg
    @MyAnnontation(MethodName = "say")
    public ResponseInf talkWithAll(){
        ServerHandler.talkWithAll(strList.get(1),getRole());
        return ResponseInf.setResponse("消息已发送到世界",getRole());
    }

    // sayTo targetId,msg,role
    @MyAnnontation(MethodName = "sayTo")
    public ResponseInf talkWithOne(){
        ServerHandler.talkWithOne(intList.get(0),strList.get(1),getRole());
        return ResponseInf.setResponse("消息已发送给对方",getRole());
    }

    //email 16 给你寄点东西 2001 2 20
    //roleId+":"+strings[2]+"。邮寄物品："+strings[3]+"数量为："+strings[4]+"金钱为："+strings[5]
    @MyAnnontation(MethodName = "email")
    public ResponseInf emailToOne(){
        boolean result = ChatService.emailToPlayer(intList.get(0),strList.get(1),intList.get(1),intList.get(2),intList.get(3),getRole());
        if(result){
            String msg = getRole().getName()+":"+strList.get(1)+"。邮寄物品："+intList.get(1)+"数量为："+intList.get(2)+"金钱为："+intList.get(3);
            ServerHandler.talkWithOne(intList.get(0),msg,getRole());
            return ResponseInf.setResponse(Const.SEND_SUCCESS,getRole());
        }
        return ResponseInf.setResponse(Const.SEND_FAILURE,getRole());
    }

    //获得角色，适用于输入参数最后一位为roleId的情况
    public Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
