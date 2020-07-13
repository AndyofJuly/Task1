package com.game.system.scene.pojo;

/**
 * NPC类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/5/20 20:47
 */
public class NpcStatic {
    // NPC的id
    private int id;
    // NPC的名字
    private String name;
    // NPC的对话
    private String words;
    // NPC所在场景id
    private int sceneId;
    // 位置坐标
    private int[] position;

    public NpcStatic(int id, String name,String words,int sceneId,int[] position) {
        this.id = id;
        this.name = name;
        this.words = words;
        this.sceneId = sceneId;
        this.position = position;
    }

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
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

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }
}
