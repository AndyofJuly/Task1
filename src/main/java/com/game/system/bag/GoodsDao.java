package com.game.system.bag;

import com.game.common.Const;
import com.game.system.bag.pojo.Equipment;
import com.game.system.bag.pojo.MyPackageBo;
import com.game.system.role.pojo.Role;
import com.game.system.bag.pojo.EquipmentResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

/**
 * 背包模块的数据库操作
 * @Author andy
 * @create 2020/7/1 17:38
 */
public class GoodsDao {

    private Connection conn;
    private boolean result;

    public GoodsDao(){
        try {
            Class.forName(Const.DRIVER);
            conn= DriverManager.getConnection(Const.URL,Const.USER,Const.PW);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 获取角色身穿装备
     * @param role 角色
     */
    public void selectBodyEquipment(Role role){
        try {
            PreparedStatement preparedStatement=conn.prepareStatement(
                    "SELECT weapon,circlet,necklace,body,head,foot FROM bodyequipment WHERE playid=?");
            preparedStatement.setInt(1,role.getId());
            ResultSet rs=preparedStatement.executeQuery();
            String[] equip = {"weapon","circlet","necklace","body","head","foot"};
            while (rs.next())
            {
                Equipment equipment1;
                for(int i=0;i<equip.length;i++){
                    if(rs.getInt(equip[i])!=0){
                        equipment1= new Equipment(rs.getInt(equip[i]));
                        role.getEquipmentHashMap().put(rs.getInt(equip[i]),equipment1);
                    }
                }
            }
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage()+" selectBodyEquipment");
        }
    }

    /**
     * 更新角色身穿装备
     * @param role 角色
     */
    public void updateRoleBodyEquipment(Role role){
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE bodyequipment " +
                    "SET weapon=?,circlet=?,necklace=?,body=?,head=?,foot=? where playid=?");
            int[] selfEquip = new int[6];
            for(Integer selfEquipId : role.getEquipmentHashMap().keySet()){
                int type = EquipmentResource.getEquipmentStaticHashMap().get(selfEquipId).getType();
                for(int i=0;i<selfEquip.length;i++){
                    if(type==i+1){
                        selfEquip[i]=selfEquipId;
                    }
                }
            }
            for(int i=0;i<selfEquip.length;i++){
                st.setInt(i+1, selfEquip[i]);
            }
            st.setInt(7, role.getId());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"updateRoleBodyEquipment");
        }
    }

    /**
     * 获取角色背包物品
     * @param role 角色
     */
    public void selectPackage(Role role){
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT goodsid,num FROM package WHERE playid=?");
            preparedStatement.setInt(1,role.getId());
            ResultSet rs=preparedStatement.executeQuery();
            HashMap<Integer,Integer> goods = new HashMap<Integer,Integer>();
            while (rs.next())
            {
                goods.put(rs.getInt("goodsid"),rs.getInt("num"));
            }
            role.setMyPackageBo(new MyPackageBo(Const.PACKAGE_SIZE,goods));
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage()+" selectPackage");
        }
    }

    /**
     * 更新角色背包物品
     * @param role 角色
     */
    public void updatePackage(Role role){
        try {
            for(Integer goodsId : role.getMyPackageBo().getGoodsHashMap().keySet()){
                if(checkPackage(role,goodsId)==false){
                    insertPackage(role,goodsId,role.getMyPackageBo().getGoodsHashMap().get(goodsId));
                }else{
                    PreparedStatement st = conn.prepareStatement("update package set num=? where playid=? and goodsid=?");
                    st.setInt(1, role.getMyPackageBo().getGoodsHashMap().get(goodsId));
                    st.setInt(2, role.getId());
                    st.setInt(3, goodsId);
                    st.executeUpdate();
                    st.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()+"updatePackage");
        }
    }

    /**
     * 判断是否有背包的记录
     * @param role 角色
     * @return 是否有背包
     */
    private boolean checkPackage(Role role,int goodsId){
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("select * from package where playid=? and goodsid=?");
            preparedStatement.setInt(1,role.getId());
            preparedStatement.setInt(2,goodsId);
            ResultSet rs=preparedStatement.executeQuery();
            result = rs.next();
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"checkPackage");
        }
        return result;
    }

    /**
     * 插入一条背包数据
     * @param role 角色
     */
    private void insertPackage(Role role,int goodsId,int nums){
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO package(playid,goodsId,num) VALUES(?,?,?)");
            st.setInt(1, role.getId());
            st.setInt(2, goodsId);
            st.setInt(3, nums);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"insertPackage");
        }
    }

    /**
     * 删除一条背包数据
     * @param role 角色
     */
    private void delete(Role role){
        try {
            PreparedStatement st = conn.prepareStatement("delete from package where playid=?");
            st.setInt(1, role.getId());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"packageDelete");
        }
    }

    /**
     * 插入一条除id外的空数据
     * @param role 角色
     */
    private void insert(Role role){
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO package(playid) VALUES(?)");
            st.setInt(1, role.getId());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"packageInsert");
        }
    }
}
