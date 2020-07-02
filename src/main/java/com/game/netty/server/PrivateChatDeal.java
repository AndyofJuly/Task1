package com.game.netty.server;

import com.game.entity.Role;
import com.game.entity.vo.DealVo;
import com.game.service.ChatService;
import com.game.service.RoleService;
import com.game.service.assis.GlobalResource;
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
        String roleName = GlobalResource.getRoleHashMap().get(roleId).getName();
        //掐头去尾处理后的消息
        switch (strings[0]) {
            case "sayTo":
                //指定用户发送
                ChannelHandlerContext ctxTwo = getContext(Integer.parseInt(strings[1]));
                if (ctxTwo != null){
                    writeMessage(roleId+":"+strings[2],ctxTwo);
                    writeMessage("我："+strings[2],ctx);
                    break;
                }
                else {
                    writeMessage("is get out\n", ctx);
                    break;
                }
            case "email":
                //指定用户发送邮件，物品在信息中获取，举例：email kk 给你寄点东西 清泉酒
                ChannelHandlerContext ctxThree = getContext(Integer.parseInt(strings[1]));
                if (ctxThree != null){
                    writeMessage(roleId+":"+strings[2]+"。邮寄物品："+strings[3]+"数量为："+strings[4],ctxThree);
                    //调用email方法
                    RoleService roleService = new RoleService();
                    ChatService chatService = new ChatService();
                    String selfMsg = chatService.emailToPlayer(Integer.parseInt(strings[1]),strings[2],strings[3],Integer.parseInt(strings[4]),Integer.parseInt(strings[5]));
                    writeMessage(selfMsg,ctx);
                    break;
                }else {
                    writeMessage("is get out\n", ctx);
                    break;
                }
            case "deal":
                ChannelHandlerContext ctxFour = getContext(Integer.parseInt(strings[1]));
                if (ctxFour != null){
                    String dealId = UUID.randomUUID().toString();
                    Role role = GlobalResource.getRoleHashMap().get(Integer.parseInt(strings[1]));
                    role.setDealVo(new DealVo(dealId,Integer.parseInt(strings[1]),Integer.parseInt(strings[2]),Integer.parseInt(strings[3]),Integer.parseInt(strings[4])));
                    writeMessage(roleName+"向你发起了交易:"+role.getDealVo().getGoodsId()+"，价格为："+role.getDealVo().getPrice()+"交易密码："+dealId+"，是否同意？",ctxFour);
                    writeMessage("我："+strings[2],ctx);
                    break;
                }
                else {
                    writeMessage("is get out\n", ctx);
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
    static void writeMessage(String message, ChannelHandlerContext ctx) {
        //Unpooled.buffer(message.getBytes().length).writeBytes(message.getBytes())
        ctx.writeAndFlush(message);
    }
}
