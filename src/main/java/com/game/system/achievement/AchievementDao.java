package com.game.system.achievement;

import com.game.common.Const;
import com.game.system.role.entity.Role;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *成就模块的数据库操作
 * @Author andy
 * @create 2020/7/21 7:39
 */
@Repository
public class AchievementDao {

    private Connection conn;

    public AchievementDao(){
        try {
            Class.forName(Const.DRIVER);
            conn= DriverManager.getConnection(Const.URL,Const.USER,Const.PW);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 获取角色成就
     * @param role 角色
     */
    public void selectAchievement(Role role){
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("SELECT * FROM achievement WHERE playid=?");
            preparedStatement.setInt(1,role.getId());
            ResultSet rs=preparedStatement.executeQuery();
            String list="";
            while (rs.next()){
                list=rs.getString("achievementlist");
                role.getAchievementCountMap().put(Achievement.killMonsterThief.getId(),rs.getInt("killMonsterThief"));
                role.getAchievementCountMap().put(Achievement.levelUpA.getId(),rs.getInt("levelUp"));
                role.getAchievementCountMap().put(Achievement.talkNpc.getId(),rs.getInt("talkNpc"));
                role.getAchievementCountMap().put(Achievement.getBestEquip.getId(),rs.getInt("getBestEquip"));
                role.getAchievementCountMap().put(Achievement.passDungeons.getId(),rs.getInt("passDungeons"));
                role.getAchievementCountMap().put(Achievement.sumEquipLevel.getId(),rs.getInt("sumEquipLevel"));
                role.getAchievementCountMap().put(Achievement.addFriend.getId(),rs.getInt("addFriend"));
                role.getAchievementCountMap().put(Achievement.firstJoinTeam.getId(),rs.getInt("firstJoinTeam"));
                role.getAchievementCountMap().put(Achievement.firstJoinUnion.getId(),rs.getInt("firstJoinUnion"));
                role.getAchievementCountMap().put(Achievement.firstTrade.getId(),rs.getInt("firstTrade"));
                role.getAchievementCountMap().put(Achievement.firstPkSuccess.getId(),rs.getInt("firstPkSuccess"));
                role.getAchievementCountMap().put(Achievement.sumMoney.getId(),rs.getInt("sumMoney"));
                role.getAchievementCountMap().put(Achievement.completeTask.getId(),rs.getInt("completeTask"));
                role.getAchievementCountMap().put(Achievement.killMonsterWicked.getId(),rs.getInt("killMonsterWicked"));
                role.getAchievementCountMap().put(Achievement.levelUpB.getId(),rs.getInt("levelUpB"));
            }
            if("".equals(list)){
                insert(role);
            }
            char[] achieve=list.toCharArray();
            for(int i=0;i<achieve.length;i++){
                if(achieve[i]=='1'){
                    role.getAchievementBo().getAchievementHashMap().put(i+ Const.ACHIEV_START,true);
                }
            }
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage()+" selectAchievement");
        }
    }

    /**
     * 更新角色成就-改
     * @param role 角色
     */
    public void updateAchievement(Role role){
        StringBuilder input= new StringBuilder();
        for(Achievement achievement : Achievement.values()){
            if(role.getAchievementBo().getAchievementHashMap().get(achievement.getId())){
                input.append("1");
            }else{
                input.append("0");
            }
        }
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE achievement SET achievementlist=?,killMonsterThief=?,levelUp=?,talkNpc=?,getBestEquip=?,passDungeons=?," +
                    "sumEquipLevel=?,addFriend=?,firstJoinTeam=?,firstJoinUnion=?,firstTrade=?,firstPkSuccess=?,sumMoney=?,completeTask=?,killMonsterWicked=?,levelUpB=? where playid=?");
            st.setString(1, input.toString());

            st.setInt(2, role.getAchievementCountMap().get(Achievement.killMonsterThief.getId()));
            st.setInt(3, role.getAchievementCountMap().get(Achievement.levelUpA.getId()));
            st.setInt(4, role.getAchievementCountMap().get(Achievement.talkNpc.getId()));
            st.setInt(5, role.getAchievementCountMap().get(Achievement.getBestEquip.getId()));
            st.setInt(6, role.getAchievementCountMap().get(Achievement.passDungeons.getId()));
            st.setInt(7, role.getAchievementCountMap().get(Achievement.sumEquipLevel.getId()));
            st.setInt(8, role.getAchievementCountMap().get(Achievement.addFriend.getId()));
            st.setInt(9, role.getAchievementCountMap().get(Achievement.firstJoinTeam.getId()));
            st.setInt(10, role.getAchievementCountMap().get(Achievement.firstJoinUnion.getId()));
            st.setInt(11, role.getAchievementCountMap().get(Achievement.firstTrade.getId()));
            st.setInt(12, role.getAchievementCountMap().get(Achievement.firstPkSuccess.getId()));
            st.setInt(13, role.getAchievementCountMap().get(Achievement.sumMoney.getId()));
            st.setInt(14, role.getAchievementCountMap().get(Achievement.completeTask.getId()));
            st.setInt(15, role.getAchievementCountMap().get(Achievement.killMonsterWicked.getId()));
            st.setInt(16, role.getAchievementCountMap().get(Achievement.levelUpB.getId()));

            st.setInt(17, role.getId());
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
