package com.game.system.scene.pojo;

/**
 * NPC类-假设同类npc只有一个，直接实例化静态资源中的npc
 * @Author andy
 * @create 2020/5/11 14:58
 */
public class Npc {
    /** NPC存活状态，1表示存活，0表示不存活 */
    private Integer alive;
    /** 资源类中的静态id，同时作为实体类id */
    private Integer npcId;
    /** NPC的名字 */
    private String name;
    /** NPC的对话 */
    private String words;
    /** NPC所在场景id */
    private Integer sceneId;
    /** 位置坐标 */
    private Integer[] position;


    public Npc(Integer npcId) {
        this.npcId = npcId;
        this.sceneId = NpcResource.getNpcsStatics().get(npcId).getSceneId();
        this.words = NpcResource.getNpcsStatics().get(npcId).getWords();
        this.name = NpcResource.getNpcsStatics().get(npcId).getName();
        this.position = NpcResource.getNpcsStatics().get(npcId).getPosition();
        this.alive = 1;
    }

    public Integer getAlive() {
        return alive;
    }

    public void setAlive(Integer alive) {
        this.alive = alive;
    }

    public Integer getNpcId() {
        return npcId;
    }

    public void setNpcId(Integer npcId) {
        this.npcId = npcId;
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
