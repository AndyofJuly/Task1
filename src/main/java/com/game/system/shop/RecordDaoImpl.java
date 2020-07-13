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
 * @Author andy
 * @create 2020/7/1 17:37
 */
public class RecordDaoImpl implements IRecordDao {

    private Connection conn;
    private boolean result;

    public RecordDaoImpl(){
        try {
            Class.forName(Const.DRIVER);
            conn= DriverManager.getConnection(Const.URL,Const.USER,Const.PW);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
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

    @Override
    public void insertBuyRecord(int roleId,int goodsid,int num){
        try {
            PreparedStatement st = conn.prepareStatement("REPLACE INTO record(playid,goodsid,num) VALUES(?,?,?)");
            st.setInt(1, roleId);
            st.setInt(2, goodsid);
            st.setInt(3, num);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"insertBuyRecord");
        }
    }


    // 获得成就roleMapper.selectAchievement(role);
    @Override
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

    //角色成就roleMapper.updateAchievement(role);
    @Override
    public void updateAchievement(Role role){
        String input="";
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            if(role.getAchievementBo().getAchievementHashMap().get(achievId)==true){
                input+="1";
            }else{
                input+="0";
            }
        }
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE achievement SET achievementlist=? where playid=?");
            st.setString(1, input);
            st.setInt(2, role.getId());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"updateAchievement");
        }
    }

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
