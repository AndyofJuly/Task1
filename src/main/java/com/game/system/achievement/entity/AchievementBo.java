package com.game.system.achievement.entity;

import com.game.system.achievement.Achievement;

import java.util.HashMap;

/**
 * 成就系统
 * @Author andy
 * @create 2020/6/28 7:56
 */
public class AchievementBo {

    /** 成就id-同静态资源id*/
    private Integer id;
    /** 每项成就及对应完成的数量，Key为成就id，value为数量 */
    private HashMap<Integer,Integer> achievementCountMap = new HashMap<>();
    /** 成就id和完成情况*/
    private HashMap<Integer,Boolean> achievementHashMap = new HashMap<>();

    public AchievementBo() {
        for(Achievement achievement : Achievement.values()){
            achievementHashMap.put(achievement.getId(),false);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HashMap<Integer, Integer> getAchievementCountMap() {
        return achievementCountMap;
    }

    public void setAchievementCountMap(HashMap<Integer, Integer> achievementCountMap) {
        this.achievementCountMap = achievementCountMap;
    }

    public HashMap<Integer, Boolean> getAchievementHashMap() {
        return achievementHashMap;
    }

    public void setAchievementHashMap(HashMap<Integer, Boolean> achievementHashMap) {
        this.achievementHashMap = achievementHashMap;
    }
}
