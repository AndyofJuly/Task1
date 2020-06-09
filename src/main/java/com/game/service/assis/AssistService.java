package com.game.service.assis;

//import com.game.common.InitStaticResource;
import com.game.controller.FunctionService;
import com.game.entity.store.*;

/**
 * 由于系统是字符串输入名字，但实际操作的key是id，因此需要根据输入先找到对应的id，这就是该类的部分功能
 * 游戏内部方法，与角色输入无关，方便业务逻辑处理时调用
 * @Author andy
 * @create 2020/6/3 20:35
 */
public class AssistService {

    public static Integer checkEquipmentId(String equipmentName){
        for (Integer key : EquipmentResource.equipmentStaticHashMap.keySet()) {
            if (equipmentName.equals(EquipmentResource.equipmentStaticHashMap.get(key).getName())) {
                return key;
            }
        }
        return 0;
    }

    public static Integer checkSkillId(String skillName){
        for (Integer key : SkillResource.skillStaticHashMap.keySet()) {
            if (skillName.equals(SkillResource.skillStaticHashMap.get(key).getName())) {
                return key;
            }
        }
        return 0;
    }

    public static Integer checkSkillType(String skillName){
        for (Integer key : SkillResource.skillStaticHashMap.keySet()) {
            if (skillName.equals(SkillResource.skillStaticHashMap.get(key).getName())) {
                return SkillResource.skillStaticHashMap.get(key).getTypeId();
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

    //通过职业名字查找职业id
    public static Integer checkCareerId(String careerName){
        for (Integer key : CareerResource.careerStaticHashMap.keySet()) {
            if(careerName.equals(CareerResource.careerStaticHashMap.get(key).getName())){
                return key;
            }
        }
        return 0;
    }

/*    //通过副本id查找bossid。后续可以调用根据怪物id得到怪物名的方法
    public static Integer checkBossName(int dungeonsId){
        for (Integer key : DungeonsResource.dungeonsStaticHashMap.keySet()) {
            if(dungeonsId==DungeonsResource.dungeonsStaticHashMap.get(key).getId()){
                return key;
            }
        }
        return 0;
    }*/

    //----
    //获取组队列表，无传参，返回列表集合元素信息，集合中为每个teamId
    public static String getTeamIdList(){
        StringBuilder stringBuilder = new StringBuilder("所有的队伍列表如下：");//扩展，队伍列表中加入副本名，每个副本名对应一个BOSS
        for(String teamId : DynamicResource.teamList.keySet()){
            stringBuilder.append(teamId+"; ");
        }
        return stringBuilder.toString();
    }

    //获取队伍中的角色列表，传参为teamId，返回角色列表集合元素信息，集合中为每个roleName
    public static String getRoleList(String teamId){
        StringBuilder stringBuilder = new StringBuilder("队伍中的角色有：");
        for(Integer roleId : DynamicResource.teamList.get(teamId).getRoleList()){
            stringBuilder.append(FunctionService.roleHashMap.get(roleId).getName()+" ");
        }
        return stringBuilder.toString();
    }

}
