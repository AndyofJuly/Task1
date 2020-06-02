package com.game.entity;

import com.game.controller.FunctionService;
import com.game.entity.excel.EquipmentStatic;
import com.game.entity.excel.PotionStatic;
import com.game.entity.excel.RoleStatic;
import com.game.entity.excel.SkillStatic;

import java.util.HashMap;

/**
 * 角色类
 * @Author andy
 * @create 2020/5/11 14:58
 */
public class Role {
    private int id;
    private String name;
    private int nowScenesId;
    private int alive;
    private RoleStatic roleStatic;
    private MyPackage myPackage;
    private int levelId;

    private int hp = 50;
    private int mp = 50;
    private int atk = 2;

    private HashMap<Integer,Skill> skillHashMap = new HashMap<Integer,Skill>();
    //如果后续装备类型变多了，还是需要集合
    //private HashMap<Integer,EquipmentStatic> equipmentStaticHashMaps = new HashMap<Integer,EquipmentStatic>();
    private HashMap<Integer,Equipment> equipmentHashMap = new HashMap<Integer,Equipment>();
    //药品应该可以不需要集合，直接从背包里拿，然后使用
    private HashMap<Integer,PotionStatic> potionStaticHashMap = new HashMap<Integer,PotionStatic>();


    public Role(String name, int nowScenesId) {
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

    public RoleStatic getRoleStatic() {
        return roleStatic;
    }

    public void setRoleStatic(RoleStatic roleStatic) {
        this.roleStatic = roleStatic;
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

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }
}
