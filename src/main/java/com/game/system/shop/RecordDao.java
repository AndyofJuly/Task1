package com.game.system.shop;

import com.game.common.Const;
import com.game.system.role.pojo.Role;
import com.game.system.achievement.pojo.AchieveResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

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
@Repository
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

}
