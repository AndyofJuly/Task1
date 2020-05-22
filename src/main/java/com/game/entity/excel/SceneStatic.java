package com.game.entity.excel;

/**
 * 场景类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/5/20 20:47
 */
public class SceneStatic {
    private int id;
    private String name;
    private String[] sceneRelation;

    private String[] npcId;
    private String[] monsterId;

    public SceneStatic(int id, String name, String[] sceneRelation, String[] npcId, String[] monsterId) {
        this.id = id;
        this.name = name;
        this.sceneRelation = sceneRelation;
        this.npcId = npcId;
        this.monsterId = monsterId;
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

    public String[] getSceneRelation() {
        return sceneRelation;
    }

    public void setSceneRelation(String[] sceneRelation) {
        this.sceneRelation = sceneRelation;
    }

    public String[] getNpcId() {
        return npcId;
    }

    public void setNpcId(String[] npcId) {
        this.npcId = npcId;
    }

    public String[] getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(String[] monsterId) {
        this.monsterId = monsterId;
    }
}
