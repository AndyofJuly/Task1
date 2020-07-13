package com.game.netty.server;

import com.game.common.protobuf.DataInfo;
import com.game.system.role.pojo.Role;
import com.game.system.shop.pojo.DealBo;
import com.game.system.chat.IChatService;
import com.game.common.ResponseInf;
import com.game.system.chat.ChatServiceImpl;
import com.game.system.assist.GlobalInfo;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//参考：https://blog.csdn.net/qq_36480491/article/details/84711553服务端私聊可通过自定义协议区分
public class PrivateChatDeal {

    private static Map<Integer, ChannelHandlerContext> onlineUsers = new HashMap<Integer, ChannelHandlerContext>();//存储用户客户端消息

    public static void add(Integer uid, ChannelHandlerContext ctx) {
        onlineUsers.put(uid, ctx);
    }

    public static void remove(Integer uid) {
        onlineUsers.remove(uid);
    }

    private static ChannelHandlerContext getContext(Integer uid) {
        return onlineUsers.get(uid);
    }

    /**
     * 消息处理、自定义协议
     * @param message 消息
     * @param ctx 客户端
     */
    static void dealMessage(String message, ChannelHandlerContext ctx) {
        String[] strings = message.split(" ");
        Integer roleId = Integer.valueOf(strings[strings.length-1]);
        String roleName = GlobalInfo.getRoleHashMap().get(roleId).getName();
        //掐头去尾处理后的消息
        switch (strings[0]) {
            case "sayTo":
                //指定用户发送
                ChannelHandlerContext ctxTwo = getContext(Integer.parseInt(strings[1]));
                if (ctxTwo != null){
                    writeMessage(new ResponseInf(roleId+":"+strings[2]),ctxTwo);
                    writeMessage(new ResponseInf("我："+strings[2]),ctx);
                    break;
                }
                else {
                    writeMessage(new ResponseInf("is get out\n"), ctx);
                    break;
                }
            case "email":
                //指定用户发送邮件，物品在信息中获取，举例：email kk 给你寄点东西 清泉酒
                ChannelHandlerContext ctxThree = getContext(Integer.parseInt(strings[1]));
                if (ctxThree != null){
                    writeMessage(new ResponseInf(roleId+":"+strings[2]+"。邮寄物品："+strings[3]+"数量为："+strings[4]),ctxThree);
                    //调用email方法
                    IChatService iChatService = new ChatServiceImpl();
                    String selfMsg = iChatService.emailToPlayer(Integer.parseInt(strings[1]),strings[2],strings[3],Integer.parseInt(strings[4]),Integer.parseInt(strings[5]));
                    writeMessage(new ResponseInf(selfMsg),ctx);
                    break;
                }else {
                    writeMessage(new ResponseInf("is get out\n"), ctx);
                    break;
                }
            case "deal":
                ChannelHandlerContext ctxFour = getContext(Integer.parseInt(strings[1]));
                if (ctxFour != null){
                    String dealId = UUID.randomUUID().toString();
                    Role role = GlobalInfo.getRoleHashMap().get(Integer.parseInt(strings[5]));
                    //0:dealId 1:int receiveRoleId, 2:int equipmentId, 3:int potionId, 4:int price, 5:int sendRoleId
                    role.setDealBo(new DealBo(dealId,Integer.parseInt(strings[1]),Integer.parseInt(strings[2]),Integer.parseInt(strings[3]),Integer.parseInt(strings[4]),Integer.parseInt(strings[5])));
                    String string = roleName+"向你发起了交易，装备:"+role.getDealBo().getEquipmentId()+"" +
                            "；药品："+role.getDealBo().getPotionId()+"，补价："+role.getDealBo().getPrice();
                    writeMessage(new ResponseInf(string),ctxFour);
                    writeMessage(new ResponseInf("已向对方发起交易"),ctx);
                    break;
                }
                else {
                    writeMessage(new ResponseInf("is get out\n"), ctx);
                    break;
                }
            default:
                return;
        }
    }

    /**
     * 发送消息
     * @param message 消息
     * @param ctx 客户端
     */
    static void writeMessage(ResponseInf message, ChannelHandlerContext ctx) {
/*        DataInfo.ResponseMsg responseMsg = DataInfo.ResponseMsg.newBuilder().setMsg(message).build();
        ctx.writeAndFlush(responseMsg);*/
        DataInfo.ResponseMsg.Builder builders = DataInfo.ResponseMsg.newBuilder();
        builders.setMsg(message.getMsg());
        if(message.getRole() != null){
            DataInfo.ResponseMsg.RoleMsg.Builder builder = DataInfo.ResponseMsg.RoleMsg.newBuilder();
            Role role = message.getRole();
            builder.setId(role.getId());
            builder.setMsgName(role.getName());
            builder.setMoney(role.getMoney());
            builder.setAtk(role.getAtk());
            builder.setHp(role.getHp());
            builder.setMp(role.getMp());
            builder.setPlace(role.getNowScenesId());
            builder.setLevel(role.getLevel());
            builder.setCareerId(role.getCareerId());
            builder.setUnionId(role.getUnionId());
            builders.setRoleMsg(builder);
        }
        ctx.writeAndFlush(builders.build());
    }
}
