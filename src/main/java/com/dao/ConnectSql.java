package com.dao;

import com.entity.Monster;
import com.entity.Npc;
import com.entity.Role;
import com.entity.Scene;
import com.netty.server.ServerHandler;
import com.scenes.ScenesConstruct;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static com.scenes.ScenesConstruct.scenesAll;

/**
 * 数据库连接与数据库操作方法实现
 * @Author andy
 * @create 2020/5/12 9:51
 */
public class ConnectSql {
    public String url="jdbc:mysql://localhost:3306/test?&useSSL=false&serverTimezone=UTC";
    public String user="root";
    public String pw ="123456";
    public Connection conn;
    public boolean result;
    public String nowScenes;
    public int x;
    public int y;

    public ConnectSql(){
        try {
            //1：加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            //2：获得数据库连接
            conn= DriverManager.getConnection(url,user,pw);
        }catch (Exception e){
            System.out.println("连接数据库时发生异常.异常信息为："+e);
        }
    }

    public boolean insertRegister(String username, String password) {
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM user WHERE username=?");
            preparedStatement.setString(1,username);
            ResultSet rs=preparedStatement.executeQuery();
            result = rs.next();
            rs.close();
        }catch (Exception e){
            System.out.println("We got unexpected:" + e.getMessage());
        }
        if(result){
            return result;
        }
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO user(username,password) VALUES(?,?)");
            st.setString(1, username);
            st.setString(2, password);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println("We got unexpected:" + e.getMessage());
        }
        return false;
    }

    public boolean selectLogin(String username, String password) {
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM user WHERE username=? and password=?");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            ResultSet rs=preparedStatement.executeQuery();
            result = rs.next();
            rs.close();
        }catch (Exception e){
            System.out.println("We got unexpected:" + e.getMessage());
        }
        return result;
    }

    public boolean moveTo(String moveTarget){
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT xline,yline FROM place WHERE placename=?");
            preparedStatement.setString(1, ServerHandler.role.getNowScenes());
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                x=rs.getInt("xline");
                y=rs.getInt("yline");
            }
            rs.close();
        }catch (Exception e){
            System.out.println("We got unexpected:" + e.getMessage());
        }
        result=ConnectSql.moveSuccess(x,y,moveTarget);
        if(result){
            for(Scene o:scenesAll) {
                if(o.getName().equals(ServerHandler.role.getNowScenes())){
                    o.getRoleAll().remove(ServerHandler.role);
                }
                if(o.getName().equals(moveTarget)){
                    o.getRoleAll().add(ServerHandler.role);
                }
            }
            insertRoleScenes(moveTarget);
            ServerHandler.role.setNowScenes(moveTarget);
        }
        return result;
    }

    public void insertRoleScenes(String scenes)
    {
        try{
            PreparedStatement st=conn.prepareStatement("UPDATE role SET scenes=? where rolename=?");
            st.setString(1,scenes);
            st.setString(2,ServerHandler.role.getName());
            st.executeUpdate();
        }catch (Exception e)
        {
            System.out.println("sql init error");
        }
    }

    public void insertNpc(String scenes)
    {
        try{
            PreparedStatement st=conn.prepareStatement("UPDATE role SET scenes=?");
            st.setString(1,scenes);
            st.executeUpdate();
        }catch (Exception e)
        {
            System.out.println("sql init error");
        }
    }

    public void insertMonster(String scenes)
    {
        try{
            PreparedStatement st=conn.prepareStatement("UPDATE role SET scenes=?");
            st.setString(1,scenes);
            st.executeUpdate();
        }catch (Exception e)
        {
            System.out.println("sql init error");
        }
    }

    public static boolean moveSuccess(int x, int y,String moveTarget){
        return moveTarget.equals(ScenesConstruct.place[x+1][y]) ||
                moveTarget.equals(ScenesConstruct.place[x][y+1]) ||
                moveTarget.equals(ScenesConstruct.place[x-1][y]) ||
                moveTarget.equals(ScenesConstruct.place[x][y-1]);
    }

    public boolean insertRegisterRole(String rolename){
        String initScene="起始之地";
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM role WHERE rolename=?");
            preparedStatement.setString(1,rolename);
            ResultSet rs=preparedStatement.executeQuery();
            result = rs.next();
            rs.close();
        }catch (Exception e){
            System.out.println("We got unexpected:" + e.getMessage());
        }
        if(result){
            return result;
        }
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO role(rolename,scenes,alive) VALUES(?,?,?)");
            st.setString(1, rolename);
            st.setString(2, initScene);
            st.setInt(3, 1);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println("We got unexpected:" + e.getMessage());
        }
        for(Scene o:scenesAll) {
            if(initScene.equals(o.getName())){
                o.getRoleAll().add(ServerHandler.role);
            }
        }
        return false;
    }

    public String selectRoleScenes(String rolename){
        try{
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT scenes FROM role where rolename=?");
            preparedStatement.setString(1,rolename);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                nowScenes=rs.getString("scenes");
            }
            rs.close();
        }catch (Exception e){
            System.out.println("We got unexpected:" + e.getMessage());
        }
        return nowScenes;
    }

    public boolean selectLoginRole(String rolename){
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM role WHERE rolename=?");
            preparedStatement.setString(1,rolename);
            ResultSet rs=preparedStatement.executeQuery();
            result = rs.next();
            rs.close();
        }catch (Exception e){
            System.out.println("We got unexpected:" + e.getMessage());
        }
        for(Scene scene:scenesAll) {
            if(ServerHandler.role.getNowScenes().equals(scene.getName())){
                scene.getRoleAll().add(ServerHandler.role);
            }
        }
        return result;
    }

    public ArrayList<Npc> selectNpcAll(String scenes){
        ArrayList<Npc> npcs = new ArrayList<Npc>();
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM npc WHERE scenes=?");
            preparedStatement.setString(1,scenes);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                Npc npc = new Npc(rs.getString("npcname"),rs.getString("scenes"),rs.getInt("alive"));
                npcs.add(npc);
            }
            rs.close();
        }catch (Exception e){
            System.out.println("We got unexpected:" + e.getMessage());
        }
        return npcs;
    }

    public ArrayList<Monster> selectMonsterAll(String scenes){
        ArrayList<Monster> monsters = new ArrayList<Monster>();
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM monster WHERE scenes=?");
            preparedStatement.setString(1,scenes);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                Monster monster = new Monster(rs.getString("monstername"),rs.getString("scenes"),rs.getInt("alive"));
                monsters.add(monster);
            }
            rs.close();
        }catch (Exception e){
            System.out.println("We got unexpected:" + e.getMessage());
        }
        return monsters;
    }

    public ArrayList<Role> selectRoleAll(String scenes){
        ArrayList<Role> roles = new ArrayList<Role>();
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM role WHERE scenes=?");
            preparedStatement.setString(1,scenes);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                Role role = new Role(rs.getString("rolename"),rs.getString("scenes"),rs.getInt("alive"));
                roles.add(role);
            }
            rs.close();
        }catch (Exception e){
            System.out.println("We got unexpected:" + e.getMessage());
        }
        return roles;
    }

    public ArrayList<Scene> selectScenesAll(){
        ArrayList<Scene> scenes = new ArrayList<Scene>();
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM place");
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                Scene scene = new Scene(rs.getString("placename"),rs.getInt("xline"),rs.getInt("yline"));
                scenes.add(scene);
            }
            rs.close();
        }catch (Exception e){
            System.out.println("We got unexpected:" + e.getMessage());
        }
        return scenes;
    }
}