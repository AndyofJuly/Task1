package com.game.system.assist;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.scene.pojo.Monster;
import com.game.system.bag.pojo.EquipmentResource;
import com.game.system.bag.pojo.PotionResource;
import com.game.system.role.pojo.Role;
import com.game.system.scene.pojo.NpcResource;
import com.game.system.scene.pojo.Scene;
import com.game.system.scene.pojo.SceneResource;

import java.util.HashSet;
import java.util.Random;

/**
 * 游戏内部方法，与角色输入无关，方便业务逻辑处理时调用
 * @Author andy
 * @create 2020/6/3 20:35
 */
public class AssistService {

/*    //查找成就id
    public static Integer checkAchieveId(String desc){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            if(desc.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc())){
                return achievId;
            }
        }
        return 0;
    }*/


    //查找npc的id-->验证是否在同一场景
    public static boolean checkNpcId(int npcId,Role role){
        int nowScenesId = role.getNowScenesId();
        int npcScenesId = NpcResource.getNpcsStatics().get(npcId).getSceneId();
        return nowScenesId==npcScenesId;
    }

    //查找怪物动态UUID
    public static String checkMonsterId(int monsterId,Role role){
        int sceneId = role.getNowScenesId();
        Scene scene = GlobalInfo.getScenes().get(sceneId);
        for (String key : scene.getMonsterHashMap().keySet()) {
            Integer monsterStaticId = scene.getMonsterHashMap().get(key).getMonsterId();
            if (monsterStaticId==monsterId){
                return key;
            }
        }
        return "";
    }

    //查找静态场景id，传参为外部场景名
    public static Integer checkSceneId(String sceneName){
        for (Integer key : SceneResource.getScenesStatics().keySet()) {
            if (sceneName.equals(SceneResource.getScenesStatics().get(key).getName())) {
                return key;
            }
        }
        return 0;
    }

    //查找药品或装备的id
    public static Integer checkGoodsId(String goodsName){
        for (Integer key : PotionResource.getPotionStaticHashMap().keySet()) {
            if (goodsName.equals(AssistService.getPotionName(key))) {
                return key;
            }
        }
        for (Integer key : EquipmentResource.getEquipmentStaticHashMap().keySet()) {
            if (goodsName.equals(AssistService.getEquipmentName(key))) {
                return key;
            }
        }
        return 0;
    }

    //检查与怪物或玩家/NPC的距离是否在可攻击、谈话范围内-demo，怪物的位置可以在场景中随机生成
    public static boolean checkDistance(Role role, Monster monster){
        int[] self = role.getPosition();
        int[] other = monster.getPosition();//目前测试，均假设为一个位置
        if(getDistance(self,other)<= Const.Max_OPT_DISTANCE){
            return true;
        }
        return false;
    }

    public static double getDistance(int[] self,int[] other){
        return Math.sqrt(Math.pow(self[0]-other[0],2)+Math.pow(self[1]-other[1],2));
    }

    public static int[] randSort(int size){
        int[] arr = new int[size];
        for(int i=0;i<size;i++){
            arr[i] = i;
        }
        Random random = new Random();
        for(int i=0;i<arr.length;i++){
            int p = random.nextInt(i+1);
            int tmp = arr[i];
            arr[i] = arr[p];
            arr[p] = tmp;
        }
        return arr;
    }


    //id生成-待修改
    private static HashSet<Integer> teamSet = new HashSet<>();
    private static HashSet<Integer> sceneSet = new HashSet<>();
    private static int unionId;

    //如果游戏中生成的队伍数量应该小于10000
    public static String generateTeamId(){
        Integer tmp=0;
        int size = teamSet.size();
        Random ran = new Random();
        while (size+1>teamSet.size()){
            tmp = ran.nextInt(Const.Max_ID);
            teamSet.add(tmp);
        }
        return tmp+"";
    }

    //如果游戏中生成的队伍数量应该小于10000
    public static int generateSceneId(){
        Integer tmp=0;
        int size = sceneSet.size();
        Random ran = new Random();
        while (size+1>sceneSet.size()){
            tmp = ran.nextInt(Const.Max_ID);
            sceneSet.add(tmp);
        }
        return tmp;
    }

    public static int generateUnionId(){
        return ++unionId;
    }


    //查找对应静态资源

    public static String getPotionName(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getName();
    }
    //获得价格
    public static int getPotionPrice(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getPrice();
    }

    public static int getPotionAddHp(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getAddHp();
    }

    public static int getPotionAddMp(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getAddMp();
    }

    //获得装备名
    public static String getEquipmentName(int key){
        return EquipmentResource.getEquipmentStaticHashMap().get(key).getName();
    }
    //获得价格
    public static int getEquipmentPrice(int key){
        return EquipmentResource.getEquipmentStaticHashMap().get(key).getPrice();
    }
    //获得装备攻击力
    public static int getEquipmentAtk(int key){
        return EquipmentResource.getEquipmentStaticHashMap().get(key).getAtk();
    }
    //获得装备耐久
    public static int getEquipmentDura(int key){
        return EquipmentResource.getEquipmentStaticHashMap().get(key).getDurability();
    }








    //监听-待修改
    public static boolean monsterIsDead;
    //监听怪物是否已被打败，通知全局
    public static boolean isDead(){
        if(AssistService.monsterIsDead==true){
            return true;
        }
        return false;
    }

    public static String mesg(){
        return Const.MONSTER_MESSEAGE;
    }

    public static void reset(){
        monsterIsDead=false;
    }

    public static boolean isMonsterIsDead() {
        return monsterIsDead;
    }

    public static void setMonsterIsDead(boolean monsterIsDead) {
        AssistService.monsterIsDead = monsterIsDead;
    }





}
