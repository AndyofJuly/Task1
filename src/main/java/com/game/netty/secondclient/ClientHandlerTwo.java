package com.game.netty.secondclient;

import com.game.common.protobuf.DataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
/**
 * netty客户端2业务逻辑
 * @author maoyuanming0806 and andy
 * @create 2020/6/10 17:32
 */
public class ClientHandlerTwo extends SimpleChannelInboundHandler<DataInfo.ResponseMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataInfo.ResponseMsg s) throws Exception {
        String msg = s.getMsg();
        System.out.println(msg.trim());
    }
}

