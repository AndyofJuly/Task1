package com.game.system.entergame;

import com.game.common.Const;
import com.game.system.role.pojo.Role;
import com.game.system.role.pojo.CareerStatic;
import com.game.system.role.pojo.CareerResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * 数据库连接与数据库操作方法实现-9个方法
 * @Author andy
 * @create 2020/5/12 9:51
 */

public class PotralDaoImpl implements IPotralDao {
    private Connection conn;
    private boolean result;

    public PotralDaoImpl(){
        try {
            Class.forName(Const.DRIVER);
            conn= DriverManager.getConnection(Const.URL,Const.USER,Const.PW);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 用户注册
     * @param username 用户名
     * @return 是否注册成功
     */
    private boolean checkUserName(String username){
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM user WHERE username=?");
            preparedStatement.setString(1,username);
            ResultSet rs=preparedStatement.executeQuery();
            result = rs.next();
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public boolean insertRegister(String username, String password) {
        if(checkUserName(username)){return true;}
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO user(username,password) VALUES(?,?)");
            st.setString(1, username);
            st.setString(2, password);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"insertRegister");
        }
        return false;
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 用户密码
     * @return 是否登录成功
     */
    @Override
    public int selectLogin(String username, String password) {
        int id = 0;
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT userid FROM user WHERE username=? and password=?");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                id=rs.getInt("userid");
            }
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage()+"selectLogin");
        }
        return id;
    }

    @Override
    public ArrayList<Integer> selectRole(int userId){
        ArrayList<Integer> roleList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT playid FROM role WHERE userid=?");
            preparedStatement.setInt(1,userId);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                roleList.add(rs.getInt("playid"));
            }
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage()+"selectLogin");
        }
        return roleList;
    }

    /**
     * 角色注册
     * @param rolename 角色名
     * @return 是否角色注册成功
     */
    @Override
    public boolean checkRoleName(String rolename){
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM role WHERE rolename=?");
            preparedStatement.setString(1,rolename);
            ResultSet rs=preparedStatement.executeQuery();
            result = rs.next();
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public void insertRegisterRole(String rolename,int careerId){
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO role" +
                    "(rolename,placeid,alive,careerid,hp,mp,atk,money,def,unionid,nowlevel) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            //根据职业id查找对应的初始化值
            CareerStatic career = CareerResource.getCareerStaticHashMap().get(careerId);
            st.setString(1, rolename);
            st.setInt(2, Const.INIT_SCENE);
            st.setInt(3, 1);
            st.setInt(4, careerId);
            st.setInt(5, career.getHp());
            st.setInt(6, career.getMp());
            st.setInt(7, career.getAtk());
            st.setInt(8, Const.INIT_MONEY);
            st.setInt(9, career.getDef());
            st.setInt(10, 0);
            st.setInt(11, 1);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"insertRegisterRole");
        }
    }

    /**
     * 角色登录
     * @param roleId 角色名
     * @return 是否角色登录成功
     */
    @Override
    public Role selectLoginRole(int roleId){
        Role role = new Role(roleId);
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT placeid,careerid,hp,mp,atk,money,def,nowlevel,rolename,unionid FROM role WHERE playid=?");
            preparedStatement.setInt(1,roleId);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                role.setNowScenesId(rs.getInt("placeid"));
                role.setCareerId(rs.getInt("careerid"));
                role.setHp(rs.getInt("hp"));
                role.setMp(rs.getInt("mp"));
                role.setAtk(rs.getInt("atk"));
                role.setMoney(rs.getInt("money"));
                role.setDef(rs.getInt("def"));
                role.setLevel(rs.getInt("nowlevel"));
                role.setName(rs.getString("rolename"));
                role.setUnionId(rs.getInt("unionid"));
            }
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage()+"selectLoginRole");
        }
        return role;
    }

/*    public void insertRoleScenes(int scenesId, Role role){
        try{
            PreparedStatement st=conn.prepareStatement("UPDATE role SET placeid=? where playid=?");
            st.setInt(1,scenesId);
            st.setInt(2, role.getId());
            st.executeUpdate();
        }catch (Exception e)
        {
            System.out.println(e.getMessage()+"insertRoleScenes");
        }
    }*/

/*    public int selectRoleIdByName(String rolename){
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
            System.out.println(e.getMessage()+"selectRoleIdByName");
        }
        return id;
    }*/

    //退出游戏，结束游戏时的一些方法，需要对数据持久化-更新一下数据库（todo 可以扩展为定时更新例如5分钟1次），包括
    //角色信息持久化，update，增加所在公会更新roleMapper.updateRoleInfo(role);
    @Override
    public void updateRoleInfo(Role role){
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE role SET placeid=?,hp=?,mp=?,money=?,def=?,unionid=?,nowlevel=? where playid=?");
            st.setInt(1, role.getNowScenesId());
            st.setInt(2, role.getHp());
            st.setInt(3, role.getMp());
            st.setInt(4, role.getMoney());
            st.setInt(5, role.getDef());
            st.setInt(6, role.getUnionId());
            st.setInt(7, role.getLevel());
            st.setInt(8, role.getId());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"updateRoleInfo");
        }
    }






}