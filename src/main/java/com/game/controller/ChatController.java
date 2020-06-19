package com.game.controller;

import com.game.service.ChatService;
import com.game.service.assis.GlobalResource;
import org.springframework.stereotype.Service;
import com.game.common.MyAnnontation;

import java.util.ArrayList;

/**
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Service
public class ChatController {

/*    private ArrayList<String> strList = GlobalResource.getStrList();
    private ArrayList<Integer> intList = GlobalResource.getIntList();*/


    // 全服聊天：在netty服务端进行处理：say words...
    // 私人聊天：在netty服务端进行处理：sayTo roleName words...
    // 邮件：在netty服务端进行处理：
    //发送邮件，使用举例：email ss(roleId) words... goods number；
/*    @MyAnnontation(MethodName = "email")
    public String emailToPlayer(){
        return ChatService.emailToPlayer(Integer.parseInt(strings[1]),strings[2],strings[3],Integer.parseInt(strings[4]),Integer.parseInt(strings[5]));
    }*/
}
