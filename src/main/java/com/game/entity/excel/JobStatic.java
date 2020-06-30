package com.game.entity.excel;

/**
 * 公会职务
 * @Author andy
 * @create 2020/6/30 11:37
 */
public class JobStatic {
    //职务id
    private int id;
    //职务名
    private String name;
    //职务权限级别
    private int grade;

    public JobStatic(int id, String name, int grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
