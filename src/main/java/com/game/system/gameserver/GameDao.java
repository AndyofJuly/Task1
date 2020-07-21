package com.game.system.gameserver;

import com.game.common.Const;
import com.game.system.role.pojo.Role;
import com.game.system.role.pojo.CareerStatic;
import com.game.system.role.pojo.CareerResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * 角色进入游戏前和游戏中的数据库操作
 * @Author andy
 * @create 2020/5/12 9:51
 */
@Repository
public class GameDao {
    private Connection conn;
    private boolean result;

    public GameDao(){
        try {
            Class.forName(Const.DRIVER);
            conn= DriverManager.getConnection(Const.URL,Const.USER,Const.PW);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 用户注册前查找是否有同名
     * @param username 用户名
     * @return 是否有同名
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

    /**
     * 用户注册
     * @param username 用户名
     * @param password 用户密码
     * @return 能否注册成功
     */
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
     * @return 用户id
     */
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

    /**
     * 获取该用户的所有角色
     * @param userId 用户id
     * @return 该用户的所有角色id集合
     */
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
     * 新注册角色时，角色表没有用户id，因此为角色表插入用户id
     * @param userId 用户id
     */
    public void insertUserId(int userId,int roleid){
        try {
            PreparedStatement st = conn.prepareStatement("update role set userid=? where playid=?");
            st.setInt(1, userId);
            st.setInt(2, roleid);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"insertUserId");
        }
    }

    /**
     * 注册角色前检查角色名
     * @param rolename 角色名
     * @return 是否有相同的角色名
     */
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

    /**
     * 角色注册
     * @param rolename 角色名
     * @param careerId 角色职业id
     */
    public int insertRegisterRole(String rolename,int careerId){
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO role" +
                    "(rolename,placeid,alive,careerid,hp,mp,atk,money,def,unionid,nowlevel) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
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
        return selectRoleIdByName(rolename);
    }

    /**
     * 登录时查找角色，并对属性赋值
     * @param roleId 角色id
     * @return Role
     */
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

    /**
     * 根据角色名查找角色id
     * @param rolename 角色名
     * @return 角色id
     */
    public int selectRoleIdByName(String rolename){
        int id = 0;
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
    }

    /**
     * 更新角色信息，对角色信息持久化
     * @param role 角色
     */
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