package com.game.entity.vo;

import com.game.entity.Equipment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/7/6 14:49
 */
public class BodyEquipVo {
    //武器id
    private int weaponId;
    //手环id
    private int circletId;
    //项链id
    private int necklaceId;
    //身装id
    private int bodyId;
    //头饰id
    private int headId;
    //鞋子id
    private int footId;
    //id集合-不为零
    //private HashMap<Integer, Equipment> equipmentHashMap = new HashMap<Integer,Equipment>();

    public BodyEquipVo(int weaponId, int circletId, int necklaceId, int bodyId, int headId, int footId) {
        this.weaponId = weaponId;
        this.circletId = circletId;
        this.necklaceId = necklaceId;
        this.bodyId = bodyId;
        this.headId = headId;
        this.footId = footId;
    }

    public int getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }

    public int getCircletId() {
        return circletId;
    }

    public void setCircletId(int circletId) {
        this.circletId = circletId;
    }

    public int getNecklaceId() {
        return necklaceId;
    }

    public void setNecklaceId(int necklaceId) {
        this.necklaceId = necklaceId;
    }

    public int getBodyId() {
        return bodyId;
    }

    public void setBodyId(int bodyId) {
        this.bodyId = bodyId;
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }

    public int getFootId() {
        return footId;
    }

    public void setFootId(int footId) {
        this.footId = footId;
    }

/*    public HashMap<Integer, Equipment> getEquipmentHashMap() {
*//*        setEquip(weaponId);
        setEquip(circletId);
        setEquip(necklaceId);
        setEquip(bodyId);
        setEquip(headId);
        setEquip(footId);*//*
        return equipmentHashMap;
    }*/

/*    private void setEquip(int id){
        if(id!=0){
            Equipment equipment= new Equipment(id,100);
            equipmentHashMap.put(id,equipment);
        }
    }*/
}
