package com.netty.server;

import com.dao.ConnectSql;
import com.entity.Role;
import com.entity.User;
import com.service.RoleService;
import com.service.UserService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * netty服务端业务逻辑
 * 引用地址：https://blog.csdn.net/maoyuanming0806/article/details/81097083?
 * utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.
 * nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.nonecase
 * @author maoyuanming0806 and andy
 * @create 2020/5/12 22:32
 */

public class ServerHandler extends ChannelInboundHandlerAdapter {
    public static User user;
    public static Role role;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String register = "register";
        String login = "login";
        String registerR = "registerR";
        String loginR = "loginR";
        String move = "move";
        String aoi = "aoi";
        String checkPlace = "checkPlace";

        System.out.println("收到来自客户端的命令:"+msg.toString());

        UserService userService = new UserService();
        RoleService roleService = new RoleService();

        String[] strings;
        strings = msg.toString().split(" ");

        //用户注册
        if(register.equals(strings[0])){
            ctx.channel().writeAndFlush(userService.register(strings[1],strings[2]));
        }

        //用户登录
        else if(login.equals(strings[0])){
            user = new User(strings[1],strings[2]);
            ctx.channel().writeAndFlush(userService.login(strings[1],strings[2]));
        }

        //角色注册，注册完直接以该角色开始游戏
        else if(registerR.equals(strings[0])){
            role = new Role(strings[1],"起始之地",1);
            user.setRole(role);
            ctx.channel().writeAndFlush(userService.registerRole(strings[1]));
        }

        // 登陆角色，登陆完即开始游戏
        else if(loginR.equals(strings[0])){
            ConnectSql connectSql = new ConnectSql();
            role = new Role(strings[1],connectSql.selectRoleScenes(strings[1]),1);
            user.setRole(role);
            ctx.channel().writeAndFlush(userService.loginRole(strings[1]));
        }

        // 角色移动，即场景切换，判断是否能成功移动
        else if(move.equals(strings[0])){
            ctx.channel().writeAndFlush(roleService.move(strings[1]));
        }

        // 查看角色目前所在的当前场景的所有实体信息
        else if(aoi.equals(strings[0])){
            ctx.channel().writeAndFlush(roleService.aoi());
        }

        // 查看其它任意场景上的所有实体信息
        else if(checkPlace.equals(strings[0])){
            ctx.channel().writeAndFlush(roleService.checkPlace(strings[1]));
        }

        // 未知命令
        else {
            ctx.channel().writeAndFlush("输入命令有误，请重新输入");
        }
        ctx.fireChannelRead(msg);
    }
}