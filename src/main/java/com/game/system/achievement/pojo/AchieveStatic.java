package com.game.system.achievement.pojo;

/**
 * @Author andy
 * @create 2020/7/1 15:21
 */
public class AchieveStatic {
    /** 成就id*/
    private Integer id;
    /** 成就说明*/
    private String desc;
    /** 目标*/
    private Integer targetId;
    /** 完成所需数量*/
    private Integer count;
    /** 系列任务*/
    private Integer[] serial;

    public AchieveStatic(Integer id, String desc, Integer targetId, Integer count,Integer[] serial) {
        this.id = id;
        this.desc = desc;
        this.targetId = targetId;
        this.count = count;
        this.serial = serial;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer[] getSerial() {
        return serial;
    }

    public void setSerial(Integer[] serial) {
        this.serial = serial;
    }
}
