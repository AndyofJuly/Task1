package com.game.system.achievement;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.role.pojo.Role;
import org.springframework.stereotype.Service;

/**
 * 成就模块的业务逻辑处理
 * @Author andy
 * @create 2020/7/21 8:25
 */
@Service
public class AchievementService {

    /**
     * 对某项成就的进度进行统计-无成就目标
     * @param desc 成就描述
     * @param role 角色
     */
    public static void countAchievement(String desc,Role role){
        for(Achievement achievement : Achievement.values()) {
            if (achievement.getDesc().equals(desc)) {
                int count = role.getAchievementCountMap().get(achievement.getId());
                role.getAchievementCountMap().put(achievement.getId(),count+1);
            }
        }
    }

    /**
     * 对某项成就的进度进行统计-包含成就目标
     * @param targetId 成就目标
     * @param desc 成就描述
     * @param role 角色
     */
    public static void countAchievementWithTarget(int targetId,String desc,Role role){
        for(Achievement achievement : Achievement.values()) {
            if (achievement.getDesc().equals(desc) && targetId==achievement.getTargetId()) {
                int count = role.getAchievementCountMap().get(achievement.getId());
                role.getAchievementCountMap().put(achievement.getId(),count+1);
            }
        }
    }

    /**
     * 根据已完成进度判断是否完成某项成就-无成就目标
     * @param desc 成就描述
     * @param role 角色
     */
    public static void checkIfComplete(String desc, Role role){
        for(Achievement achievement : Achievement.values()) {
            if (achievement.getDesc().equals(desc)) {
                int nowCount = role.getAchievementCountMap().get(achievement.getId());
                int targetCount = achievement.getCount();
                if(nowCount>=targetCount){
                    role.getAchievementBo().getAchievementHashMap().put(achievement.getId(),true);
                }
            }
        }
    }

    /**
     * 根据已完成进度判断是否完成某项成就-包含成就目标
     * @param targetId 成就目标
     * @param desc 成就描述
     * @param role 角色
     */
    public static void checkIfCompleteWithTarget(int targetId,String desc, Role role){
        for(Achievement achievement : Achievement.values()) {
            if (achievement.getDesc().equals(desc) && targetId==achievement.getTargetId()) {
                //如果计数大于对应id对应的count，则提示已完成成就
                int nowCount = role.getAchievementCountMap().get(achievement.getId());
                int targetCount = achievement.getCount();
                if(nowCount>=targetCount){
                    role.getAchievementBo().getAchievementHashMap().put(achievement.getId(),true);
                }
            }
        }
    }

    /**
     * 检查是否完成某一系列任务
     * @param desc 成就描述
     * @param role 角色
     */
    public static void completSeriaAchievement(String desc,Role role){
        for(Achievement achievement : Achievement.values()) {
            if (achievement.getDesc().equals(desc)) {
                Integer[] strings = AchieveResource.getAchieveStaticHashMap().get(achievement.getId()).getSerial();
                for(int i=0;i<strings.length;i++){
                    if(role.getAchievementBo().getAchievementHashMap().get(strings[i])==false){
                        return;
                    }
                }
                role.getAchievementBo().getAchievementHashMap().put(achievement.getId(),true);
            }
        }
    }


    /**
     * 查询当前所有成就是否已完成
     * @param role 角色
     * @return 信息提示
     */
    public String getAchievementList(Role role){
        StringBuilder result= new StringBuilder();
        for(Integer achieveId : AchieveResource.getAchieveStaticHashMap().keySet()){
            result.append(AchieveResource.getAchieveStaticHashMap().get(achieveId).getDesc()).append("：").
                    append(role.getAchievementBo().getAchievementHashMap().get(achieveId)).append("\n");
        }
        return result.toString();
    }

    /**
     * 获取当前所有成就的完成进度
     * @param role 角色
     * @return 信息提示
     */
    public String getTaskSchedule(Role role){
        StringBuilder stringBuilder = new StringBuilder();
        for(Integer achieveId : role.getAchievementCountMap().keySet()){
            stringBuilder.append(achieveId+":"+role.getAchievementCountMap().get(achieveId)+"\n");
        }
        return stringBuilder.toString();
    }
}
