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
 * @Author andy
 * @create 2020/7/1 17:38
 */
public class GoodsDaoImpl implements IGoodsDao {

    private Connection conn;
    private boolean result;

    public GoodsDaoImpl(){
        try {
            Class.forName(Const.DRIVER);
            conn= DriverManager.getConnection(Const.URL,Const.USER,Const.PW);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // 获得身上装备roleMapper.selectBodyEquipment(role);
    @Override
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
                        equipment1= new Equipment(rs.getInt(equip[i]),Const.EQUIP_DURA);
                        role.getEquipmentHashMap().put(rs.getInt(equip[i]),equipment1);
                    }
                }
            }
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage()+" selectBodyEquipment");
        }
    }


    //角色身上装备roleMapper.updateRoleBodyEquipment(role);
    @Override
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

    // 获得背包-包括物品roleMapper.selectPackage(role);
    @Override
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
/*            if(goods==null){
                insert(role);
            }*/
            role.setMyPackageBo(new MyPackageBo(Const.PACKAGE_SIZE,goods));
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage()+" selectPackage");
        }
    }

    //将replace拆分，先检查数据库中有无该角色id及物品id；如果有的话使用该update方法，如果没有，再使用insert方法；
    //角色背包内容roleMapper.updatePackage(role);
    //"REPLACE INTO package(playid,goodsid,num) VALUES(?,?,?)");
    @Override
    public void updatePackage(Role role){
        try {
            //业务逻辑处理需要在外面进行，因此以下循环判断需提取到service层
            //先清空表
            //delete(role);
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
