package com.game.service.assis;

//import com.game.common.InitStaticResource;
import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.entity.Role;
import com.game.entity.store.*;

/**
 * 由于系统是字符串输入名字，但实际操作的key是id，因此需要根据输入先找到对应的id，这就是该类的部分功能
 * 游戏内部方法，与角色输入无关，方便业务逻辑处理时调用
 * @Author andy
 * @create 2020/6/3 20:35
 */
public class AssistService {

    //查找装备id
    public static Integer checkEquipmentId(String equipmentName){
        for (Integer key : EquipmentResource.equipmentStaticHashMap.keySet()) {
            if (equipmentName.equals(EquipmentResource.equipmentStaticHashMap.get(key).getName())) {
                return key;
            }
        }
        return 0;
    }

    //查找技能id
    public static Integer checkSkillId(String skillName){
        for (Integer key : SkillResource.skillStaticHashMap.keySet()) {
            if (skillName.equals(SkillResource.skillStaticHashMap.get(key).getName())) {
                return key;
            }
        }
        return 0;
    }

/*    public static Integer checkSkillType(String skillName){
        for (Integer key : SkillResource.skillStaticHashMap.keySet()) {
            if (skillName.equals(SkillResource.skillStaticHashMap.get(key).getName())) {
                return SkillResource.skillStaticHashMap.get(key).getTypeId();
            }
        }
        return 0;
    }*/

    //查找npc的id
    public static Integer checkNpcId(String npcName,int roleId){
        for (Integer key : NpcResource.npcsStatics.keySet()) {
            if(NpcResource.npcsStatics.get(key).getName().equals(npcName) &&
                    RoleController.roleHashMap.get(roleId).getNowScenesId()==NpcResource.npcsStatics.get(key).getSceneId()) {
                return key;
            }
        }
        return 0;
    }

    //查找怪物动态UUID
    public static String checkMonsterId(String monsterName,int roleId){
        int sceneId = RoleController.roleHashMap.get(roleId).getNowScenesId();
        for (String key : InitGame.scenes.get(sceneId).getMonsterHashMap().keySet()) {
            if (MonsterResource.monstersStatics.get(InitGame.scenes.get(sceneId).getMonsterHashMap().
                    get(key).getMonsterId()).getName().equals(monsterName)){
                return key;
            }
        }
        return "";
    }

    //查找药品id
    public static Integer checkPotionId(String drugName){
        for (Integer key : PotionResource.potionStaticHashMap.keySet()) {
            if (drugName.equals(PotionResource.potionStaticHashMap.get(key).getName())) {
                return key;
            }
        }
        return 0;
    }

    //查找静态场景id
    public static Integer checkSceneId(String sceneName){
        for (Integer key : SceneResource.scenesStatics.keySet()) {
            if (sceneName.equals(SceneResource.scenesStatics.get(key).getName())) {
                return key;
            }
        }
        return 0;
    }

    //查找动态场景id
    public static Integer checkDynSceneId(String sceneName){
        for (Integer key : InitGame.scenes.keySet()) {
            if (sceneName.equals(InitGame.scenes.get(key).getName())) {
                return key;
            }
        }
        return 0;
    }

    //查找药品或装备的id
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
/*    //获取组队列表，无传参，返回列表集合元素信息，集合中为每个teamId
    public static String getTeamIdList(){
        StringBuilder stringBuilder = new StringBuilder("所有的队伍列表如下：");//扩展，队伍列表中加入副本名，每个副本名对应一个BOSS
        for(String teamId : DynamicResource.teamList.keySet()){
            stringBuilder.append(teamId+"; ");
        }
        return stringBuilder.toString();
    }*/

    // 物品数量始终大于零，若小于零，归零
    public static int checkNum(int num){
        if(num<0){
            return 0;
        }
        return num;
    }

    //检查与怪物或玩家/NPC的距离是否在可攻击、谈话范围内-demo，怪物的位置可以在场景中随机生成
    public static boolean checkDistance(int roleId,String monsterId){
        Role role = RoleController.roleHashMap.get(roleId);
        int[] self = role.getPosition();
        int[] monster = {30,30};//目前测试，均假设为一个位置
        if(getDistance(self,monster)<= Const.Max_OPT_DISTANCE){
            return true;
        }
        return false;
    }

    public static double getDistance(int[] self,int[] other){
        return Math.sqrt(Math.pow(self[0]-other[0],2)+Math.pow(self[1]-other[1],2));
    }
}
