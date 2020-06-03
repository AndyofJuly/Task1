package com.game.service;

import com.game.dao.ConnectSql;


/**
 * 用户一些功能的方法实现
 * @Author andy
 * @create 2020/5/11 21:50
 */

public class UserService {

    ConnectSql connectSql = new ConnectSql();

    //用户注册
    public String register(String username, String password){

        if(connectSql.insertRegister(username,password)){
            return "注册失败，该用户名已有人使用";
        }else {
            return "注册成功";
        }
    }

    //用户登录
    public int login(String username, String password){
        return connectSql.selectLogin(username,password);
/*        if(connectSql.selectLogin(username,password)!=0){
            //return "登陆成功";
        }else {
            //return "用户名或密码错误，登陆失败";
        }*/
    }

    //角色注册
    public String registerRole(String rolename,int roleId){
        if(connectSql.insertRegisterRole(rolename,roleId)){
            return "注册失败，该角色名称已有人使用";
        }else {
            return "注册成功，您进入到了游戏世界";
        }
    }

    //角色登录
    public String loginRole(String rolename,int roleId){
        if(connectSql.selectLoginRole(rolename,roleId)){
            return "登陆成功，您进入到了游戏世界";
        }else {
            return "您没有该角色，登陆失败";
        }
    }
}
