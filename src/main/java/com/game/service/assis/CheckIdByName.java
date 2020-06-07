package com.game.service.assis;

//import com.game.common.InitStaticResource;
import com.game.controller.FunctionService;
import com.game.entity.store.*;

/**
 * 由于系统是字符串输入名字，但实际操作的key是id，因此需要根据输入先找到对应的id，这就是该类的功能
 * @Author andy
 * @create 2020/6/3 20:35
 */
public class CheckIdByName {

    public static Integer checkEquipmentId(String equipmentName){
        for (Integer key : EquipmentResource.equipmentStaticHashMap.keySet()) {
            if (equipmentName.equals(EquipmentResource.equipmentStaticHashMap.get(key).getName())) {
                return key;
            }
        }
        return 0;
    }

    public static Integer checkSkillId(String skillName){
        for (Integer key1 : SkillResource.skillStaticHashMap.keySet()) {
            if (skillName.equals(SkillResource.skillStaticHashMap.get(key1).getName())) {
                return key1;
            }
        }
        return 0;
    }

    public static Integer checkNpcId(String npcName,int roleId){
        for (Integer key : NpcResource.npcsStatics.keySet()) {
            if(NpcResource.npcsStatics.get(key).getName().equals(npcName) &&
                    FunctionService.roleHashMap.get(roleId).getNowScenesId()==NpcResource.npcsStatics.get(key).getSceneId()) {
                return key;
            }
        }
        return 0;
    }

    public static String checkMonsterId(String monsterName,int roleId){
        int sceneId = FunctionService.roleHashMap.get(roleId).getNowScenesId();
        for (String key : InitGame.scenes.get(sceneId).getMonsterHashMap().keySet()) {
            if (MonsterResource.monstersStatics.get(InitGame.scenes.get(sceneId).getMonsterHashMap().
                    get(key).getMonsterId()).getName().equals(monsterName)){
                return key;
            }
        }
        return "";
    }

    public static Integer checkPotionId(String drugName){
        for (Integer key : PotionResource.potionStaticHashMap.keySet()) {
            if (drugName.equals(PotionResource.potionStaticHashMap.get(key).getName())) {
                return key;
            }
        }
        return 0;
    }

    public static Integer checkSceneId(String sceneName){
        for (Integer key : SceneResource.scenesStatics.keySet()) {
            if (sceneName.equals(SceneResource.scenesStatics.get(key).getName())) {
                return key;
            }
        }
        return 0;
    }

    public static Integer checkGoodsId(String goodsName){
        for (Integer key : PotionResource.potionStaticHashMap.keySet()) {
            if (goodsName.equals(PotionResource.potionStaticHashMap.get(key).getName())) {
                return key;
            }
        }
        for (Integer key : EquipmentResource.equipmentStaticHashMap.keySet()) {
            if (goodsName.equals(EquipmentResource.equipmentStaticHashMap.get(key).getName())) {
                return key;
            }
        }
        return 0;
    }
}
