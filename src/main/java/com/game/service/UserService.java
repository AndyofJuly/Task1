package com.game.service;

import com.game.dao.ConnectSql;


/**
 * 用户一些功能的方法实现
 * @Author andy
 * @create 2020/5/11 21:50
 */

public class UserService {

    ConnectSql connectSql = new ConnectSql();
    public String register(String username, String password){

        if(connectSql.insertRegister(username,password)){
            return "注册失败，该用户名已有人使用";
        }else {
            return "注册成功";
        }
    }

    public String login(String username, String password){
        if(connectSql.selectLogin(username,password)){
            return "登陆成功";
        }else {
            return "用户名或密码错误，登陆失败";
        }
    }

    public String registerRole(String rolename){
        if(connectSql.insertRegisterRole(rolename)){
            return "注册失败，该角色名称已有人使用";
        }else {
            return "注册成功，您进入到了游戏世界";
        }
    }

    public String loginRole(String rolename){
        if(connectSql.selectLoginRole(rolename)){
            return "登陆成功，您进入到了游戏世界";
        }else {
            return "您没有该角色，登陆失败";
        }
    }
}
