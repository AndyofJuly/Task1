package com.game.system.dungeons.pojo;

/**
 * @Author andy
 * @create 2020/6/8 10:40
 */
public class DungeonsStatic {

    /** 副本id */
    private Integer id;
    /** 副本名 */
    private String name;
    /** 副本规定时间 */
    private Integer deadTime;
    /** 副本中的bossId，可扩展为集合 */
    private Integer bossId;
    /** 副本中的场景来源，根据该场景建立出临时场景 */
    private Integer sceneId;

    public DungeonsStatic(Integer id, String name, Integer deadTime, Integer bossId, Integer sceneId) {
        this.id = id;
        this.name = name;
        this.deadTime = deadTime;
        this.bossId = bossId;
        this.sceneId = sceneId;
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

    public Integer getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(Integer deadTime) {
        this.deadTime = deadTime;
    }

    public Integer getBossId() {
        return bossId;
    }

    public void setBossId(Integer bossId) {
        this.bossId = bossId;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }
}
