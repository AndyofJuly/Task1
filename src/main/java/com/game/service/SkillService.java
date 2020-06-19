package com.game.service;

import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.entity.Baby;
import com.game.entity.Monster;
import com.game.entity.Role;
import com.game.entity.Skill;
import com.game.entity.excel.SkillStatic;
import com.game.entity.store.CareerResource;
import com.game.entity.store.SkillResource;
import com.game.service.assis.*;

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
        int damage = SkillResource.getSkillStaticHashMap().get(skillId).getAtk();
        return damage;
    }

    //查找当前自己的职业有什么技能
    public static String[] getSkillList(int roleId){
        int careerId = GlobalResource.getRoleHashMap().get(roleId).getCareerId();
        String[] skillId = CareerResource.getCareerStaticHashMap().get(careerId).getSkillId();
        String[] skillName = new String[skillId.length];
        for(int i=0;i<skillName.length;i++){
            skillName[i] = SkillResource.getSkillStaticHashMap().get(Integer.parseInt(skillId[i])).getName();
        }
        return skillName;
    }

    //普通的攻击技能
    public static String useSkillAttack(String skillName,String monsterId,int roleId){
        if(AssistService.checkDistance(roleId,monsterId)==false){
            return Const.Fight.DISTACNE_LACK;
        }
        //使用该技能，记录当前时间，set方法传给角色的集合的技能对象的属性，同时判断时间是否合理满足CD
        String result = SkillService.skillCommon(skillName,roleId);
        if(!Const.Fight.SUCCESS.equals(result)){
            return result;
        }
        //说明技能已经冷却，可以调用该方法，怪物扣血
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        int key1 = AssistService.checkSkillId(skillName);

        Monster nowMonster = InitGame.scenes.get(role.getNowScenesId()).getMonsterHashMap().get(monsterId);
        int hp = nowMonster.getMonsterHp();
        hp=hp-role.getAtk()-SkillResource.getSkillStaticHashMap().get(key1).getAtk()-
                Const.WEAPON_BUFF;
        if(hp<=Const.ZERO){
            result = Const.Fight.SLAY_SUCCESS;
            Listen.setMonsterIsDead(true);  //对全部客户端进行通知
            nowMonster.setMonsterHp(Const.ZERO);
            nowMonster.setAlive(0);
            return result;
        }
        //System.out.println("玩家使用了"+skillName+"技能，怪物的血量还有"+hp);
        nowMonster.setMonsterHp(hp);
        return Const.Fight.TARGET_HP+hp;
    }

    //战士的嘲讽技能；需在BossAttack中进行优化
    public static void tauntSkill(int roleId){
        GlobalResource.setUseTauntDate(Instant.now());
        GlobalResource.getRoleHashMap().get(roleId).setUseTaunt(true);
    }

    //法师的群伤技能；需要调整血量下限，不能为负数
    public static void groupAtkSkill(String skillName,int roleId){
        int skey = AssistService.checkSkillId(skillName);
        int sceneId = GlobalResource.getRoleHashMap().get(roleId).getNowScenesId();
        for(String key : InitGame.scenes.get(sceneId).getMonsterHashMap().keySet()){
            Monster monster = InitGame.scenes.get(sceneId).getMonsterHashMap().get(key);
            monster.setMonsterHp(monster.getMonsterHp()-SkillResource.getSkillStaticHashMap().get(skey).getAtk());
        }
    }

    //法师的群体恢复技能；需要调整血量上限，不能超过最大血量
    public static void groupCureSkill(String skillName,int roleId){
        SkillStatic skill = SkillResource.getSkillStaticHashMap().get(AssistService.checkSkillId(skillName));
        int sceneId = GlobalResource.getRoleHashMap().get(roleId).getNowScenesId();
        try { //吟唱施法时间
            Thread.sleep(skill.getCastTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(Role role : InitGame.scenes.get(sceneId).getRoleAll()){
            role.setHp(role.getHp()+skill.getAddHp());
        }
    }

    //对怪物使用召唤技能时自动触发宝宝攻击
    //技能攻击
    public static void summonSkill(String monsterName,int roleId){
        Role role = GlobalResource.getRoleHashMap().get(roleId);
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
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        String result = Const.Fight.SUCCESS;
        int dura;
        int weaponId =0;
        for (Integer temp : role.getEquipmentHashMap().keySet()) {
            weaponId = temp;
        }
        int key1 = AssistService.checkSkillId(skillName);
        //使用该技能，记录当前时间，set方法传给角色的集合的技能对象的属性，同时判断时间是否合理满足CD
        Instant nowDate = Instant.now();
        Duration between = Duration.between(GlobalResource.getRoleHashMap().get(roleId).
                getSkillHashMap().get(key1).getStart(), nowDate);
        long l = between.toMillis();
        if(l>SkillResource.getSkillStaticHashMap().get(key1).getCd()*Const.GAP_TIME_SKILL) {
            role.getSkillHashMap().get(key1).setStart(nowDate);
            int mp=role.getMp();
            dura=role.getEquipmentHashMap().get(weaponId).getDura();
            //耐久小于等于0或者蓝量不够，退出场景
            if(dura<=0){
                return Const.Fight.DURA_LACK;
            }
            if(mp<SkillResource.getSkillStaticHashMap().get(key1).getUseMp()){
                return Const.Fight.MP_LACK;
            }
            mp=mp-SkillResource.getSkillStaticHashMap().get(key1).getUseMp();
            role.setMp(mp);
            role.getEquipmentHashMap().get(weaponId).setDura(dura-Const.DURA_MINUS);
        }else {
            result = Const.Fight.SKILL_IN_CD;
        }
        return result;
    }

}
