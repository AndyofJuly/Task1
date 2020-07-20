package com.game.system.union.pojo;

/**
 * 公会职务
 * @Author andy
 * @create 2020/6/30 11:37
 */
public class JobStatic {
    /** 职务id */
    private Integer id;
    /** 职务名 */
    private String name;
    /** 职务权限级别 */
    private Integer grade;

    public JobStatic(Integer id, String name, Integer grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}
