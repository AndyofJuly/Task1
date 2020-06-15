package com.game.dao;

import com.game.common.Const;
//import com.game.common.InitStaticResource;
import com.game.controller.FunctionService;
import com.game.entity.store.SceneResource;
import com.game.service.assis.InitGame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 数据库连接与数据库操作方法实现
 * @Author andy
 * @create 2020/5/12 9:51
 */

public class ConnectSql {
    public Connection conn;
    public boolean result;
    public int nowScenesId;
    public int id;
    public static ConnectSql sql = new ConnectSql();

    public ConnectSql(){
        try {
            //1：加载驱动程序
            Class.forName(Const.DRIVER);
            //2：获得数据库连接
            conn= DriverManager.getConnection(Const.URL,Const.USER,Const.PW);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 用户注册
     * @param username 用户名
     * @param password 用户密码
     * @return 是否注册成功
     */
    public boolean insertRegister(String username, String password) {
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM user WHERE username=?");
            preparedStatement.setString(1,username);
            ResultSet rs=preparedStatement.executeQuery();
            result = rs.next();
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        if(result){
            return true;
        }
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO user(username,password) VALUES(?,?)");
            st.setString(1, username);
            st.setString(2, password);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 用户密码
     * @return 是否登录成功
     */
/*    public int selectLogin(String username, String password) {
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT playid FROM user WHERE username=? and password=?");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            ResultSet rs=preparedStatement.executeQuery();
            //result = rs.next();
            while (rs.next())
            {
                id=rs.getInt("playid");
            }
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return id;
    }*/
    public boolean selectLogin(String username, String password) {
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM user WHERE username=? and password=?");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            ResultSet rs=preparedStatement.executeQuery();
            result = rs.next();
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * 角色注册
     * @param rolename 角色名
     * @return 是否角色注册成功
     */
    public boolean insertRegisterRole(String rolename,int careerId){
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM role WHERE rolename=?");
            preparedStatement.setString(1,rolename);
            ResultSet rs=preparedStatement.executeQuery();
            result = rs.next();
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        if(result){
            //数据库中有该名字，注册失败，退出，不进行数据库插入操作
            return false;
        }
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO role(rolename,placeid,alive,careerid) VALUES(?,?,?,?)");
            st.setString(1, rolename);
            st.setInt(2, 10001);
            st.setInt(3, 1);
            st.setInt(4, careerId);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    /**
     * 角色登录
     * @param rolename 角色名
     * @return 是否角色登录成功
     */
    public int selectLoginRole(String rolename,int roleId){
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT careerid FROM role WHERE rolename=?");
            preparedStatement.setString(1,rolename);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                id=rs.getInt("careerid");//职业id
            }
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return id;
    }

    /**
     * 给当前角色设置所在的场景属性，用于最后离开游戏时保存角色所在的位置
     * @param scenesId 场景id
     */
    public void insertRoleScenes(int scenesId,int roleId)
    {
        try{
            PreparedStatement st=conn.prepareStatement("UPDATE role SET placeid=? where rolename=?");
            st.setInt(1,scenesId);
            st.setString(2, FunctionService.roleHashMap.get(roleId).getName());
            st.executeUpdate();
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 根据角色名查找角色id
     * @param rolename 角色名
     * @return 角色id
     */
    public int selectRoleIdByName(String rolename){
        try{
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT playid FROM role where rolename=?");
            preparedStatement.setString(1,rolename);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                id=rs.getInt("playid");
            }
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return id;
    }

    /**
     * 根据角色名查找当前场景id
     * @param rolename 角色名
     * @return 场景id
     */
    public int selectRoleScenesId(String rolename){
        try{
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT placeid FROM role where rolename=?");
            preparedStatement.setString(1,rolename);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                nowScenesId=rs.getInt("placeid");
            }
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return nowScenesId;
    }

    /**
     * 根据用户名查找角色id
     * @param username 用户名
     * @return 角色id
     */

}