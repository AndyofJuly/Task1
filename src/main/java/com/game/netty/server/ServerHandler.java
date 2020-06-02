package com.game.netty.server;

import com.game.common.ReflectService;
import com.game.controller.FunctionService;
import com.game.service.Listen;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * netty服务端业务逻辑
 * 参考来源：csdn上作者maoyuanming0806的文章，参考了如何用netty实现客户端与服务端的通信，属于通用类代码
 * @author maoyuanming0806 and andy
 * @create 2020/5/12 22:32
 */

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        Logger logger = LoggerFactory.getLogger(NettyServer.class);
        System.out.println("收到来自客户端:"+msg.toString());

        FunctionService.strings = msg.toString().split(" ");
        ReflectService reflectService = new ReflectService();
        ctx.channel().writeAndFlush(reflectService.getMethod(FunctionService.strings[0]));
        ctx.fireChannelRead(msg);

        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if(Listen.isDead()){
                ch.writeAndFlush(Listen.mesg());
            }
            if(channel !=ch){
                ch.writeAndFlush(msg);
            }/*else{
                ch.writeAndFlush(" 【自己】"+msg +" ");
            }*/
        });
        Listen.reset();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(" 【服务器】 -" +channel.remoteAddress() +" 加入");
        channelGroup.add(channel);
    }
}