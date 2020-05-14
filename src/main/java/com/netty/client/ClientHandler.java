package com.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * netty客户端业务逻辑
 * 引用地址：https://blog.csdn.net/maoyuanming0806/article/details/81097083?
 * utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.
 * nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.nonecase
 * @author maoyuanming0806 and andy
 * @create 2020/5/12 22:32
 */

public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        //此处的msg.toString来自ServerHandler的flush刷新后的通道信息
        System.out.println(msg.toString());
    }
}