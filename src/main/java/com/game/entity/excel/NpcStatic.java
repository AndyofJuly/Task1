package com.game.entity.excel;

/**
 * NPC类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/5/20 20:47
 */
public class NpcStatic {
    private int id;
    private String name;
    private String words;
    private int sceneId;

    public NpcStatic(int id, String name,String words,int sceneId) {
        this.id = id;
        this.name = name;
        this.words = words;
        this.sceneId = sceneId;
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
}
