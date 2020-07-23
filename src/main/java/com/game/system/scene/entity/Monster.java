package com.game.system.scene.entity;

/**
 * 怪物类
 * @Author andy
 * @create 2020/5/12 22:32
 */
public class Monster{
    /** 具体对象的id，每个怪物的id不相同 */
    private String id;
    /** 怪物存活状态，1表示存活，0表示被消灭 */
    private Integer alive;
    /** 资源类中的静态id */
    private Integer monsterId;
    /** 怪物名称 */
    private String monsterName;
    /** 怪物血量 */
    private Integer monsterHp;
    /** 怪物防御力 */
    private Integer monsterDef;
    /** 怪物位置 */
    private Integer[] position;
    /** 怪物锁定的目标 */
    private Integer atkTargetId;
    /** 怪物攻击力 */
    private Integer atk;
    /** 怪物所在场景 */
    private Integer sceneId;

    public Monster(String id, Integer monsterId) {
        this.id = id;
        this.monsterId = monsterId;
        this.monsterHp = MonsterResource.getMonstersStatics().get(this.monsterId).getHp();
        this.monsterDef = MonsterResource.getMonstersStatics().get(this.monsterId).getDef();
        this.position = MonsterResource.getMonstersStatics().get(this.monsterId).getPosition();
        this.atk = MonsterResource.getMonstersStatics().get(this.monsterId).getAtk();
        this.monsterName = MonsterResource.getMonstersStatics().get(this.monsterId).getName();
        this.atkTargetId = 0;
        this.alive = 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAlive() {
        return alive;
    }

    public void setAlive(Integer alive) {
        this.alive = alive;
    }

    public Integer getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(Integer monsterId) {
        this.monsterId = monsterId;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public Integer getMonsterHp() {
        return monsterHp;
    }

    public void setMonsterHp(Integer monsterHp) {
        this.monsterHp = monsterHp;
    }

    public Integer getMonsterDef() {
        return monsterDef;
    }

    public void setMonsterDef(Integer monsterDef) {
        this.monsterDef = monsterDef;
    }

    public Integer[] getPosition() {
        return position;
    }

    public void setPosition(Integer[] position) {
        this.position = position;
    }

    public Integer getAtkTargetId() {
        return atkTargetId;
    }

    public void setAtkTargetId(Integer atkTargetId) {
        this.atkTargetId = atkTargetId;
    }

    public Integer getAtk() {
        return atk;
    }

    public void setAtk(Integer atk) {
        this.atk = atk;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

}
