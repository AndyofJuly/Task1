package com.game.netty.newclient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * netty客户端业务逻辑
 * 参考来源：csdn上作者maoyuanming0806的文章，参考了如何用netty实现客户端与服务端的通信，属于通用类代码
 * @author maoyuanming0806 and andy
 * @create 2020/06/02 9:32
 */

public class NewClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        //此处的msg.toString来自ServerHandler的flush刷新后的通道信息
        System.out.println(msg.toString());

        //新增，测试
        //ctx.fireChannelRead(msg);
    }
}