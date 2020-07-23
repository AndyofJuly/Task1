package com.game.system.gameserver;

import com.game.common.Const;
import com.game.system.bag.entity.Equipment;
import com.game.system.scene.entity.*;
import com.game.system.bag.entity.EquipmentResource;
import com.game.system.bag.entity.PotionResource;
import com.game.system.role.entity.Role;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;

/**
 * 一些常用的协助方法，方便业务逻辑处理时调用
 * @Author andy
 * @create 2020/6/3 20:35
 */
@Service
public class AssistService {

    /**
     * 验证目标npc是否在该场景
     * @param npcId npcId
     * @param role 角色
     * @return 目标npc是否在该场景
     */
    public static boolean isNotInScene(int npcId,Role role){
        int nowScenesId = role.getNowScenesId();
        int npcScenesId = NpcResource.getNpcsStatics().get(npcId).getSceneId();
        return nowScenesId!=npcScenesId;
    }

    /**
     * 根据静态怪物id查找怪物动态UUID
     * @param monsterId 怪物id
     * @param role 角色
     * @return 怪物动态UUID
     */
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

    /**
     * 根据场景名查找静态场景id
     * @param sceneName 场景名
     * @return 静态场景id
     */
    public static Integer checkSceneId(String sceneName){
        for (Integer key : SceneResource.getScenesStatics().keySet()) {
            if (sceneName.equals(SceneResource.getScenesStatics().get(key).getName())) {
                return key;
            }
        }
        return 0;
    }

    /**
     * 检查角色与怪物的距离是否在可攻击范围内
     * @param role 角色
     * @param monster 怪物
     * @return 是否在可攻击范围内
     */
    public static boolean isOutOfInteraction(Role role, Monster monster){
        Integer[] self = role.getPosition();
        Integer[] other = monster.getPosition();
        return !(getDistance(self, other) <= Const.Max_OPT_DISTANCE);
    }

    /**
     * 检查角色与NPC的距离是否在可谈话范围内
     * @param role 角色
     * @param npc npc
     * @return 是否在可谈话范围内
     */
    public static boolean isOutOfInteraction(Role role, Npc npc){
        Integer[] self = role.getPosition();
        Integer[] other = npc.getPosition();
        return !(getDistance(self, other) <= Const.Max_OPT_DISTANCE);
    }

    /**
     * 检查角色之间距离是否在可攻击、谈话范围内
     * @param role 角色
     * @param target 目标角色
     * @return 是否在可攻击、谈话范围内
     */
    public static boolean isOutOfInteraction(Role role, Role target){
        Integer[] self = role.getPosition();
        Integer[] other = target.getPosition();
        return !(getDistance(self, other) <= Const.Max_OPT_DISTANCE);
    }

    /**
     * 计算两个实体间的直线距离
     * @param self 角色自身位置
     * @param other 目标实体位置
     * @return 对应成就包含的所有目标id
     */
    public static double getDistance(Integer[] self,Integer[] other){
        return Math.sqrt(Math.pow(self[0]-other[0],2)+Math.pow(self[1]-other[1],2));
    }

    private static final HashSet<Integer> teamSet = new HashSet<>();
    private static final HashSet<Integer> sceneSet = new HashSet<>();
    GameDao gameDao = new GameDao();

    /**
     * 队伍id随机生成，数量小于10000
     * @return 队伍id
     */
    public static String generateTeamId(){
        int tmp=0;
        int size = teamSet.size();
        Random ran = new Random();
        while (size+1>teamSet.size()){
            tmp = ran.nextInt(Const.MAX_ID);
            teamSet.add(tmp);
        }
        return String.valueOf(tmp);
    }

    /**
     * 临时副本id生成，小于10000
     * @return 临时副本id
     */
    public static int generateSceneId(){
        int tmp=0;
        int size = sceneSet.size();
        Random ran = new Random();
        while (size+1>sceneSet.size()){
            tmp = ran.nextInt(Const.MAX_ID);
            sceneSet.add(tmp);
        }
        return tmp;
    }

    /**
     * 公会id生成
     * @return 公会id
     */
    public int generateUnionId(){
        int unionId = gameDao.selectMaxUnionId();
        gameDao.updateMaxUnionId(unionId);
        return ++unionId;
    }

    /**
     * 装备id生成
     * @return 装备id
     */
    public int generateEquipId(){
        int equipId = gameDao.selectMaxEquipId();
        gameDao.updateMaxEquipId(equipId);
        return ++equipId;
    }

    /**
     * 根据唯一id，查找静态id
     * @param onlyId 装备唯一id
     * @return 装备静态id
     */
    public static int getStaticEquipId(int onlyId){
        Equipment equipment = GlobalInfo.getEquipmentHashMap().get(onlyId);
        return equipment.getEquipmentId();
    }

    /**
     * 获得装备或药品价格
     * @param key 物品id
     * @return 装备或药品价格
     */
    public static int getGoodsPrice(int key){
        if(String.valueOf(key).startsWith(Const.POTION_HEAD)){
            return PotionResource.getPotionStaticHashMap().get(key).getPrice();
        }else{
            return EquipmentResource.getEquipmentStaticHashMap().get(key).getPrice();
        }
    }

    /**
     * 获得药品名称
     * @param key 物品id
     * @return 药品名称
     */
    public static String getPotionName(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getName();
    }

    /**
     * 获得药品血量增益
     * @param key 物品id
     * @return 药品血量增益
     */
    public static int getPotionAddHp(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getAddHp();
    }

    /**
     * 获得药品蓝量增益
     * @param key 物品id
     * @return 药品蓝量增益
     */
    public static int getPotionAddMp(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getAddMp();
    }

    /**
     * 获得装备名
     * @param key 物品id
     * @return 装备名
     */
    public static String getEquipmentName(int key){
        return EquipmentResource.getEquipmentStaticHashMap().get(key).getName();
    }

    /**
     * 获得装备耐久
     * @param key 物品id
     * @return 装备耐久
     */
    public static int getEquipmentDura(int key){
        int equipId = getStaticEquipId(key);
        return EquipmentResource.getEquipmentStaticHashMap().get(equipId).getDurability();
    }

    /** 饿汉式单例模式 */
    private static AssistService instance = new AssistService();
    private AssistService() {}
    public static AssistService getInstance() {
        return instance;
    }
}
