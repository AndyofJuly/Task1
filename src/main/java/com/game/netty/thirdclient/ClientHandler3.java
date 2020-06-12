package com.game.netty.thirdclient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
/**
 * netty客户端3业务逻辑
 * @author maoyuanming0806 and andy
 * @create 2020/6/10 17:32
 */
public class ClientHandler3 extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s.trim());
    }
}

