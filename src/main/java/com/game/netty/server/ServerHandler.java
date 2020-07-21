package com.game.netty.server;

import com.game.common.ReflectService;
import com.game.common.PatternUtil;
import com.game.common.protobuf.DataInfo;
import com.game.system.gameserver.GameController;
import com.game.system.gameserver.GameService;
import com.game.system.role.pojo.Role;
import com.game.system.scene.pojo.Scene;
import com.game.system.gameserver.GlobalInfo;
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
            GameController.setIntList(PatternUtil.getIntList(msg.toString().split(" ")));
            GameController.setStrList(PatternUtil.getStrList(msg.toString().split(" ")));
            ReflectService reflectService = new ReflectService();
            Channel channel = ctx.channel();
            channelGroup.forEach(ch -> {
                if(channel == ch){
                    try {
                        writeMessage(reflectService.getMethod(GameController.getStrList().get(0)),ch);
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
        channelGroup.add(channel);
    }

    //表示断开连接，该方法执行，会导致   channelGroup.remove(channel);   所以不用写此句代码
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //do nothing
    }

    //表示channel处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"已连接服务器");
    }

    //表示channel处于非活动状态
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //先对角色数据进行保存
        new GameService().saveDataBase();
        //角色集合中移除该客户端的角色、需要在当前场景中移除、还需要在队伍中移除
        if(clientGroup.get(ctx.channel().id())==null){
            return;
        }
        Integer removeId = clientGroup.get(ctx.channel().id());
        int sceneId = GlobalInfo.getRoleHashMap().get(removeId).getNowScenesId();
        Scene scene = GlobalInfo.getScenes().get(sceneId);
        scene.getRoleAll().remove(GlobalInfo.getRoleHashMap().get(removeId));

        Role removeRole = GlobalInfo.getRoleHashMap().get(removeId);
        GlobalInfo.getRoleHashMap().remove(removeId);

        ArrayList<Role> roles = new ArrayList<>();
        for(Integer key : GlobalInfo.getTeamList().get(removeRole.getTeamId()).getRoleList()){
            roles.add(GlobalInfo.getRoleHashMap().get(key));
        }
        GlobalInfo.getTeamList().get(removeRole.getTeamId()).getRoleList().remove(removeId);
        notifyGroupRoles(roles,"队友"+removeRole.getName()+"掉线了。");
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

    //通知某个角色集合
    public static void notifyGroupRoles(ArrayList<Role> roles,String msg){
        for(Role role : roles){
            Channel channel = sceneGroup.get(role.getId()).channel();
            writeMessage(new ResponseInf(msg),channel);
        }
    }

    //通知某个角色
    public static void notifyRole(int targetId,String targetMsg,int roleId,String roleMsg){
        Channel targetChannel = sceneGroup.get(targetId).channel();
        Channel selfChannel = sceneGroup.get(roleId).channel();
        writeMessage(new ResponseInf(targetMsg),targetChannel);
        writeMessage(new ResponseInf(roleMsg),selfChannel);
    }

    //通知自身
    public static void notifySelf(int roleId,String roleMsg){
        Channel selfChannel = sceneGroup.get(roleId).channel();
        writeMessage(new ResponseInf(roleMsg),selfChannel);
    }

}

