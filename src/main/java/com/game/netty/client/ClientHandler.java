package com.game.netty.client;

import com.game.common.protobuf.DataInfo;
import com.game.entity.Role;
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
        System.out.println("提示："+msg.trim());

        if(s.getRoleMsg()!=null && s.getRoleMsg().getId()!=0){
            DataInfo.ResponseMsg.RoleMsg role = s.getRoleMsg();
            System.out.println("[id:"+role.getId()+
                    ",name:"+role.getMsgName()+
                    ",hp:"+role.getHp()+
                    ",mp:"+role.getMp()+
                    ",atk:"+role.getAtk()+
                    ",money:"+role.getMoney()+
                    ",nowScenesId:"+role.getPlace()+
                    ",careerId:"+role.getCareerId()+
                    ",unionId:"+role.getUnionId()+
                    ",level:"+role.getLevel()+
                    "]");
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 出现异常时关闭连接。
        cause.printStackTrace();
        //ctx.close();
    }
}

