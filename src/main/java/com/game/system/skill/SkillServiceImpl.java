package com.game.system.skill;

import com.game.system.achievement.AchieveServiceImpl;
import com.game.system.achievement.observer.SlayMonsterOB;
import com.game.system.achievement.subject.SlayMonsterSB;
import com.game.system.assist.AssistService;
import com.game.system.role.BabyAttack;
import com.game.system.assist.GlobalInfo;
import com.game.system.bag.PackageServiceImpl;
import com.game.common.Const;
import com.game.system.scene.pojo.Monster;
import com.game.system.skill.pojo.SkillStatic;
import com.game.system.role.pojo.Baby;
import com.game.system.role.pojo.CareerResource;
import com.game.system.bag.pojo.EquipmentResource;
import com.game.system.skill.pojo.SkillResource;
import com.game.system.achievement.IAchieveService;
import com.game.system.bag.IPackageService;
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
public class SkillServiceImpl implements ISkillService {
    private IAchieveService iAchieveService = new AchieveServiceImpl();
    private IPackageService iPackageService = new PackageServiceImpl();

    //普通单体攻击技能，角色和怪物均可以使用
    @Override
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

    @Override
    public String getSkillInfo(Role role){
        String output = Const.Fight.SKILL_LIST;
        String[] skillName = getSkillList(role);
        for(int i=0;i<skillName.length;i++){
            output+=skillName[i]+" ";
        }
        return output;
    }

    //普通的攻击技能
    @Override
    public String useSkillAttack(int skillId,String monsterId,Role role){
        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        Monster nowMonster = scene.getMonsterHashMap().get(monsterId);
        nowMonster.setAtkTargetId(role.getId());
        if(AssistService.checkDistance(role,nowMonster)==false){
            return Const.Fight.DISTACNE_LACK;
        }
        //使用该技能，记录当前时间，set方法传给角色的集合的技能对象的属性，同时判断时间是否合理满足CD
        String result = skillCommon(skillId,role);
        if(!Const.Fight.SUCCESS.equals(result)){
            return result;
        }
        //说明技能已经冷却，可以调用该方法，怪物扣血
        int hp = nowMonster.getMonsterHp();
        int skillHarm = SkillResource.getSkillStaticHashMap().get(skillId).getAtk();
        hp=hp-role.getAtk()-Const.WEAPON_BUFF-skillHarm;
        if(hp<=0){
            nowMonster.setAlive(0);
            return Const.Fight.SLAY_SUCCESS+getReward(nowMonster,role);
        }
        nowMonster.setMonsterHp(hp);
        return Const.Fight.TARGET_HP+hp;
    }

    @Override
    public String normalAttack(String monsterId,Role role){
        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        Monster nowMonster = scene.getMonsterHashMap().get(monsterId);
        //角色攻击力-怪物防御力为怪物扣血量
        nowMonster.setAtkTargetId(role.getId());
        if(AssistService.checkDistance(role,nowMonster)==false){
            return Const.Fight.DISTACNE_LACK;
        }
        if(!haveWeaponDura(role)){return Const.Fight.DURA_LACK;}
        int harm = role.getAtk()-nowMonster.getMonsterDef();
        int leftHp = nowMonster.getMonsterHp();
        if(harm>0){
            leftHp = nowMonster.getMonsterHp()-harm;
        }
        nowMonster.setMonsterHp(leftHp);
        if(leftHp<=0){
            nowMonster.setAlive(0);
            return Const.Fight.SLAY_SUCCESS+getReward(nowMonster,role);
        }
        return Const.Fight.TARGET_HP+leftHp;
    }

