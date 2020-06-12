package com.game.netty.server;

import com.game.common.ReflectService;
import com.game.controller.FunctionService;
import com.game.entity.Role;
import com.game.service.assis.Listen;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * netty服务端业务逻辑
 * 参考来源：csdn上作者maoyuanming0806的文章，参考了如何用netty实现客户端与服务端的通信，属于通用类代码
 * @author maoyuanming0806 and andy
 * @create 2020/5/12 22:32
 */

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    /*
    定义一个Channel 组，管理所有的channel
     */
    //GlobalEventExecutor.INSTANCE是一个全局事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    Logger logger = LoggerFactory.getLogger(NettyServer.class);
    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        if(msg.startsWith("0") || msg.startsWith("1") || msg.startsWith("2")){
            PrivateChatDeal.dealMessage(msg,ctx);
        }else if(msg.startsWith("all")){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Channel channel = ctx.channel();//此channel为发送者的channel，需要找到接收者的channel，然后接收者ch.writeAndFlush即可

            //这时我们要遍历ChannelGroup，根据不同情况，会送不同消息
            channelGroup.forEach(ch->{
                if (channel != ch){
                    ch.writeAndFlush("[客户]"+channel.remoteAddress()+" "+sdf.format(new Date())+":"+msg);
                }else {//回显自己发送的消息
                    ch.writeAndFlush("[自己]自己发送的消息： "+sdf.format(new Date())+":"+msg);
                }
            });
        }else{
            System.out.println("收到来自客户端:"+msg.toString());
            FunctionService.strings = msg.toString().split(" ");
            ReflectService reflectService = new ReflectService();
            //System.out.println(FunctionService.strings[0]);
            //ctx.channel().writeAndFlush(reflectService.getMethod(FunctionService.strings[0]));
            //ctx.fireChannelRead(msg);

            Channel channel = ctx.channel();
            channelGroup.forEach(ch -> {
                if(Listen.isDead()){
                    ch.writeAndFlush(Listen.mesg());
                }
                if(channel == ch){
                    ch.writeAndFlush(reflectService.getMethod(FunctionService.strings[0]));
                }
            });
            Listen.reset();
        }
    }

    //表示连接建立，第一个被执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();

        //将该客户加入聊天的信息推送给其他在线的客户端
        channelGroup.writeAndFlush("客户端"+channel.remoteAddress()+"加入聊天\n");//全局通知，每次远程客户端地址不同

        //将当前channel加入到ChannelGroup
        channelGroup.add(channel);
    }


    //表示断开连接
    //该方法执行，会导致   channelGroup.remove(channel);   所以不用写此句代码
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        //将该客户加入聊天的信息推送给其他在线的客户端
        channelGroup.writeAndFlush("客户端"+channel.remoteAddress()+"离线了\n");
    }

    //表示channel处于活动状态  提示XX上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        PrivateChatDeal.writeMessage("ni hao a",ctx);
        System.out.println(ctx.channel().remoteAddress()+"上线了");
    }

    //表示channel处于非活动状态   提示XX下线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"离线了");
    }

/*    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 出现异常时关闭连接。
        cause.printStackTrace();
        ctx.close();
    }*/

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }
}

