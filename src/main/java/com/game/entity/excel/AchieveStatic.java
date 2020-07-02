package com.game.entity.excel;

/**
 * @Author andy
 * @create 2020/7/1 15:21
 */
public class AchieveStatic {
    //成就id
    private int id;
    //成就说明
    private String msg;

    public AchieveStatic(int id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
