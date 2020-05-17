package com.game.netty.server;

import com.game.service.FunctionService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * netty服务端业务逻辑
 * 参考来源：csdn上作者maoyuanming0806的文章，参考了如何用netty实现客户端与服务端的通信，属于通用类代码
 * @author maoyuanming0806 and andy
 * @create 2020/5/12 22:32
 */

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        System.out.println("收到来自客户端的命令:"+msg.toString());

        FunctionService.strings = msg.toString().split(" ");
        FunctionService functionService = new FunctionService();
        ctx.channel().writeAndFlush(functionService.getMethod(FunctionService.strings[0]));
        ctx.fireChannelRead(msg);
    }

}