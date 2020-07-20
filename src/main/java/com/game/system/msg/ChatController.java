package com.game.system.msg;

import com.game.common.Const;
import com.game.common.MyAnnontation;
import com.game.common.ResponseInf;
import com.game.netty.server.ServerHandler;
import com.game.system.assist.GlobalInfo;
import com.game.system.role.RoleController;
import com.game.system.role.pojo.Role;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

/**
 * 通信模块调用方法入口
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Controller("chatController")
public class ChatController {

    private ArrayList<String> strList = RoleController.getStrList();
    private ArrayList<Integer> intList = RoleController.getIntList();

    /** 在公共世界进行聊天，使用方式：say msg */
    @MyAnnontation(MethodName = "say")
    public ResponseInf talkWithAll(){
        ArrayList<Role> roles = new ArrayList<>();
        for(Integer key : GlobalInfo.getRoleHashMap().keySet()){
            roles.add(GlobalInfo.getRoleHashMap().get(key));
        }
        ServerHandler.notifyGroupRoles(roles,getRole().getName()+":"+strList.get(1));
        return ResponseInf.setResponse("消息已发送到世界",getRole());
    }

    /** 私聊，使用方式：sayTo targetId msg */
    @MyAnnontation(MethodName = "sayTo")
    public ResponseInf talkWithOne(){
        ServerHandler.notifyRole(intList.get(0),getRole().getName()+":"+strList.get(1),getRole().getId(),"");
        return ResponseInf.setResponse("消息已发送给对方",getRole());
    }

    /** 邮件，使用方式：email targetRoleId msg goodsId num money */
    @MyAnnontation(MethodName = "email")
    public ResponseInf emailToOne(){
        boolean result = ChatService.emailToPlayer(intList.get(0),intList.get(1),intList.get(2),intList.get(3),getRole());
        if(result){
            String msg = getRole().getName()+":"+strList.get(1)+"。附件道具："+intList.get(1)+"，数量为："+intList.get(2)+"，赠送金钱："+intList.get(3);

            ServerHandler.notifyRole(intList.get(0),"邮件提示--"+msg,getRole().getId(),"");
            return ResponseInf.setResponse(Const.SEND_SUCCESS,getRole());
        }
        return ResponseInf.setResponse(Const.SEND_FAILURE,getRole());
    }

    /** 根据输入获得角色，输入参数最后一位为roleId */
    public Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
