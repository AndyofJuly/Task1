package com.game.service;

import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.entity.MyPackage;
import com.game.entity.Role;
import com.game.service.assis.AssistService;
import com.game.service.assis.GlobalResource;

import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/6/15 14:56
 */
public class ChatService {
    //全服聊天、私人聊天在netty服务端进行处理

    //发送邮件 here
    public String emailToPlayer (int TargetRoleId, String words, String goods, int num, int roleId){
        MyPackage myPackage = GlobalResource.getRoleHashMap().get(roleId).getMyPackage();
        int lastNum = myPackage.getGoodsHashMap().get(AssistService.checkGoodsId(goods))-num;
        if(lastNum<0){return Const.SEND_FAILURE;}
        myPackage.getGoodsHashMap().put(AssistService.checkGoodsId(goods),lastNum);
        Role TargetRole = GlobalResource.getRoleHashMap().get(TargetRoleId);
        HashMap<Integer,Integer> goodsHashMapRec = TargetRole.getMyPackage().getGoodsHashMap();
        int number = goodsHashMapRec.get(AssistService.checkGoodsId(goods))+num;
        goodsHashMapRec.put(AssistService.checkGoodsId(goods),number);
        return Const.SEND_SUCCESS;
    }
}
