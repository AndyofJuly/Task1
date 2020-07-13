package com.game.system.chat;

import com.game.common.Const;
import com.game.system.bag.pojo.MyPackageBo;
import com.game.system.role.pojo.Role;
import com.game.system.assist.AssistService;
import com.game.system.assist.GlobalInfo;

import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/6/15 14:56
 */
public class ChatServiceImpl implements IChatService {
    //全服聊天、私人聊天在netty服务端进行处理

    //发送邮件 here
    @Override
    public String emailToPlayer (int TargetRoleId, String words, String goods, int num, int roleId){
        MyPackageBo myPackageBo = GlobalInfo.getRoleHashMap().get(roleId).getMyPackageBo();
        int lastNum = myPackageBo.getGoodsHashMap().get(AssistService.checkGoodsId(goods))-num;
        if(lastNum<0){return Const.SEND_FAILURE;}
        myPackageBo.getGoodsHashMap().put(AssistService.checkGoodsId(goods),lastNum);
        Role TargetRole = GlobalInfo.getRoleHashMap().get(TargetRoleId);
        HashMap<Integer,Integer> goodsHashMapRec = TargetRole.getMyPackageBo().getGoodsHashMap();
        int number = goodsHashMapRec.get(AssistService.checkGoodsId(goods))+num;
        goodsHashMapRec.put(AssistService.checkGoodsId(goods),number);
        return Const.SEND_SUCCESS;
    }
}