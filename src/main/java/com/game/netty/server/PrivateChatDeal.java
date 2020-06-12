package com.game.netty.server;

import com.game.service.RoleService;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

//参考：https://blog.csdn.net/qq_36480491/article/details/84711553私聊部分
public class PrivateChatDeal {
    private static Map<Integer, ChannelHandlerContext> onlineUsers = new HashMap<Integer, ChannelHandlerContext>();//存储用户客户端消息

    private static void add(Integer uid, ChannelHandlerContext ctx) {
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
        String[] strings = message.split("#");
        System.out.println(strings.length+" length");
        System.out.println(message);
        //定义协议#号隔开数组下标0：数据类型、1：接收端用户ID、2:发送端ID、3内容
        //if (strings.length != 4) {return;}
        switch (strings[0]) {
            case "0"://认证客户端
                add(Integer.valueOf(strings[2]), ctx);
                break;

            case "1"://指定用户发送
                ChannelHandlerContext ctxTwo = getContext(Integer.valueOf(strings[1]));
                if (ctxTwo != null){
                    writeMessage(message,ctxTwo);
                    break;
                }
                else {
                    writeMessage("is get out\n", ctx);
                    break;
                }
            case "2"://指定用户发送邮件，物品在信息中获取，举例：#3#16#1#收邮件&清泉酒
                ChannelHandlerContext ctxThree = getContext(Integer.valueOf(strings[1]));
                if (ctxThree != null){
                    writeMessage(message,ctxThree);
                    if(message.contains("&")){
/*                        String[] strings1 = message.split("&");
                        String goods = strings1[1];*/
                        //用户发送的邮件中不能有#、&和空格符号
                        String goods = message.substring(message.indexOf("&")+1,message.indexOf(" "));
                        //调用email方法
                        RoleService roleService = new RoleService();
                        roleService.emailToPlayer(Integer.parseInt(strings[1]),strings[3],goods,Integer.parseInt(strings[2]));
                    }
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
    //连接服务器，收到服务器返回（ni hao a）的消息, 马上返回消息绑定客户端如0#1#2#3，及表示自己客户端的id为2，
    // 其他客户端可通过id来向自己发送消息，发送消息给其他客户端如2#1#2#4，给id为1的客户端发送消息4。


    /**
     * 发送消息
     * @param message 消息
     * @param ctx 客户端
     */
    static void writeMessage(String message, ChannelHandlerContext ctx) {
        ctx.writeAndFlush(message);//Unpooled.buffer(message.getBytes().length).writeBytes(message.getBytes())
    }
}
