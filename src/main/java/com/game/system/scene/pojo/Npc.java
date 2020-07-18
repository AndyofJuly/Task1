package com.game.system.scene.pojo;

/**
 * NPC类-假设同类npc只有一个，直接实例化静态资源中的npc
 * @Author andy
 * @create 2020/5/11 14:58
 */
public class Npc {
    /** NPC存活状态，1表示存活，0表示不存活 */
    private int alive;
    /** 资源类中的静态id，同时作为实体类id */
    private int npcId;
    /** NPC的名字 */
    private String name;
    /** NPC的对话 */
    private String words;
    /** NPC所在场景id */
    private int sceneId;
    /** 位置坐标 */
    private int[] position;


    public Npc(int npcId) {
        this.npcId = npcId;
        this.sceneId = NpcResource.getNpcsStatics().get(npcId).getSceneId();
        this.words = NpcResource.getNpcsStatics().get(npcId).getWords();
        this.name = NpcResource.getNpcsStatics().get(npcId).getName();
        this.position = NpcResource.getNpcsStatics().get(npcId).getPosition();
        this.alive = 1;
    }

    public int getAlive() {
        return alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }

    public int getNpcId() {
        return npcId;
    }

    public void setNpcId(int npcId) {
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

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }
}
