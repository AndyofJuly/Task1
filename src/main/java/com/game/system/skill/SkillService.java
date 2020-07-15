package com.game.system.skill;

import com.game.netty.server.ServerHandler;
import com.game.system.achievement.observer.SlayMonsterOB;
import com.game.system.achievement.subject.SlayMonsterSB;
import com.game.system.assist.AssistService;
import com.game.system.role.BabyAttack;
import com.game.system.assist.GlobalInfo;
import com.game.system.bag.PackageService;
import com.game.common.Const;
import com.game.system.scene.pojo.Monster;
import com.game.system.skill.pojo.SkillStatic;
import com.game.system.role.pojo.Baby;
import com.game.system.role.pojo.CareerResource;
import com.game.system.bag.pojo.EquipmentResource;
import com.game.system.skill.pojo.SkillResource;
import com.game.system.role.pojo.Role;
import com.game.system.scene.pojo.Scene;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.Timer;

/**
 * 职业技能相关，目前是个小demo，后续将进行改动，目前暂时弃置
 * @Author andy
 * @create 2020/6/8 12:12
 */
public class SkillService {
    //private IAchieveService iAchieveService = new AchieveServiceImpl();
    private PackageService packageService = new PackageService();

    /**
     * 普通攻击技能-怪物
     * @param skillId 技能id
     * @return 技能伤害
     */
    //怪物普通攻击技能
    public int normalAttackSkill(int skillId){
        int damage = SkillResource.getSkillStaticHashMap().get(skillId).getAtk();
        return damage;
    }

    //查找当前自己的职业有什么技能
    private String[] getSkillList(Role role){
        int careerId = role.getCareerId();
        int[] skillId = CareerResource.getCareerStaticHashMap().get(careerId).getSkillId();
        String[] skillName = new String[skillId.length];
        for(int i=0;i<skillName.length;i++){
            int id = skillId[i];
            skillName[i] = SkillResource.getSkillStaticHashMap().get(id).getName();
        }
        return skillName;
    }

    /**
     * 获取当前角色的技能列表
     * @param role 角色
     * @return String
     */
    public String getSkillInfo(Role role){
        String output = Const.Fight.SKILL_LIST;
        String[] skillName = getSkillList(role);
        for(int i=0;i<skillName.length;i++){
            output+=skillName[i]+" ";
        }
        return output;
    }

    /**
     * 普通攻击技能
     * @param skillId 技能id
     * @param monsterId 怪物id
     * @param role 角色
     * @return String
     */
    public String useSkillAttack(int skillId,String monsterId,Role role){
        if(!checkSkillValid(skillId,role)){return "你没有该技能";}
        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        Monster monster = scene.getMonsterHashMap().get(monsterId);
        monster.setAtkTargetId(role.getId());
        if(!AssistService.checkDistance(role, monster)){
            return Const.Fight.DISTACNE_LACK;
        }
        String result = skillCommon(skillId,role);
        if(!Const.Fight.SUCCESS.equals(result)){
            return result;
        }
        return atkSkillAffect(skillId,monster,role);
    }

    private String atkSkillAffect(int skillId,Monster monster,Role role){
        int hp = monster.getMonsterHp();
        int skillHarm = SkillResource.getSkillStaticHashMap().get(skillId).getAtk();
        int weaponBuff = 0;
        if(role.getEquipmentHashMap()!=null){
            //有装备，加攻击buff
            weaponBuff=Const.WEAPON_BUFF;
        }
        hp=hp-role.getAtk()-weaponBuff-skillHarm;
        if(hp<=0){
            monster.setAlive(0);
            return Const.Fight.SLAY_SUCCESS+getReward(monster,role);
        }
        monster.setMonsterHp(hp);
        return Const.Fight.TARGET_HP+hp;
    }

