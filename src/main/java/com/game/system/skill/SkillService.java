package com.game.system.skill;

import com.game.netty.server.ServerHandler;
import com.game.system.achievement.observer.FsPkSuccessOb;
import com.game.system.achievement.observer.SlayMonsterOb;
import com.game.system.achievement.subject.Subject;
import com.game.system.assist.AssistService;
import com.game.system.dungeons.DungeonsService;
import com.game.system.role.RoleBabyAi;
import com.game.system.assist.GlobalInfo;
import com.game.system.bag.PackageService;
import com.game.common.Const;
import com.game.system.role.RoleService;
import com.game.system.scene.SceneService;
import com.game.system.scene.pojo.Monster;
import com.game.system.skill.pojo.SkillStatic;
import com.game.system.role.pojo.Baby;
import com.game.system.role.pojo.CareerResource;
import com.game.system.bag.pojo.EquipmentResource;
import com.game.system.skill.pojo.SkillResource;
import com.game.system.role.pojo.Role;
import com.game.system.scene.pojo.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

/**
 * 技能模块的业务逻辑处理
 * @Author andy
 * @create 2020/6/8 12:12
 */
@Service
public class SkillService {
    @Autowired
    private PackageService packageService;// = new PackageService();

    /**
     * 怪物普通攻击技能
     * @param skillId 技能id
     * @return 技能伤害值
     */
    public int normalAttackSkill(int skillId){
        return SkillResource.getSkillStaticHashMap().get(skillId).getAtk();
    }

    /**
     * 查找当前自己的职业有什么技能
     * @param role 角色
     * @return 职业技能
     */
    private String[] getSkillList(Role role){
        int careerId = role.getCareerId();
        Integer[] skillId = CareerResource.getCareerStaticHashMap().get(careerId).getSkillId();
        String[] skillName = new String[skillId.length];
        for(int i=0;i<skillName.length;i++){
            int id = skillId[i];
            skillName[i] = SkillResource.getSkillStaticHashMap().get(id).getName();
        }
        return skillName;
    }

    /**
     * 获得当前自己拥有的技能
     * @param role 角色
     * @return 信息提示
     */
    public String getSkillInfo(Role role){
        StringBuilder output = new StringBuilder(Const.Fight.SKILL_LIST);
        String[] skillName = getSkillList(role);
        for (String s : skillName) {
            output.append(s).append(" ");
        }
        return output.toString();
    }

    /**
     * 普通攻击技能，每个职业都可用使用
     * @param skillId 技能id
     * @param monsterId 怪物id
     * @param role 角色
     * @return 信息提示
     */
    public String useSkillAttack(int skillId,String monsterId,Role role){
        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        Monster monster = scene.getMonsterHashMap().get(monsterId);
        monster.setAtkTargetId(role.getId());
        if(AssistService.isNotInView(role, monster)){
            return Const.Fight.DISTACNE_LACK;
        }
        String result = skillCommon(skillId,role);
        if(!Const.Fight.SUCCESS.equals(result)){
            return result;
        }
        return atkSkillAffect(skillId,monster,role);
    }

    /**
     * 使用普通攻击技能时怪物的状态处理
     * @param skillId 技能id
     * @param monster 怪物
     * @param role 角色
     * @return 信息提示
     */
    private String atkSkillAffect(int skillId,Monster monster,Role role){
        int hp = monster.getMonsterHp();
        int skillHarm = SkillResource.getSkillStaticHashMap().get(skillId).getAtk();
        int weaponBuff = 0;
        if(role.getEquipmentHashMap().get(0)!=0){
            weaponBuff=Const.WEAPON_BUFF;
        }
        hp=hp-role.getAtk()-weaponBuff-skillHarm;
        if(hp<=0){
            return Const.Fight.SLAY_SUCCESS+getReward(monster,role);
        }
        //monster.setMonsterHp(hp);
        SceneService.checkAndSetMonsterHp(hp,monster);
        return Const.Fight.TARGET_HP+hp;
    }

