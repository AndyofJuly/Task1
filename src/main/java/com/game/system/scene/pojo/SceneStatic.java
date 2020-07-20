package com.game.system.scene.pojo;

/**
 * 场景类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/5/20 20:47
 */
public class SceneStatic {
    /** 场景id */
    private Integer id;
    /** 场景名 */
    private String name;
    /** 场景间的关系 */
    private Integer[] sceneRelation;
    /** 场景包含的NPC */
    private Integer[] npcId;
    /** 场景包含的怪物 */
    private Integer[] monsterId;

    public SceneStatic(Integer id, String name, Integer[] sceneRelation, Integer[] npcId, Integer[] monsterId) {
        this.id = id;
        this.name = name;
        this.sceneRelation = sceneRelation;
        this.npcId = npcId;
        this.monsterId = monsterId;
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

    public Integer[] getSceneRelation() {
        return sceneRelation;
    }

    public void setSceneRelation(Integer[] sceneRelation) {
        this.sceneRelation = sceneRelation;
    }

    public Integer[] getNpcId() {
        return npcId;
    }

    public void setNpcId(Integer[] npcId) {
        this.npcId = npcId;
    }

    public Integer[] getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(Integer[] monsterId) {
        this.monsterId = monsterId;
    }
}
