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
/*        String msg = s.getMsg();
        System.out.println(msg.trim());*/
        //DataInfo.ResponseMsg msg = (DataInfo.ResponseMsg)s;
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

