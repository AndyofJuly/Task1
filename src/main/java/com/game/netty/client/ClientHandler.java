package com.game.netty.client;

import com.game.common.protobuf.DataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * netty客户端业务逻辑
 * 参考来源：csdn上作者maoyuanming0806的文章，参考了如何用netty实现客户端与服务端的通信，属于通用类代码
 * @author maoyuanming0806 and andy
 * @create 2020/5/12 22:32
 */

public class ClientHandler extends SimpleChannelInboundHandler<DataInfo.ResponseMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataInfo.ResponseMsg s) throws Exception {
        String msg = s.getMsg();
        System.out.println(msg.trim());
    }
}

