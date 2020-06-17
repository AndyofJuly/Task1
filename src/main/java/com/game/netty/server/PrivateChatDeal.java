package com.game.netty.server;

import com.game.controller.RoleController;
import com.game.dao.ConnectSql;
import com.game.service.ChatService;
import com.game.service.RoleService;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

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
        System.out.println(strings.length+" length");
        System.out.println(message);
        Integer roleId = Integer.valueOf(strings[strings.length-1]);
        String roleName = RoleController.roleHashMap.get(roleId).getName();
        System.out.println(Integer.valueOf(strings[strings.length-1]));
        //掐头去尾处理后的消息
        switch (strings[0]) {
            case "sayTo":
                //指定用户发送
                ChannelHandlerContext ctxTwo = getContext(ConnectSql.sql.selectRoleIdByName(strings[1]));
                if (ctxTwo != null){
                    writeMessage(roleName+":"+strings[2],ctxTwo);
                    writeMessage("我："+strings[2],ctx);
                    break;
                }
                else {
                    writeMessage("is get out\n", ctx);
                    break;
                }
            case "email":
                //指定用户发送邮件，物品在信息中获取，举例：email kk 给你寄点东西 清泉酒
                ChannelHandlerContext ctxThree = getContext(ConnectSql.sql.selectRoleIdByName(strings[1]));
                if (ctxThree != null){
                    writeMessage(roleName+":"+strings[2]+"。邮寄物品："+strings[3]+"数量为："+strings[4],ctxThree);
                    //调用email方法
                    RoleService roleService = new RoleService();
                    String selfMsg = ChatService.emailToPlayer(ConnectSql.sql.selectRoleIdByName(strings[1]),strings[2],strings[3],Integer.parseInt(strings[4]),Integer.parseInt(strings[5]));
                    writeMessage(selfMsg,ctx);
                    break;
                }else {
                    writeMessage("is get out\n", ctx);
                    break;
                }
            default:
                System.out.println("over");
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