    /**
     * 使用技能时的mp较少和CD计算等共同特征
     * @param skillId 技能id
     * @param role 角色
     * @return 信息提示
     */
    public String skillCommon(int skillId,Role role){
        Instant nowDate = Instant.now();
        Duration between = Duration.between(role.getSkillHashMap().get(skillId).getStart(), nowDate);
        if(between.toMillis()>SkillResource.getSkillStaticHashMap().get(skillId).getCd()*Const.GAP_TIME_SKILL) {
            role.getSkillHashMap().get(skillId).setStart(nowDate);
            if(role.getMp()<SkillResource.getSkillStaticHashMap().get(skillId).getUseMp()){
                return Const.Fight.MP_LACK;
            }
            int leftMp=role.getMp()-SkillResource.getSkillStaticHashMap().get(skillId).getUseMp();
            //role.setMp(leftMp);
            RoleService.checkAndSetMp(leftMp,role);
        }else {
            return Const.Fight.SKILL_IN_CD;
        }
        return Const.Fight.SUCCESS;
    }

    /**
     * 普通攻击，穿戴武器情况下可以使用
     * @param monsterId 怪物id
     * @param role 角色
     * @return 信息提示
     */
    public String normalAttack(String monsterId,Role role){
        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        Monster monster = scene.getMonsterHashMap().get(monsterId);
        monster.setAtkTargetId(role.getId());
        if(AssistService.isNotInView(role, monster)){
            return Const.Fight.DISTACNE_LACK;
        }
        if(!isWeaponHaveDura(role)){return Const.Fight.DURA_LACK;}
        return atkAffect(monster,role);
    }

    /**
     * 普通攻击时怪物的状态处理
     * @param monster 怪物
     * @param role 角色
     * @return 信息提示
     */
    private String atkAffect(Monster monster,Role role){
        int harm = role.getAtk()-monster.getMonsterDef();
        int leftHp = monster.getMonsterHp();
        if(harm>0){
            leftHp = monster.getMonsterHp()-harm;
        }
        //monster.setMonsterHp(leftHp);
        SceneService.checkAndSetMonsterHp(leftHp,monster);
        if(leftHp<=0){
            return Const.Fight.SLAY_SUCCESS+getReward(monster,role);
        }
        return Const.Fight.TARGET_HP+leftHp;
    }

    /**
     * 普通攻击时武器耐久下降
     * @param role 角色
     * @return 信息提示
     */
    private boolean isWeaponHaveDura(Role role){
        //int weaponId =0;
/*        for (Integer selfEquipId : role.getEquipmentHashMap().keySet()) {
            if(EquipmentResource.getEquipmentStaticHashMap().get(selfEquipId).getType()==1){
                weaponId = selfEquipId;
            }
        }*/
        int weaponId = role.getEquipmentHashMap().get(0);
        int leftDura=GlobalInfo.getEquipmentHashMap().get(weaponId).getDura()-1;
        GlobalInfo.getEquipmentHashMap().get(weaponId).setDura(leftDura);
        return leftDura >= 0;
    }

    /**
     * 打败怪物后获得奖励
     * @param nowMonster 怪物
     * @param role 角色
     * @return 信息提示
     */
    private String getReward(Monster nowMonster,Role role){
        Random rand = new Random();
        int getMoney = rand.nextInt(Const.RAND_MONEY);
        packageService.addMoney(getMoney,role);
        //int getEquipId = (int) (3001 + Math.floor(Math.random()*Const.RAND_EWUIP));
        //packageService.putIntoPackage(getEquipId,1,role);
        role.setAtk(role.getAtk()+Const.REWARD_ATK);

        //Subject.notifyObservers(nowMonster.getMonsterId(),role,slayMonsterOb);
        skillSubject.notifyObserver(nowMonster.getMonsterId(),role);

        ArrayList<Role> roles = GlobalInfo.getScenes().get(role.getNowScenesId()).getRoleAll();
        ServerHandler.notifyGroupRoles(roles,nowMonster.getMonsterId()+"已被"+role.getName()+"打败");

        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        scene.getMonsterHashMap().remove(nowMonster.getId());
        return "!获得银两"+getMoney;//"获得装备："+getEquipId+
    }

