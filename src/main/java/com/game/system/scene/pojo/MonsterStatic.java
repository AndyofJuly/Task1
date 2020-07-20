package com.game.system.scene.pojo;

/**
 * 怪物类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/5/20 20:45
 */
public class MonsterStatic {
    /** 怪物id */
    private Integer id;
    /** 怪物名 */
    private String name;
    /** 怪物血量 */
    private Integer hp;
    /** 怪物攻击力 */
    private Integer atk;
    /** 位置坐标 */
    private Integer[] position;
    /** 怪物物理防御力 */
    private Integer def;

    public MonsterStatic(Integer id, String name, Integer hp, Integer atk, Integer[] position, Integer def) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        //this.sceneId = sceneId;
        this.atk = atk;
        this.position = position;
        this.def = def;
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

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getAtk() {
        return atk;
    }

    public void setAtk(Integer atk) {
        this.atk = atk;
    }

    public Integer[] getPosition() {
        return position;
    }

    public void setPosition(Integer[] position) {
        this.position = position;
    }

    public Integer getDef() {
        return def;
    }

    public void setDef(Integer def) {
        this.def = def;
    }
}
