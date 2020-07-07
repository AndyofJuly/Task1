package com.game.netty.server;

import com.game.common.ReflectService;
import com.game.common.UtilHelper;
import com.game.common.protobuf.DataInfo;
import com.game.controller.RoleController;
import com.game.entity.Scene;
import com.game.service.assis.GlobalResource;
import com.game.service.assis.InitGame;
import com.game.service.assis.Listen;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * netty服务端业务逻辑
 * 参考来源：csdn上作者maoyuanming0806的文章，参考了如何用netty实现客户端与服务端的通信，属于通用类代码
 * @author maoyuanming0806 and andy
 * @create 2020/5/12 22:32
 */

public class ServerHandler extends SimpleChannelInboundHandler<DataInfo.RequestMsg> {
    /*
    定义一个Channel 组，管理所有的channel
     */
    //GlobalEventExecutor.INSTANCE是一个全局事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static HashMap<ChannelId, Integer> clientGroup = new HashMap<ChannelId, Integer>();
    Logger logger = LoggerFactory.getLogger(NettyServer.class);
    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.RequestMsg msgs) throws Exception {

        String msg = msgs.getMsg();
        String[] s = msg.split(" ");
        if(!msg.contains("registerR") && !msg.contains("login") && !msg.contains("register")){
            clientGroup.put(ctx.channel().id(),Integer.parseInt(s[s.length-1]));
            //认证客户端
            PrivateChatDeal.add(Integer.parseInt(s[s.length-1]),ctx);
        }

        if(msg.startsWith("sayTo") || msg.startsWith("email") || msg.startsWith("deal")){
            PrivateChatDeal.dealMessage(msg,ctx);
        }else if(msg.startsWith("say")){
            //此channel为发送者的channel，需要找到接收者的channel，然后接收者ch.writeAndFlush即可
            Channel channel = ctx.channel();
            //这时我们要遍历ChannelGroup，根据不同情况，会送不同消息
            channelGroup.forEach(ch->{
                if (channel != ch){ // 默认登录以后才能收到消息
                    int roleId = clientGroup.get(channel.id());
                    writeMessage(GlobalResource.getRoleHashMap().get(roleId).getName()+":"+msg.substring(3,msg.length()-2),ch);
                }else {//回显自己发送的消息
                    writeMessage("我:"+msg.substring(3,msg.length()-2),ch);
                }
            });
        }else{
            System.out.println("收到来自客户端:"+msg.toString());
            RoleController.setIntList(UtilHelper.getIntList(msg.toString().split(" ")));
            RoleController.setStrList(UtilHelper.getStrList(msg.toString().split(" ")));
            ReflectService reflectService = new ReflectService();
            Channel channel = ctx.channel();
            channelGroup.forEach(ch -> {
                if(Listen.isDead()){
                    // todo 有问题待修改
                    writeMessage(Listen.mesg(),ch);
                }
                if(channel == ch){
                    writeMessage(reflectService.getMethod(RoleController.getStrList().get(0)),ch);
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
        //channelGroup.writeAndFlush("客户端"+channel.remoteAddress()+"上线了\n");//全局通知，每次远程客户端地址不同
        channelGroup.forEach(ch -> {
            writeMessage("客户端"+channel.remoteAddress()+"上线了\n",ch);
        });
        //将当前channel加入到ChannelGroup
        channelGroup.add(channel);
    }

    //表示断开连接
    //该方法执行，会导致   channelGroup.remove(channel);   所以不用写此句代码
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户离开聊天的信息推送给其他在线的客户端
        //channelGroup.writeAndFlush("客户端"+channel.remoteAddress()+"离线了-handlerRemoved");
        channelGroup.forEach(ch -> {
            writeMessage("客户端"+channel.remoteAddress()+"离线了-handlerRemoved",ch);
        });
    }

    //表示channel处于活动状态  提示XX上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //PrivateChatDeal.writeMessage("ni hao a",ctx);
        System.out.println(ctx.channel().remoteAddress()+"上线了");
    }

    //表示channel处于非活动状态   提示XX下线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //角色集合中移除该客户端的角色、需要在当前场景中移除、还需要在队伍中移除
        //传参必须为Integer，否则List会将id看做下标而不是元素
        Integer removeId = clientGroup.get(ctx.channel().id());
        int sceneId = GlobalResource.getRoleHashMap().get(removeId).getNowScenesId();
        Scene scene = GlobalResource.getScenes().get(sceneId);
        scene.getRoleAll().remove(GlobalResource.getRoleHashMap().get(removeId));
        GlobalResource.getRoleHashMap().remove(removeId);
        for(String teamId : GlobalResource.getTeamList().keySet()){
            GlobalResource.getTeamList().get(teamId).getRoleList().remove(removeId);
        }
        System.out.println(ctx.channel().remoteAddress()+"离线了-channelInactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 出现异常时关闭连接。
        cause.printStackTrace();
        //ctx.close();
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    static void writeMessage(String message, Channel ch) {
        DataInfo.ResponseMsg responseMsg = DataInfo.ResponseMsg.newBuilder().setMsg(message).build();
        ch.writeAndFlush(responseMsg);
    }
}

