package com.game.system.shop;

import com.game.common.Const;
import com.game.system.role.pojo.Role;
import com.game.system.achievement.pojo.AchieveResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

/**
 * 购物模块的数据库操作
 * @Author andy
 * @create 2020/7/1 17:37
 */
public class RecordDao {

    private Connection conn;

    public RecordDao(){
        try {
            Class.forName(Const.DRIVER);
            conn= DriverManager.getConnection(Const.URL,Const.USER,Const.PW);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 查找角色购买记录
     * @param roleId 角色id
     * @return 购买记录
     */
    public HashMap<Integer, HashMap<Integer,Integer>> selectBuyRecord(int roleId){
        HashMap<Integer,Integer> recordNum = new HashMap<>();
        HashMap<Integer,HashMap<Integer,Integer>> buyRecord= new HashMap<>();
        try{
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT goodsid,num FROM record where playid=?");
            preparedStatement.setInt(1,roleId);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                recordNum.put(rs.getInt("goodsid"),rs.getInt("num"));
            }
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage()+"selectBuyRecord");
        }
        buyRecord.put(roleId,recordNum);
        return buyRecord;
    }

    /**
     * 更新角色购买记录
     * @param roleId 角色id
     * @param goodsId 物品id
     * @param num 数量
     */
    public void insertBuyRecord(int roleId,int goodsId,int num){
        try {
            PreparedStatement st = conn.prepareStatement("REPLACE INTO record(playid,goodsid,num) VALUES(?,?,?)");
            st.setInt(1, roleId);
            st.setInt(2, goodsId);
            st.setInt(3, num);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"insertBuyRecord");
        }
    }

    /**
     * 获取角色成就
     * @param role 角色
     */
    public void selectAchievement(Role role){
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT achievementlist FROM achievement WHERE playid=?");
            preparedStatement.setInt(1,role.getId());
            ResultSet rs=preparedStatement.executeQuery();
            String list="";
            while (rs.next())
            {
                list=rs.getString("achievementlist");
            }
            if("".equals(list)){
                System.out.println("insertRole");
                insert(role);
            }
            char[] achieve=list.toCharArray();
            for(int i=0;i<achieve.length;i++){
                if(achieve[i]=='1'){
                    role.getAchievementBo().getAchievementHashMap().put(i+Const.ACHIEV_START,true);
                }
            }
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage()+" selectAchievement");
        }
    }

    /**
     * 更新角色成就
     * @param role 角色
     */
    public void updateAchievement(Role role){
        StringBuilder input= new StringBuilder();
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            if(role.getAchievementBo().getAchievementHashMap().get(achievId)==true){
                input.append("1");
            }else{
                input.append("0");
            }
        }
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE achievement SET achievementlist=? where playid=?");
            st.setString(1, input.toString());
            st.setInt(2, role.getId());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"updateAchievement");
        }
    }

    /**
     * 插入角色成就
     * @param role 角色
     */
    private void insert(Role role){
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO achievement(playid) VALUES(?)");
            st.setInt(1, role.getId());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"achievInsert");
        }
    }
}