    /**
     * 与玩家pk-任意同场景相同视野下可以发动，效果与技能和普攻相同
     * @param skillId 技能id
     * @param targetRoleId pk角色id
     * @param role 角色
     * @return 信息提示
     */
    public String pkPlayer (int skillId,int targetRoleId, Role role){
        Role enemy = GlobalInfo.getRoleHashMap().get(targetRoleId);
        if(!enemy.getNowScenesId().equals(role.getNowScenesId())){
            return "不再同一场景，无法pk";
        }
        if(AssistService.isNotInView(role,enemy)){
            return Const.Fight.DISTACNE_LACK;
        }

        int harm = 0;
        //skillId为0时表示普通攻击
        if(skillId==0){
            harm = role.getAtk();
        }else {
            String result = new SkillService().skillCommon(skillId,role);
            if(!Const.Fight.SUCCESS.equals(result)){
                return result;
            }
            if(role.getEquipmentHashMap().get(0)!=0){
                int weaponBuff=Const.WEAPON_BUFF;
                harm = SkillResource.getSkillStaticHashMap().get(skillId).getAtk()+weaponBuff;
            }
        }
        return pkAffect(harm,enemy,role);
    }

    /**
     * pk攻击时的双方的状态处理
     * @param harm 伤害
     * @param enemy pk的对象
     * @param role 角色
     * @return 信息提示
     */
    private String pkAffect(int harm,Role enemy,Role role){
        RoleService.checkAndSetHp(enemy.getHp()-harm,enemy);
        //pk收益与损失结算
        if(enemy.getHp()<=0){
            role.setAtk(role.getAtk()+Const.ABTAIN_ATK);
            packageService.addMoney(Const.PK_GET_LOST,role);
            if(!packageService.lostMoney(Const.PK_GET_LOST,enemy)){
                enemy.setMoney(0);
            }
            //Subject.notifyObservers(Const.achieve.TASK_PK_SUCCESS,role,fsPkSuccessOb);
            skillSubject.notifyObserver(0,role);
            return Const.Fight.PK_SUCCESS;
        }
        ServerHandler.notifyRole(enemy.getId(),"你遭到玩，"+role.getName()+"的pk，受到"+harm+"点伤害",
                role.getId(),"你向对方发起了pk，攻击了对方");
        return Const.Fight.TARGET_HP+enemy.getHp();
    }

    /**
     * 战士的嘲讽技能，在副本中对BOSS使用
     * @param role 角色
     * @return 信息提示
     */
    public String tauntSkill(Role role){
        if(isNotSkillValid(Const.TAUNT_SKILL_ID,role)){return "你没有该技能";}
        GlobalInfo.setUseTauntDate(Instant.now());
        role.setUseTaunt(true);
        return Const.Fight.SUMMON_MSG;
    }

    /**
     * 法师的群伤技能，对视野内怪物造成伤害
     * @param skillId 技能id
     * @param role 角色
     * @return 信息提示
     */
    public String groupAtkSkill(int skillId,Role role){
        if(isNotSkillValid(skillId,role)){return "你没有该技能";}

        for(String key : role.getViewGridBo().getGridMonsterMap().keySet()){
            Monster monster = role.getViewGridBo().getGridMonsterMap().get(key);
            int leftHp = monster.getMonsterHp()-SkillResource.getSkillStaticHashMap().get(skillId).getAtk();
            //monster.setMonsterHp(leftHp);
            SceneService.checkAndSetMonsterHp(leftHp,monster);
            if(leftHp<0){
                getReward(monster,role);
            }
        }
        return Const.Fight.GROUPATK_MSG;
    }

