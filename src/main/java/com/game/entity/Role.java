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

    private int hp = 50;
    private int mp = 50;
    private int atk = 2;

    //应该是集合
    private HashMap<Integer,SkillStatic> skillStaticHashMap = new HashMap<Integer,SkillStatic>();
    private HashMap<Integer,EquipmentStatic> equipmentStaticHashMap = new HashMap<Integer,EquipmentStatic>();
    private HashMap<Integer,PotionStatic> potionStaticHashMap = new HashMap<Integer,PotionStatic>();
    private MyPackage myPackage;

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

    public HashMap<Integer, SkillStatic> getSkillStaticHashMap() {
        return skillStaticHashMap;
    }

    public void setSkillStaticHashMap(HashMap<Integer, SkillStatic> skillStaticHashMap) {
        this.skillStaticHashMap = skillStaticHashMap;
    }

    public HashMap<Integer, EquipmentStatic> getEquipmentStaticHashMap() {
        return equipmentStaticHashMap;
    }

    public void setEquipmentStaticHashMap(HashMap<Integer, EquipmentStatic> equipmentStaticHashMap) {
        this.equipmentStaticHashMap = equipmentStaticHashMap;
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
}