    //使用技能时的共同特征进行抽象，如减mp，CD计算等
    public String skillCommon(int skillId,Role role){
        Instant nowDate = Instant.now();
        Duration between = Duration.between(role.getSkillHashMap().get(skillId).getStart(), nowDate);
        if(between.toMillis()>SkillResource.getSkillStaticHashMap().get(skillId).getCd()*Const.GAP_TIME_SKILL) {
            role.getSkillHashMap().get(skillId).setStart(nowDate);
            if(role.getMp()<SkillResource.getSkillStaticHashMap().get(skillId).getUseMp()){
                return Const.Fight.MP_LACK;
            }
            int leftMp=role.getMp()-SkillResource.getSkillStaticHashMap().get(skillId).getUseMp();
            role.setMp(leftMp);
        }else {
            return Const.Fight.SKILL_IN_CD;
        }
        return Const.Fight.SUCCESS;
    }

    /**
     * 普攻
     * @param monsterId 怪物id
     * @param role 角色
     * @return String
     */
    //普通攻击，平砍
    public String normalAttack(String monsterId,Role role){
        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        Monster monster = scene.getMonsterHashMap().get(monsterId);
        //角色攻击力-怪物防御力为怪物扣血量
        monster.setAtkTargetId(role.getId());
        if(AssistService.checkDistance(role,monster)==false){
            return Const.Fight.DISTACNE_LACK;
        }
        if(!isWeaponHaveDura(role)){return Const.Fight.DURA_LACK;}
        return atkAffect(monster,role);
    }

    //普通攻击触发的效果
    private String atkAffect(Monster monster,Role role){
        int harm = role.getAtk()-monster.getMonsterDef();
        int leftHp = monster.getMonsterHp();
        if(harm>0){
            leftHp = monster.getMonsterHp()-harm;
        }
        monster.setMonsterHp(leftHp);
        if(leftHp<=0){
            monster.setAlive(0);
            return Const.Fight.SLAY_SUCCESS+getReward(monster,role);
        }
        return Const.Fight.TARGET_HP+leftHp;
    }

    //普通攻击时武器耐久下降
    private boolean isWeaponHaveDura(Role role){
        int weaponId =0;
        for (Integer selfEquipId : role.getEquipmentHashMap().keySet()) {
            if(EquipmentResource.getEquipmentStaticHashMap().get(selfEquipId).getType()==1){
                weaponId = selfEquipId;
            }
        }
        int leftDura=role.getEquipmentHashMap().get(weaponId).getDura()-1;
        role.getEquipmentHashMap().get(weaponId).setDura(leftDura);
        if(leftDura<0){
            return false;
        }
        return true;
    }

    private String getReward(Monster nowMonster,Role role){
        nowMonster.setMonsterHp(0);
        nowMonster.setAlive(0);
        //获得奖励-随机装备和道具
        Random rand = new Random();
        int getMoney = rand.nextInt(Const.RAND_MONEY);
        packageService.addMoney(getMoney,role);
        int getEquipId = (int) (3001 + Math.floor(Math.random()*Const.RAND_EWUIP));
        packageService.putIntoPackage(getEquipId,1,role);
        role.setAtk(role.getAtk()+Const.REWARD_ATK);
        //成就
        SlayMonsterSB.notifyObservers(nowMonster.getMonsterId(),role);
        //通知该场景其他角色怪物已被打败
        ServerHandler.notifySceneGroup(role.getNowScenesId(),nowMonster.getMonsterId()+"已被"+role.getName()+"打败");

        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        scene.getMonsterHashMap().remove(nowMonster.getId());
        return "获得装备："+getEquipId+"!获得银两"+getMoney;
    }

    /**
     * 嘲讽技能
     * @param role 角色
     */
    //战士的嘲讽技能；需在BossAttack中进行优化
    public String tauntSkill(Role role){
        if(!checkSkillValid(Const.TAUNT_SKILL_ID,role)){return "你没有该技能";}
        GlobalInfo.setUseTauntDate(Instant.now());
        role.setUseTaunt(true);
        return Const.Fight.SUMMON_MSG;
    }