    /**
     * 群体治疗，队伍存在时为队伍角色治疗，不存在时仅治疗自身
     * @param skillId 技能id
     * @param role 角色
     * @return 信息提示
     */
    public String groupCureSkill(int skillId,Role role){
        if(isNotSkillValid(skillId,role)){return "你没有该技能";}
        SkillStatic skill = SkillResource.getSkillStaticHashMap().get(skillId);
        //吟唱施法时间
        try {
            Thread.sleep(skill.getCastTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //队伍角色回血/自己回血
        if(role.getTeamId()!=null){
            ArrayList<Role> roles = DungeonsService.getTeamRoles(GlobalInfo.getTeamList().get(role.getTeamId()).getRoleList());
            for(Role friendRole : roles){
                //friendRole.setHp(friendRole.getHp()+skill.getAddHp());
                RoleService.checkAndSetHp(friendRole.getHp()+skill.getAddHp(),friendRole);
            }
        }else {
            //role.setHp(role.getHp()+skill.getAddHp());
            RoleService.checkAndSetHp(role.getHp()+skill.getAddHp(),role);
        }
        return Const.Fight.CURE_MSG;
    }

    /**
     * 召唤术，召唤宝宝，指定宝宝对某个怪物发起攻击
     * @param monsterId 召唤出宝宝所要攻击的怪物id
     * @param role 角色
     * @return 信息提示
     */
    public String summonSkill(int monsterId,Role role){
        if(isNotSkillValid(Const.SUMMON_ID,role)){return "你没有该技能";}
        if(role.getBaby()==null){
            Baby baby = new Baby(Const.BABY_RAND_ID,Const.BABY_ID,role);
            role.setBaby(baby);
        }
        String monsterRandId = AssistService.checkMonsterId(monsterId,role);
        babyAttackMonster(monsterRandId,role);
        return Const.Fight.BABY_MSG;
    }

    /**
     * 宝宝进行攻击，简单Ai
     * @param monsterId 所要攻击的怪物id
     * @param role 角色
     */
    public void babyAttackMonster(String monsterId,Role role){
        Timer timer = new Timer();
        RoleBabyAi roleBabyAi = new RoleBabyAi(timer,monsterId,role.getNowScenesId(),role.getId());
        timer.schedule(roleBabyAi, Const.DELAY_TIME, Const.GAP_TIME_BABY);
    }

    /**
     * 判断该角色当前职业是否能使用该技能
     * @param skillId 技能id
     * @param role 角色
     * @return 时刻可以使用指定技能
     */
    private boolean isNotSkillValid(int skillId,Role role){
        int careerId = role.getCareerId();
        Integer[] skillsId = CareerResource.getCareerStaticHashMap().get(careerId).getSkillId();
        for (int value : skillsId) {
            if (skillId == value) {
                return false;
            }
        }
        return true;
    }

    //盾类和蓝药缓慢恢复技能6.9版本，暂未重写

    //可扩展：boss释放群体伤害技能-对场景内所有角色产生伤害

/*    public static void groupAttackSkill(List<Integer> roleList, int skillId){
        //对这个角色集合中所有角色造成伤害，造成的伤害根据skillId对应的属性来增加
        for(Integer key : roleList){
            FunctionService.roleHashMap.get(key).setHp(FunctionService.roleHashMap.get(key).getHp()-SkillResource.skillStaticHashMap.get(skillId).getAtk());
        }
    }*/

/*    static {
        SlayMonsterSB.registerObserver(new SlayMonsterOb());
    }*/
    Subject skillSubject = new Subject();
    private SlayMonsterOb slayMonsterOb = new SlayMonsterOb(skillSubject);
    private FsPkSuccessOb fsPkSuccessOb = new FsPkSuccessOb(skillSubject);
}
