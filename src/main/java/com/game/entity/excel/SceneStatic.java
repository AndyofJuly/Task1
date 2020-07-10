package com.game.entity.excel;

/**
 * 场景类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/5/20 20:47
 */
public class SceneStatic {
    // 场景id
    private int id;
    // 场景名
    private String name;
    // 场景间的关系
    private int[] sceneRelation;
    // 场景包含的NPC-可以改为int
    private int[] npcId;
    // 场景包含的怪物
    private int[] monsterId;

    public SceneStatic(int id, String name, int[] sceneRelation, int[] npcId, int[] monsterId) {
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

    public int[] getSceneRelation() {
        return sceneRelation;
    }

    public void setSceneRelation(int[] sceneRelation) {
        this.sceneRelation = sceneRelation;
    }

    public int[] getNpcId() {
        return npcId;
    }

    public void setNpcId(int[] npcId) {
        this.npcId = npcId;
    }

    public int[] getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(int[] monsterId) {
        this.monsterId = monsterId;
    }
}
