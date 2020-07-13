package com.game.test.selfevent;

/**
 * @Author andy
 * @create 2020/7/11 18:37
 */
public class MyEvent {

    //成就系统事件-与excel表相对应
    private int id;
    private String desc;
    private int targetId;
    private int count;
    private int[] serial;

    public MyEvent(int id, String desc, int targetId, int count, int[] serial) {
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

    public int[] getSerial() {
        return serial;
    }

    public void setSerial(int[] serial) {
        this.serial = serial;
    }
}
