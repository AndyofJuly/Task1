package com.game.service;

import com.game.controller.RoleController;
import com.game.service.assis.AssistService;

import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/6/15 14:56
 */
public class ChatService {
    //全服聊天、私人聊天在netty服务端进行处理

    //发送邮件
    public static String emailToPlayer (int TargetRoleId, String words, String goods, int num, int roleId){
        HashMap<Integer,Integer> goodsHashMap = RoleController.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap();
        int lastNum = goodsHashMap.get(AssistService.checkGoodsId(goods))-num;
        if(lastNum<0){return "数量不足，无法发送";}
        goodsHashMap.put(AssistService.checkGoodsId(goods),lastNum);
        HashMap<Integer,Integer> goodsHashMapRec = RoleController.roleHashMap.get(TargetRoleId).getMyPackage().getGoodsHashMap();
        goodsHashMapRec.put(AssistService.checkGoodsId(goods),goodsHashMapRec.get(AssistService.checkGoodsId(goods))+num);
        return "已发送邮件，包含"+goods+"，数量："+num;
    }
}
