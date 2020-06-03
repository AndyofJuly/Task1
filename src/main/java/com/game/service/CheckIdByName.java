package com.game.service;

import com.game.common.InitStaticResource;
import com.game.controller.FunctionService;

/**
 * 由于系统是字符串输入名字，但实际操作的key是id，因此需要根据输入先找到对应的id，这就是该类的功能
 * @Author andy
 * @create 2020/6/3 20:35
 */
public class CheckIdByName {

    public static Integer checkEquipmentId(String equipmentName){
        for (Integer key : InitStaticResource.equipmentStaticHashMap.keySet()) {
            if (equipmentName.equals(InitStaticResource.equipmentStaticHashMap.get(key).getName())) {
                return key;
            }
        }
        return 0;
    }

    public static Integer checkSkillId(String skillName){
        for (Integer key1 : InitStaticResource.skillStaticHashMap.keySet()) {
            if (skillName.equals(InitStaticResource.skillStaticHashMap.get(key1).getName())) {
                return key1;
            }
        }
        return 0;
    }

    public static Integer checkNpcId(String npcName,int roleId){
        for (Integer key : InitStaticResource.npcsStatics.keySet()) {
            if(InitStaticResource.npcsStatics.get(key).getName().equals(npcName) &&
                    FunctionService.roleHashMap.get(roleId).getNowScenesId()==InitStaticResource.npcsStatics.get(key).getSceneId()) {
                return key;
            }
        }
        return 0;
    }

    public static Integer checkMonsterId(String monsterName,int roleId){
        for (Integer key : InitStaticResource.monstersStatics.keySet()) {
            if (InitStaticResource.monstersStatics.get(key).getName().equals(monsterName) &&
                    FunctionService.roleHashMap.get(roleId).getNowScenesId() == InitStaticResource.monstersStatics.get(key).getSceneId()) {
                return key;
            }
        }
        return 0;
    }

    public static Integer checkPotionId(String drugName){
        for (Integer key : InitStaticResource.potionStaticHashMap.keySet()) {
            if (drugName.equals(InitStaticResource.potionStaticHashMap.get(key).getName())) {
                return key;
            }
        }
        return 0;
    }
}
