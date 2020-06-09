package com.game.entity;

import com.game.entity.excel.PotionStatic;

import java.util.HashMap;

/**
 * 角色类
 * @Author andy
 * @create 2020/5/11 14:58
 */
public class Role {
    // 角色id-静态属性
    private int id;
    // 角色名
    private String name;
    // 角色所在场景
    private int nowScenesId;
    // 角色存活状态
    private int alive;
    // 角色包裹
    private MyPackage myPackage;
    // 角色职业id
    private int careerId;
    //角色血量
    private int hp = 50;
    //角色蓝量
    private int mp = 50;
    //角色攻击力
    private int atk = 10;
    //角色的金钱，初始送100
    private int money = 100;
    //角色防御力
    private int def = 3;

    //角色学会的技能
    private HashMap<Integer,Skill> skillHashMap = new HashMap<Integer,Skill>();
    //如果后续装备类型变多了，还是需要集合
    //角色装备栏
    private HashMap<Integer,Equipment> equipmentHashMap = new HashMap<Integer,Equipment>();
    //药品应该可以不需要集合，直接从背包里拿，然后使用
    // 角色手中药品
    private HashMap<Integer,PotionStatic> potionStaticHashMap = new HashMap<Integer,PotionStatic>();

    public Role(int id, String name, int nowScenesId) {
        this.id = id;
        this.name = name;
        this.nowScenesId = nowScenesId;
        this.alive = 1;
    }

    public Role(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNowScenesId() {
        return nowScenesId;
    }

    public void setNowScenesId(int nowScenesId) {
        this.nowScenesId = nowScenesId;
    }

    public int getAlive() {
        return alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public HashMap<Integer, Skill> getSkillHashMap() {
        return skillHashMap;
    }

    public void setSkillHashMap(HashMap<Integer, Skill> skillHashMap) {
        this.skillHashMap = skillHashMap;
    }

    public HashMap<Integer, Equipment> getEquipmentHashMap() {
        return equipmentHashMap;
    }

    public void setEquipmentHashMap(HashMap<Integer, Equipment> equipmentHashMap) {
        this.equipmentHashMap = equipmentHashMap;
    }

    public HashMap<Integer, PotionStatic> getPotionStaticHashMap() {
        return potionStaticHashMap;
    }

    public void setPotionStaticHashMap(HashMap<Integer, PotionStatic> potionStaticHashMap) {
        this.potionStaticHashMap = potionStaticHashMap;
    }

    public MyPackage getMyPackage() {
        return myPackage;
    }

    public void setMyPackage(MyPackage myPackage) {
        this.myPackage = myPackage;
    }

    public int getCareerId() {
        return careerId;
    }

    public void setCareerId(int careerId) {
        this.careerId = careerId;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
