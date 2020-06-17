package com.game.service;

import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.entity.Baby;
import com.game.entity.Monster;
import com.game.entity.Role;
import com.game.entity.store.CareerResource;
import com.game.entity.store.SkillResource;
import com.game.service.assis.AssistService;
import com.game.service.assis.BabyAttack;
import com.game.service.assis.InitGame;
import com.game.service.assis.Listen;

import java.time.Duration;
import java.time.Instant;
import java.util.Timer;

/**
 * 职业技能相关，目前是个小demo，后续将进行改动，目前暂时弃置
 * @Author andy
 * @create 2020/6/8 12:12
 */
public class SkillService {

    //普通单体攻击技能，角色和怪物均可以使用
    public static int normalAttackSkill(int skillId){
        int damage = SkillResource.skillStaticHashMap.get(skillId).getAtk();
        return damage;
    }

    //查找当前自己的职业有什么技能
    public static String[] getSkillList(int roleId){
        int careerId = RoleController.roleHashMap.get(roleId).getCareerId();
        String[] skillId = CareerResource.careerStaticHashMap.get(careerId).getSkillId();
        String[] skillName = new String[skillId.length];
        for(int i=0;i<skillName.length;i++){
            skillName[i] = SkillResource.skillStaticHashMap.get(Integer.parseInt(skillId[i])).getName();
        }
        return skillName;
    }

    //普通的攻击技能
    public static String useSkillAttack(String skillName,String monsterId,int roleId){
        if(AssistService.checkDistance(roleId,monsterId)==false){
            return "距离不够";
        }
        //使用该技能，记录当前时间，set方法传给角色的集合的技能对象的属性，同时判断时间是否合理满足CD
        String result = SkillService.skillCommon(skillName,roleId);
        if(!"success".equals(result)){
            return result;
        }
        //说明技能已经冷却，可以调用该方法，怪物扣血
        Role role = RoleController.roleHashMap.get(roleId);
        int key1 = AssistService.checkSkillId(skillName);

        Monster nowMonster = InitGame.scenes.get(role.getNowScenesId()).getMonsterHashMap().get(monsterId);
        int hp = nowMonster.getMonsterHp();
        hp=hp-role.getAtk()-SkillResource.skillStaticHashMap.get(key1).getAtk()-
                Const.WEAPON_BUFF;
        if(hp<=Const.ZERO){
            result = "怪物血量为0，你已经打败该怪物！恭喜获得50银!获得2点攻击力加成!";
            Listen.monsterIsDead=true;  //对全部客户端进行通知
            nowMonster.setMonsterHp(Const.ZERO);
            nowMonster.setAlive(0);
            return result;
        }
        //System.out.println("玩家使用了"+skillName+"技能，怪物的血量还有"+hp);
        nowMonster.setMonsterHp(hp);
        return "你使用了"+skillName+"技能，怪物的血量还有"+hp;
    }

    //战士的嘲讽技能；需在BossAttack中进行优化
    public static void tauntSkill(int roleId){
        RoleService.useTauntDate = Instant.now();
        RoleController.roleHashMap.get(roleId).setUseTaunt(true);
    }

    //法师的群伤技能；需要调整血量下限，不能为负数
    public static void groupAtkSkill(String skillName,int roleId){
        int skey = AssistService.checkSkillId(skillName);
        int sceneId = RoleController.roleHashMap.get(roleId).getNowScenesId();
        for(String key : InitGame.scenes.get(sceneId).getMonsterHashMap().keySet()){
            Monster monster = InitGame.scenes.get(sceneId).getMonsterHashMap().get(key);
            monster.setMonsterHp(monster.getMonsterHp()-SkillResource.skillStaticHashMap.get(skey).getAtk());
        }
    }

    //法师的群体恢复技能；需要调整血量上限，不能超过最大血量
    public static void groupCureSkill(String skillName,int roleId){
        int skey = AssistService.checkSkillId(skillName);
        int sceneId = RoleController.roleHashMap.get(roleId).getNowScenesId();
        for(Role role : InitGame.scenes.get(sceneId).getRoleAll()){
            role.setHp(role.getHp()+SkillResource.skillStaticHashMap.get(skey).getAddHp());
        }
    }

    //对怪物使用召唤技能时自动触发宝宝攻击
    //技能攻击
    public static void summonSkill(String monsterName,int roleId){
        Role role = RoleController.roleHashMap.get(roleId);
        if(role.getBaby()==null){ //角色没有baby，则创建一个
            Baby baby = new Baby(Const.BABY_RAND_ID,Const.BABY_ID,role);
            role.setBaby(baby);
        }
        babyAttackMonster(AssistService.checkMonsterId(monsterName,roleId),role.getNowScenesId());
    }

    //宝宝定时使用技能攻击怪物方法，简单AI
    public static void babyAttackMonster(String monsterId,int sceneId){
        Timer timer = new Timer();
        BabyAttack babyAttack = new BabyAttack(timer,monsterId,sceneId);
        timer.schedule(babyAttack, Const.DELAY_TIME, Const.GAP_TIME_BABY);//1s一次，Const.GAP_TIME_POTION为10s一次
    }

    //盾类和蓝药缓慢恢复技能6.9版本，暂未重写

    //可扩展：boss释放群体伤害技能
/*    public static void groupAttackSkill(List<Integer> roleList, int skillId){
        //对这个角色集合中所有角色造成伤害，造成的伤害根据skillId对应的属性来增加
        for(Integer key : roleList){
            FunctionService.roleHashMap.get(key).setHp(FunctionService.roleHashMap.get(key).getHp()-SkillResource.skillStaticHashMap.get(skillId).getAtk());
        }
    }*/

    //使用技能时的共同特征进行抽象，如减mp，减武器耐久，CD计算等
    public static String skillCommon(String skillName,int roleId){
        Role role = RoleController.roleHashMap.get(roleId);
        String result = "success";
        int dura;
        int weaponId =0;
        for (Integer temp : role.getEquipmentHashMap().keySet()) {
            weaponId = temp;
        }
        int key1 = AssistService.checkSkillId(skillName);
        //使用该技能，记录当前时间，set方法传给角色的集合的技能对象的属性，同时判断时间是否合理满足CD
        Instant nowDate = Instant.now();
        Duration between = Duration.between(RoleController.roleHashMap.get(roleId).
                getSkillHashMap().get(key1).getStart(), nowDate);
        long l = between.toMillis();
        if(l>SkillResource.skillStaticHashMap.get(key1).getCd()*Const.GAP_TIME_SKILL) {
            role.getSkillHashMap().get(key1).setStart(nowDate);
            int mp=role.getMp();
            dura=role.getEquipmentHashMap().get(weaponId).getDura();
            //耐久小于等于0或者蓝量不够，退出场景
            if(dura<=0){
                return "武器耐久不够，请先修理再战斗";
            }
            if(mp<SkillResource.skillStaticHashMap.get(key1).getUseMp()){
                return "角色蓝量不够，请先恢复再战斗";
            }
            mp=mp-SkillResource.skillStaticHashMap.get(key1).getUseMp();
            role.setMp(mp);
            role.getEquipmentHashMap().get(weaponId).setDura(dura-Const.DURA_MINUS);
        }else {
            result = "该技能冷却中";
        }
        return result;
    }

}
