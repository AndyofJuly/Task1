package com.game.system.bag;

import com.game.common.Const;
import com.game.system.assist.GlobalInfo;
import com.game.system.bag.pojo.Equipment;
import com.game.system.bag.pojo.MyPackageBo;
import com.game.system.role.pojo.Role;
import com.game.system.bag.pojo.EquipmentResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

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
@Repository
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
                Equipment equipment;
                for(int i=0;i<equip.length;i++){
                    if(rs.getInt(equip[i])!=0){
                        int staticId = checkStaticByEquipId(rs.getInt(equip[i]))[0];
                        int dura = checkStaticByEquipId(rs.getInt(equip[i]))[1];
                        equipment= new Equipment(rs.getInt(equip[i]),staticId,dura);//新增一个表，动态随机id对应静态id；在游戏运行之前全部读到内存中。耐久也要存数据库？
                        GlobalInfo.getEquipmentHashMap().put(equipment.getId(),equipment);//全局记录
                        role.getEquipmentHashMap().put(i,rs.getInt(equip[i]));//rs.getInt(equip[i]
                    }
                }
            }
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage()+" selectBodyEquipment");
        }
    }

    public int[] checkStaticByEquipId(int equipId){
/*        int staticId = 0;
        int dura = 0;*/
        int[] arr = new int[2];
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("select staticid,dura from equipment where equipid=?");
            preparedStatement.setInt(1,equipId);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next())
            {
                arr[0] = rs.getInt("staticid");
                arr[1] = rs.getInt("dura");
            }
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage()+" checkStaticByEquipId");
        }
        return arr;
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
            for(int i=0;i<selfEquip.length;i++){//Integer key : role.getEquipmentHashMap().keySet()
                if(role.getEquipmentHashMap().get(i)!=null){
                    st.setInt(i+1, role.getEquipmentHashMap().get(i));
                }else {
                    st.setInt(i+1, 0);
                }
            }
/*            for(Integer key : role.getEquipmentHashMap().keySet()){ todo
                int type = EquipmentResource.getEquipmentStaticHashMap().get(selfEquipId).getType();
                for(int i=0;i<selfEquip.length;i++){
                    if(type==i+1){
                        selfEquip[i]=selfEquipId;
                    }
                }
            }
            for(int i=0;i<selfEquip.length;i++){
                st.setInt(i+1, selfEquip[i]);
            }*/
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
                //物品id和物品数量
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
                if(!checkPackage(role, goodsId)){
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

    //获取全局武器数据库信息
    public void getEquipment(){
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT equipid,staticid,dura FROM equipment");
            ResultSet rs=preparedStatement.executeQuery();
            HashMap<Integer,Equipment> equipmentHashMap = new HashMap<Integer,Equipment>();
            while (rs.next())
            {
                Equipment equipment = new Equipment(rs.getInt("equipid"),rs.getInt("staticid"),rs.getInt("dura"));
                equipmentHashMap.put(rs.getInt("equipid"),equipment);
            }
            GlobalInfo.setEquipmentHashMap(equipmentHashMap);
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage()+" getEquipment");
        }
    }

    //更新全局武器数据库表，有则update，没有则insert
    public void updateEquipment(){
        try {
            for(Integer key : GlobalInfo.getEquipmentHashMap().keySet()){
                Equipment equipment = GlobalInfo.getEquipmentHashMap().get(key);
                if(!checkEquipment(equipment.getId())){
                    insertEquipment(equipment.getId(),equipment.getEquipmentId(),equipment.getDura());
                }else{
                    PreparedStatement st = conn.prepareStatement("update equipment set dura=? where equipId=?");
                    st.setInt(1, equipment.getDura());
                    st.setInt(2, equipment.getId());
                    st.executeUpdate();
                    st.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()+"updateEquipment");
        }
    }

    private boolean checkEquipment(int equipId){
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM equipment where equipid = ?");
            preparedStatement.setInt(1,equipId);
            ResultSet rs=preparedStatement.executeQuery();
            result = rs.next();
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage()+" checkEquipment");
        }
        return result;
    }

    private void insertEquipment(int equipId,int staticId,int dura){
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO equipment(equipId,staticId,dura) VALUES(?,?,?)");
            st.setInt(1, equipId);
            st.setInt(2, staticId);
            st.setInt(3, dura);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"insertEquipment");
        }
    }
}
