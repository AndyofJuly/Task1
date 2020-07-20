package com.game.system.achievement.pojo;

import java.util.HashMap;

/**
 * 成就系统
 * @Author andy
 * @create 2020/6/28 7:56
 */
public class AchievementBo {

    //任务完成情况；id和是否已完成，对应数据库0表示未完成，1表示已完成
    //击杀怪物id和数量
    /** 同静态资源id*/
    private Integer id;
    /** 击杀怪物记录*/
    private HashMap<Integer,Integer> killMonsterCountMap = new HashMap<>();
    /** 成就id和完成情况*/
    private HashMap<Integer,Boolean> achievementHashMap = new HashMap<>();
    /** 添加好友数量*/
    private int countFriend;

    public AchievementBo() {
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            achievementHashMap.put(achievId,false);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HashMap<Integer, Integer> getKillMonsterCountMap() {
        return killMonsterCountMap;
    }

    public void setKillMonsterCountMap(HashMap<Integer, Integer> killMonsterCountMap) {
        this.killMonsterCountMap = killMonsterCountMap;
    }

    public HashMap<Integer, Boolean> getAchievementHashMap() {
        return achievementHashMap;
    }

    public void setAchievementHashMap(HashMap<Integer, Boolean> achievementHashMap) {
        this.achievementHashMap = achievementHashMap;
    }

    public int getCountFriend() {
        return countFriend;
    }

    public void setCountFriend(int countFriend) {
        this.countFriend = countFriend;
    }
}
