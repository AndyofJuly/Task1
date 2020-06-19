package com.game.service;

import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.service.assis.AssistService;
import com.game.service.assis.GlobalResource;

import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/6/15 14:56
 */
public class ChatService {
    //全服聊天、私人聊天在netty服务端进行处理

    //发送邮件
    public static String emailToPlayer (int TargetRoleId, String words, String goods, int num, int roleId){
        HashMap<Integer,Integer> goodsHashMap = GlobalResource.getRoleHashMap().get(roleId).getMyPackage().getGoodsHashMap();
        int lastNum = goodsHashMap.get(AssistService.checkGoodsId(goods))-num;
        if(lastNum<0){return Const.SEND_FAILURE;}
        goodsHashMap.put(AssistService.checkGoodsId(goods),lastNum);
        HashMap<Integer,Integer> goodsHashMapRec = GlobalResource.getRoleHashMap().get(TargetRoleId).getMyPackage().getGoodsHashMap();
        goodsHashMapRec.put(AssistService.checkGoodsId(goods),goodsHashMapRec.get(AssistService.checkGoodsId(goods))+num);
        return Const.SEND_SUCCESS;
    }
}
