package com.game.system.scene.entity;

/**
 * NPC类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/5/20 20:47
 */
public class NpcStatic {
    /** NPC的id */
    private Integer id;
    /** NPC的名字 */
    private String name;
    /** NPC的对话 */
    private String words;
    /** NPC所在场景id */
    private Integer sceneId;
    /** 位置坐标 */
    private Integer[] position;

    public NpcStatic(Integer id, String name,String words,Integer sceneId,Integer[] position) {
        this.id = id;
        this.name = name;
        this.words = words;
        this.sceneId = sceneId;
        this.position = position;
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

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public Integer[] getPosition() {
        return position;
    }

    public void setPosition(Integer[] position) {
        this.position = position;
    }
}
