package com.game.entity.vo;

import com.game.entity.store.AchieveResource;

import java.util.HashMap;

/**
 * 成就系统
 * @Author andy
 * @create 2020/6/28 7:56
 */
public class AchievementVo {

    //任务完成情况；id和是否已完成，对应数据库0表示未完成，1表示已完成
    private HashMap<Integer,Boolean> achievementHashMap = new HashMap<>();

    public AchievementVo() {
        for(int i = 0; i< AchieveResource.getAchieveStaticHashMap().size(); i++){
            achievementHashMap.put(i,false);
        }
    }

    //计数并判断是否满足数量要求，满足则设为true;
    private int slayConut = 0;

    //计数
    public int getSlayConut() {
        return slayConut;
    }

    public void setSlayConut(int slayConut) {
        this.slayConut = this.slayConut+slayConut;
    }

    public HashMap<Integer, Boolean> getAchievementHashMap() {
        return achievementHashMap;
    }

    public void setAchievementHashMap(HashMap<Integer, Boolean> achievementHashMap) {
        this.achievementHashMap = achievementHashMap;
    }
}
