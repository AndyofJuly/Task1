package com.game.netty.server;

import com.game.common.ReflectService;
import com.game.common.PatternUtil;
import com.game.common.protobuf.DataInfo;
import com.game.system.assist.GameService;
import com.game.system.role.RoleController;
import com.game.system.role.pojo.Role;
import com.game.system.scene.pojo.Scene;
import com.game.system.assist.GlobalInfo;
import com.game.common.ResponseInf;
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

    //定义一个Channel 组，管理所有的channel
    //GlobalEventExecutor.INSTANCE是一个全局事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static HashMap<ChannelId, Integer> clientGroup = new HashMap<ChannelId, Integer>();
    Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private static HashMap<Integer, ChannelHandlerContext> sceneGroup = new HashMap<Integer, ChannelHandlerContext>();
    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.RequestMsg msgs) throws Exception {

        String msg = msgs.getMsg();
        String[] s = msg.split(" ");
        if(msg.contains("loginR")){//!msg.contains("registerR") && !msg.contains("login") && !msg.contains("register")
            try {
                clientGroup.put(ctx.channel().id(),Integer.parseInt(s[s.length-1]));
                sceneGroup.put(Integer.parseInt(s[s.length-1]),ctx);
            }catch (Exception e){
                System.out.println(e.getMessage()+"登录角色错误");
            }
        }
            System.out.println("收到来自客户端:"+msg.toString());
            RoleController.setIntList(PatternUtil.getIntList(msg.toString().split(" ")));
            RoleController.setStrList(PatternUtil.getStrList(msg.toString().split(" ")));
            ReflectService reflectService = new ReflectService();
            Channel channel = ctx.channel();
            channelGroup.forEach(ch -> {
                if(channel == ch){
                    try {
                        writeMessage(reflectService.getMethod(RoleController.getStrList().get(0)),ch);
                    }catch (Exception e){
                        writeMessage(new ResponseInf("命令无法识别"),ch);
                    }
                }
            });
    }

    //表示连接建立，第一个被执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端
        //channelGroup.writeAndFlush("客户端"+channel.remoteAddress()+"上线了\n");//全局通知，每次远程客户端地址不同
        channelGroup.forEach(ch -> {
            writeMessage(new ResponseInf("客户端"+channel.remoteAddress()+"上线了\n"),ch);
            //writeMessage("客户端"+channel.remoteAddress()+"上线了\n",ch);
            //System.out.println("客户端的提示-上线"+channel.remoteAddress());
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
            writeMessage(new ResponseInf("客户端"+channel.remoteAddress()+"离线了\n"),ch);
            //writeMessage("客户端"+channel.remoteAddress()+"离线了-handlerRemoved",ch);
            //System.out.println("客户端的提示-离线"+channel.remoteAddress());
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
        System.out.println(ctx.channel().remoteAddress()+"离线了-channelInactive");
        //先对角色数据进行保存
        new GameService().saveDataBase();
        //角色集合中移除该客户端的角色、需要在当前场景中移除、还需要在队伍中移除
        //传参必须为Integer，否则List会将id看做下标而不是元素
        if(clientGroup.get(ctx.channel().id())==null){
            return;
        }
        Integer removeId = clientGroup.get(ctx.channel().id());
        int sceneId = GlobalInfo.getRoleHashMap().get(removeId).getNowScenesId();
        Scene scene = GlobalInfo.getScenes().get(sceneId);
        scene.getRoleAll().remove(GlobalInfo.getRoleHashMap().get(removeId));
        Role removeRole = GlobalInfo.getRoleHashMap().get(removeId);
        System.out.println(removeRole.getName()+"掉线通知");
        GlobalInfo.getRoleHashMap().remove(removeId);
        for(String teamId : GlobalInfo.getTeamList().keySet()){
            if( GlobalInfo.getTeamList().get(teamId).getRoleList().contains(removeId)){
                GlobalInfo.getTeamList().get(teamId).getRoleList().remove(removeId);
                notifyTeam(removeRole,GlobalInfo.getTeamList().get(teamId).getRoleList());
            }
        }
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

    public static void writeMessage(ResponseInf message, Channel ch) {

        DataInfo.ResponseMsg.Builder builders = DataInfo.ResponseMsg.newBuilder();
        builders.setMsg(message.getMsg());

        //在此构造任何自己需要的信息，传送给客户端-扩展
        //举例：角色相关的信息
        if(message.getRole() != null){
            DataInfo.ResponseMsg.RoleMsg.Builder builder = DataInfo.ResponseMsg.RoleMsg.newBuilder();
            Role role = message.getRole();
            builder.setId(role.getId());
            builder.setMsgName(role.getName());
            builder.setMoney(role.getMoney());
            builder.setAtk(role.getAtk());
            builder.setHp(role.getHp());
            builder.setMp(role.getMp());
            builder.setPlace(role.getNowScenesId());
            builder.setLevel(role.getLevel());
            builder.setCareerId(role.getCareerId());
            builder.setUnionId(role.getUnionId());
            builders.setRoleMsg(builder);
        }

        ch.writeAndFlush(builders.build());
    }

    //实现各类业务通知，例如双方通信，世界通信，邮件提示等等- todo 可以进行抽象优化，传参为角色集合与msg或者为单个角色，可以抽象成两个方法
    //通知场景玩家
    public static void notifySceneGroup(int sceneId,String msg){//String msg,int sceneId
        Scene o = GlobalInfo.getScenes().get(sceneId);
        ArrayList<Role> roles = o.getRoleAll();
        for(Role role : roles){
            Channel channel = sceneGroup.get(role.getId()).channel();
            writeMessage(new ResponseInf(msg),channel);
        }
    }

    public static void notifyAuctionGroup(Role buyRole,int sceneId,int bid){//String msg,int sceneId
        Scene o = GlobalInfo.getScenes().get(sceneId);
        ArrayList<Role> roles = o.getRoleAll();
        for(Role role : roles){
            Channel channel = sceneGroup.get(role.getId()).channel();
            if(buyRole==null){
                writeMessage(new ResponseInf("拍卖结束！"),channel);
                continue;
            }
            writeMessage(new ResponseInf(buyRole.getName()+"出价："+bid),channel);
        }
    }

    //组队掉线通知？加入队伍、离开队伍通知？
    private void notifyTeam(Role removeRole,ArrayList<Integer> roleList){
        for(Integer key : roleList){
            Role role = GlobalInfo.getRoleHashMap().get(key);
            Channel channel = sceneGroup.get(role.getId()).channel();
            writeMessage(new ResponseInf("队友"+removeRole.getName()+"掉线了。"),channel);
        }
    }

    public static void talkWithOne(int targetId,String msg,Role role){
        Channel targetChannel = sceneGroup.get(targetId).channel();
        Channel selfChannel = sceneGroup.get(role.getId()).channel();
        writeMessage(new ResponseInf(role.getName()+"："+msg),targetChannel);
        writeMessage(new ResponseInf("我："+msg),selfChannel);
    }

    public static void talkWithAll(String msg,Role role){
        //此channel为发送者的channel，需要找到接收者的channel，然后接收者ch.writeAndFlush即可
        Channel channel = sceneGroup.get(role.getId()).channel();
        //这时我们要遍历ChannelGroup，根据不同情况，会送不同消息
        channelGroup.forEach(ch->{
            if (channel != ch){ // 默认登录以后才能收到消息
                String string = role.getName()+":"+msg;
                writeMessage(new ResponseInf(string),ch);
            }else {//回显自己发送的消息
                writeMessage(new ResponseInf("我:"+msg),ch);
            }
        });
    }

    public static void notifyTrade(int targetId,String msg,Role role){
        Channel targetChannel = sceneGroup.get(targetId).channel();
        Channel selfChannel = sceneGroup.get(role.getId()).channel();
        writeMessage(new ResponseInf(msg),targetChannel);
        writeMessage(new ResponseInf(msg),selfChannel);
    }

}

