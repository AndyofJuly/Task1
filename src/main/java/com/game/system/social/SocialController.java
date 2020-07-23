package com.game.system.social;

import com.game.common.Const;
import com.game.common.MyAnnontation;
import com.game.common.ResponseInf;
import com.game.netty.server.ServerHandler;
import com.game.system.gameserver.GameController;
import com.game.system.gameserver.GlobalInfo;
import com.game.system.role.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

/**
 * 社交模块调用方法入口
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Controller("socialController")
public class SocialController {

    private ArrayList<String> strList = GameController.getStrList();
    private ArrayList<Integer> intList = GameController.getIntList();

    @Autowired
    private SocialService socialService;

    /** 添加好友申请，使用方式：askFriend friendId */
    @MyAnnontation(MethodName = "askFriend")
    public ResponseInf askFriend(){
        socialService.askFriend(intList.get(0),getRole());
        return ResponseInf.setResponse("申请已提交",getRole());
    }

    /** 同意添加好友，使用方式：addFriend friendId */
    @MyAnnontation(MethodName = "addFriend")
    public ResponseInf addFriend(){
        socialService.addFriend(intList.get(0),getRole());
        return ResponseInf.setResponse("已添加好友",getRole());
    }

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
        boolean result = SocialService.emailToPlayer(intList.get(0),intList.get(1),intList.get(2),intList.get(3),getRole());
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
