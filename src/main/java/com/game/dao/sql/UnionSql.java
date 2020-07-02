package com.game.dao.sql;

import com.game.common.Const;
import com.game.entity.Role;
import com.game.entity.vo.Union;
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