    private String getReward(Monster nowMonster,Role role){
        AssistService.setMonsterIsDead(true);  //对全部客户端进行通知
        nowMonster.setMonsterHp(0);
        nowMonster.setAlive(0);
        //获得奖励-随机装备和道具
        Random rand = new Random();
        int getMoney = rand.nextInt(60);
        iPackageService.addMoney(getMoney,role);
        int getEquipId = (int) (3001 + Math.floor(Math.random()*3));
        iPackageService.putIntoPackage(getEquipId,1,role);
        role.setAtk(role.getAtk()+2);
        //iAchieveService.countKilledMonster(nowMonster.getMonsterId(),role);
        SlayMonsterSB.notifyObservers(nowMonster.getMonsterId(),role);
        return "获得装备："+getEquipId+"!获得银两"+getMoney;
    }

    //战士的嘲讽技能；需在BossAttack中进行优化
    @Override
    public void tauntSkill(Role role){
        GlobalInfo.setUseTauntDate(Instant.now());
        role.setUseTaunt(true);
    }

    //法师的群伤技能；需要调整血量下限，不能为负数
    @Override
    public void groupAtkSkill(int skillId,Role role){
        //int skey = AssistService.checkSkillId(skillName);
        int sceneId = role.getNowScenesId();
        Scene scene = GlobalInfo.getScenes().get(sceneId);
        for(String key : scene.getMonsterHashMap().keySet()){
            Monster monster = scene.getMonsterHashMap().get(key);
            int leftHp = monster.getMonsterHp()-SkillResource.getSkillStaticHashMap().get(skillId).getAtk();
            monster.setMonsterHp(leftHp);
        }
    }

    //法师的群体恢复技能；需要调整血量上限，不能超过最大血量
    @Override
    public void groupCureSkill(int skillId,Role role){
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
    }

    //对怪物使用召唤技能时自动触发宝宝攻击
    //技能攻击
    @Override
    public void summonSkill(int monsterId,Role role){
        if(role.getBaby()==null){ //角色没有baby，则创建一个
            Baby baby = new Baby(Const.BABY_RAND_ID,Const.BABY_ID,role);
            role.setBaby(baby);
        }
        String monsterRandId = AssistService.checkMonsterId(monsterId,role);
        babyAttackMonster(monsterRandId,role.getNowScenesId());
    }

    //宝宝定时使用技能攻击怪物方法，简单AI
    @Override
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

    //使用技能时的共同特征进行抽象，如减mp，减武器耐久，CD计算等
    public String skillCommon(int skillId,Role role){
        String result = Const.Fight.SUCCESS;
        //使用该技能，记录当前时间，set方法传给角色的集合的技能对象的属性，同时判断时间是否合理满足CD
        Instant nowDate = Instant.now();
        Duration between = Duration.between(role.
                getSkillHashMap().get(skillId).getStart(), nowDate);
        long l = between.toMillis();
        if(l>SkillResource.getSkillStaticHashMap().get(skillId).getCd()*Const.GAP_TIME_SKILL) {
            role.getSkillHashMap().get(skillId).setStart(nowDate);
            int mp=role.getMp();
            //蓝量不够，不能使用技能
           if(mp<SkillResource.getSkillStaticHashMap().get(skillId).getUseMp()){
                return Const.Fight.MP_LACK;
            }
            mp=mp-SkillResource.getSkillStaticHashMap().get(skillId).getUseMp();
            role.setMp(mp);
        }else {
            result = Const.Fight.SKILL_IN_CD;
        }
        return result;
    }

    //普通攻击时武器耐久下降
    private boolean haveWeaponDura(Role role){
        int weaponId =0;//找到武器
        for (Integer selfEquipId : role.getEquipmentHashMap().keySet()) {
            int type = EquipmentResource.getEquipmentStaticHashMap().get(selfEquipId).getType();
            if(type==1){
                weaponId = selfEquipId;
            }
        }
        int dura=role.getEquipmentHashMap().get(weaponId).getDura();
        role.getEquipmentHashMap().get(weaponId).setDura(dura-Const.DURA_MINUS);
        if(dura<0){
            return false;
        }
        return true;
    }

    static {
        SlayMonsterSB.registerObserver(new SlayMonsterOB());
    }
}
