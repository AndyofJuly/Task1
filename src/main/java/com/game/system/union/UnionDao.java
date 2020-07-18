package com.game.system.union;

import com.game.common.Const;
import com.game.system.role.pojo.Role;
import com.game.system.union.pojo.Union;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 公会模块的数据库操作
 * @Author andy
 * @create 2020/7/1 17:36
 */
public class UnionDao {
    private Connection conn;
    private boolean result;

    public UnionDao(){
        try {
            Class.forName(Const.DRIVER);
            conn= DriverManager.getConnection(Const.URL,Const.USER,Const.PW);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /** 获取所有公会 */
    public void selectUnion(){
        try {
            PreparedStatement st = conn.prepareStatement("select unionid,unionname from unions");
            ResultSet rs=st.executeQuery();
            while (rs.next()){
                UnionService.unionHashMap.put(rs.getInt("unionid"),new Union(rs.getInt("unionid"),rs.getString("unionname")));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"selectUnion");
        }
    }

    /** 获取公会成员，放入公会集合中 */
    public void selectUnionMemb(){
        try {
            PreparedStatement st = conn.prepareStatement("select unionid,playid,grade from unionmember");
            ResultSet rs=st.executeQuery();
            while (rs.next()){
                int unionid = rs.getInt("unionid");
                int playid = rs.getInt("playid");
                int grade = rs.getInt("grade");
                UnionService.unionHashMap.get(unionid).getRoleJobHashMap().put(playid,grade);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"selectUnionMemb");
        }
    }

    /** 获取公会仓库物品 */
    public void selectUnionStore(){
        try {
            PreparedStatement st = conn.prepareStatement("select unionid,goodsid,num from unionstore");
            ResultSet rs=st.executeQuery();
            while (rs.next()){
                int unionid = rs.getInt("unionid");
                int goodsid = rs.getInt("goodsid");
                int num = rs.getInt("num");
                UnionService.unionHashMap.get(unionid).getGoodsHashMap().put(goodsid,num);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"selectUnionMemb");
        }
    }

    /**
     * 更新所有公会
     * @param role 角色
     */
    public void updateUnion(Role role){
        if(role.getUnionId()==0){return;}
        try {
            PreparedStatement st = conn.prepareStatement("REPLACE INTO unions(unionid,unionname,money) VALUES(?,?,?)");
            st.setInt(1, role.getUnionId());
            st.setString(2, UnionService.unionHashMap.get(role.getUnionId()).getName());
            st.setInt(3, UnionService.unionHashMap.get(role.getUnionId()).getMoney());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"updateUnion");
        }
    }

    /**
     * 更新公会仓库，需要先查找，根据是否包含进行插入或者更新操作
     * @param role 角色
     */
    public void updateUnionStore(Role role){
        try {
            if(role.getUnionId()==0){return;}
            Union union = UnionService.unionHashMap.get(role.getUnionId());
            for(Integer goodsId : union.getGoodsHashMap().keySet()){
                if(checkUnionStore(role,goodsId)==false){
                    insertUnionStore(role,goodsId,union.getGoodsHashMap().get(goodsId));
                }else{
                    PreparedStatement st = conn.prepareStatement("update unionstore set num=? where unionid=? and goodsid=?");
                    st.setInt(1, union.getGoodsHashMap().get(goodsId));
                    st.setInt(2, role.getUnionId());
                    st.setInt(3, goodsId);
                    st.executeUpdate();
                    st.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()+"updateUnionStore");
        }
    }

    /** 更新公会成员 */
    public void updateUnionMember(){
        try {
            for(Integer uid : UnionService.unionHashMap.keySet()){
                for(Integer rid : UnionService.unionHashMap.get(uid).getRoleJobHashMap().keySet()){
                    int grade = UnionService.unionHashMap.get(uid).getRoleJobHashMap().get(rid);
                    if(!checkUnionMember(uid, rid)){
                        insertUnionMember(uid,rid,grade);
                    }else{
                        PreparedStatement st = conn.prepareStatement("update unionmember set unionid=?,playid=?,grade=?");
                        st.setInt(1, uid);
                        st.setInt(2, rid);
                        st.setInt(3, grade);
                        st.executeUpdate();
                        st.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()+"updateUnionMember");
        }
    }

    /**
     * 查找公会成员
     * @param unionid 公会id
     * @param playid 角色id
     * @return 是否有该成员
     */
    private boolean checkUnionMember(int unionid,int playid){
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("select * from unionmember where unionid=? and playid=?");
            preparedStatement.setInt(1,unionid);
            preparedStatement.setInt(2,playid);
            ResultSet rs=preparedStatement.executeQuery();
            result = rs.next();
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"checkUnionMember");
        }
        return result;
    }

    /**
     * 插入公会成员
     * @param unionid 公会id
     * @param playid 角色id
     * @param grade 权限
     */
    private void insertUnionMember(int unionid,int playid,int grade){
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO unionmember(unionid,playid,grade) VALUES(?,?,?)");
            st.setInt(1, unionid);
            st.setInt(2, playid);
            st.setInt(3, grade);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"insertUnionMember");
        }
    }

    /**
     * 查找公会仓库物品
     * @param role 角色
     * @param goodsId 物品id
     * @return 有无该物品
     */
    private boolean checkUnionStore(Role role,int goodsId){
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("select * from unionstore where unionid=? and goodsid=?");
            preparedStatement.setInt(1,role.getUnionId());
            preparedStatement.setInt(2,goodsId);
            ResultSet rs=preparedStatement.executeQuery();
            result = rs.next();
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"checkUnionStore");
        }
        return result;
    }

    /**
     * 公会仓库中插入物品
     * @param role 角色
     * @param goodsId 物品id
     * @param nums 数量
     */
    private void insertUnionStore(Role role,int goodsId,int nums){
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO unionstore(unionid,goodsId,num) VALUES(?,?,?)");
            st.setInt(1, role.getUnionId());
            st.setInt(2, goodsId);
            st.setInt(3, nums);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"insertUnionStore");
        }
    }

    /**
     * 清除公会仓库
     * @param role 角色
     */
    private void delete(Role role){
        try {
            PreparedStatement st = conn.prepareStatement("delete from unionstore where playid=?");
            st.setInt(1, role.getId());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"unionDelete");
        }
    }
}