    /**
     * 群体技能
     * @param skillId 技能id
     * @param role 角色
     */
    //法师的群伤技能；需要调整血量下限，不能为负数
    public String groupAtkSkill(int skillId,Role role){
        if(!checkSkillValid(skillId,role)){return "你没有该技能";}
        //int skey = AssistService.checkSkillId(skillName);
        int sceneId = role.getNowScenesId();
        Scene scene = GlobalInfo.getScenes().get(sceneId);
        for(String key : scene.getMonsterHashMap().keySet()){
            Monster monster = scene.getMonsterHashMap().get(key);
            int leftHp = monster.getMonsterHp()-SkillResource.getSkillStaticHashMap().get(skillId).getAtk();
            monster.setMonsterHp(leftHp);
        }
        return Const.Fight.GROUPATK_MSG;
    }

    /**
     * 群体治疗
     * @param skillId 技能id
     * @param role 角色
     */
    //法师的群体恢复技能；需要调整血量上限，不能超过最大血量
    public String groupCureSkill(int skillId,Role role){
        if(!checkSkillValid(skillId,role)){return "你没有该技能";}
        //int skillId = AssistService.checkSkillId(skillName);
        SkillStatic skill = SkillResource.getSkillStaticHashMap().get(skillId);
        int sceneId = role.getNowScenesId();
        try { //吟唱施法时间
            Thread.sleep(skill.getCastTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(Role tempRole : GlobalInfo.getScenes().get(sceneId).getRoleAll()){
            tempRole.setHp(tempRole.getHp()+skill.getAddHp());
        }
        return Const.Fight.CURE_MSG;
    }

    /**
     * 召唤术
     * @param monsterId 召唤出宝宝所要攻击的怪物id
     * @param role 角色
     */
    //对怪物使用召唤技能时自动触发宝宝攻击
    //技能攻击
    public String summonSkill(int monsterId,Role role){
        if(!checkSkillValid(Const.SUMMON_ID,role)){return "你没有该技能";}
        if(role.getBaby()==null){ //角色没有baby，则创建一个
            Baby baby = new Baby(Const.BABY_RAND_ID,Const.BABY_ID,role);
            role.setBaby(baby);
        }
        String monsterRandId = AssistService.checkMonsterId(monsterId,role);
        babyAttackMonster(monsterRandId,role.getNowScenesId());
        return Const.Fight.BABY_MSG;
    }

    /**
     * 宝宝攻击
     * @param monsterId 所要攻击的怪物id
     * @param sceneId 场景id
     */
    //宝宝定时使用技能攻击怪物方法，简单AI
    public void babyAttackMonster(String monsterId,int sceneId){
        Timer timer = new Timer();
        BabyAttack babyAttack = new BabyAttack(timer,monsterId,sceneId);
        timer.schedule(babyAttack, Const.DELAY_TIME, Const.GAP_TIME_BABY);//1s一次，Const.GAP_TIME_POTION为10s一次
    }

    //盾类和蓝药缓慢恢复技能6.9版本，暂未重写

    //可扩展：boss释放群体伤害技能-对某个范围的角色产生伤害
/*    public static void groupAttackSkill(List<Integer> roleList, int skillId){
        //对这个角色集合中所有角色造成伤害，造成的伤害根据skillId对应的属性来增加
        for(Integer key : roleList){
            FunctionService.roleHashMap.get(key).setHp(FunctionService.roleHashMap.get(key).getHp()-SkillResource.skillStaticHashMap.get(skillId).getAtk());
        }
    }*/



    //判断该角色是否能使用该技能-感觉此方法写在服务端有些多余
    private boolean checkSkillValid(int skillId,Role role){
        int careerId = role.getCareerId();
        int[] skillsId = CareerResource.getCareerStaticHashMap().get(careerId).getSkillId();
        for(int i=0;i<skillsId.length;i++){
            if(skillId==skillsId[i]){
                return true;
            }
        }
        return false;
    }

    static {
        SlayMonsterSB.registerObserver(new SlayMonsterOB());
    }
}
