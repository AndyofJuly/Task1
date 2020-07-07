package com.game.dao.sql;

import com.game.common.Const;
import com.game.entity.Role;
import com.game.entity.Union;
import com.game.service.UnionService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Author andy
 * @create 2020/7/1 17:36
 */
public class UnionSql {
    Connection conn;
    boolean result;

    public UnionSql(){
        try {
            Class.forName(Const.DRIVER);
            conn= DriverManager.getConnection(Const.URL,Const.USER,Const.PW);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void selectUnion(){
        //查出所有unionid，给unionHashMap
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

    //公会列表和每个公会的仓库roleMapper.updateUnion(role);//暂时放在此处
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

    //需要先查找，根据是否包含进行插入或者更新操作
    public void updateUnionStore(Role role){
        try {
            //todo 业务逻辑处理需要在外面进行，因此以下循环判断需提取到service层
            //delete(role);
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

    public void updateUnionMemb(){
        try {
            for(Integer uid : UnionService.unionHashMap.keySet()){
                for(Integer rid : UnionService.unionHashMap.get(uid).getRoleJobHashMap().keySet()){
                    int grade = UnionService.unionHashMap.get(uid).getRoleJobHashMap().get(rid);
                    if(checkUnionMemb(uid,rid)==false){
                        insertUnionMemb(uid,rid,grade);
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
            System.out.println(e.getMessage()+"updateUnionMemb");
        }
    }

    private boolean checkUnionMemb(int unionid,int playid){
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("select * from unionmember where unionid=? and playid=?");
            preparedStatement.setInt(1,unionid);
            preparedStatement.setInt(2,playid);
            ResultSet rs=preparedStatement.executeQuery();
            result = rs.next();
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"checkUnionMemb");
        }
        return result;
    }

    private void insertUnionMemb(int unionid,int playid,int grade){
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO unionmember(unionid,playid,grade) VALUES(?,?,?)");
            st.setInt(1, unionid);
            st.setInt(2, playid);
            st.setInt(3, grade);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"insertUnionMemb");
        }
    }

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
