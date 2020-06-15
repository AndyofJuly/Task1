package com.game.netty.server;

import com.game.controller.FunctionService;
import com.game.dao.ConnectSql;
import com.game.service.RoleService;
import io.netty.buffer.Unpooled;
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
        //定义协议#（改为空格）号隔开数组下标  0：不同命令、  1：接收端用户ID、    2：内容
        //if (strings.length != 4) {return;}
        Integer roleId = Integer.valueOf(strings[strings.length-1]);
        //add(roleId, ctx);
        String roleName = FunctionService.roleHashMap.get(roleId).getName();
        System.out.println(Integer.valueOf(strings[strings.length-1]));
        //掐头去尾处理后消息
        switch (strings[0]) {
/*            case "0"://认证客户端
                add(Integer.valueOf(strings[2]), ctx);
                break;*/
            case "sayTo":
                //指定用户发送
                ChannelHandlerContext ctxTwo = getContext(ConnectSql.sql.selectRoleIdByName(strings[1]));
                if (ctxTwo != null){
                    writeMessage(roleName+":"+strings[2],ctxTwo);
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
                    writeMessage(roleName+":"+strings[2]+"。邮寄物品："+strings[3],ctxThree);
                    //调用email方法
                    RoleService roleService = new RoleService();
                    roleService.emailToPlayer(ConnectSql.sql.selectRoleIdByName(strings[1]),strings[2],strings[3],Integer.parseInt(strings[4]));
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
