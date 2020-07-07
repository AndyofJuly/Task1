package com.game.entity.excel;

/**
 * @Author andy
 * @create 2020/7/1 15:21
 */
public class AchieveStatic {
    //成就id
    private int id;
    //成就说明
    private String desc;
    //目标
    private int targetId;
    //完成所需数量
    private int count;
    //系列任务
    private String[] serial;

    public AchieveStatic(int id, String desc, int targetId, int count,String[] serial) {
        this.id = id;
        this.desc = desc;
        this.targetId = targetId;
        this.count = count;
        this.serial = serial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String[] getSerial() {
        return serial;
    }

    public void setSerial(String[] serial) {
        this.serial = serial;
    }
}
