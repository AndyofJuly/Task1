package com.game.system.dungeons;

import com.game.netty.server.ServerHandler;
import com.game.system.achievement.observer.DungeonsOb;
import com.game.system.achievement.entity.Subject;
import com.game.system.gameserver.GlobalInfo;
import com.game.system.bag.PackageService;
import com.game.common.Const;
import com.game.system.role.entity.Baby;
import com.game.system.role.entity.Role;
import com.game.system.dungeons.entity.DungeonsResource;
import com.game.system.scene.entity.Monster;
import com.game.system.skill.entity.SkillResource;
import com.game.system.role.RoleService;
import com.game.system.scene.SceneService;
import com.game.system.skill.SkillService;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 副本中Boss的简单Ai，可以根据优先级选择角色攻击，释放普通攻击技能，同时也可以移动或普通攻击
 * @Author andy
 * @create 2020/6/8 18:19
 */
public class DungeonsBossAI extends TimerTask {
    /** 定时器操作对象 */
    private Timer timer;
    /** 队伍id */
    private String teamId;
    /** 副本id */
    private int dungeonsId;
    /** 临时场景id */
    private int sceneId;


    public DungeonsBossAI(Timer timer, String teamId, int dungeonsId, int sceneId) {
        this.timer = timer;
        this.teamId = teamId;
        this.dungeonsId = dungeonsId;
        this.sceneId = sceneId;
    }

    /** 调用该方法的次数 */
    static int k;
    /** 1秒，计时 */
    static int seconds;
    SkillService skillService = new SkillService();
    int damage = skillService.normalAttackSkill(Const.BOSS_SKILL_ID);
    Baby baby = null;
    ArrayList<Integer> list;
    boolean success = false;
    private SceneService sceneService = new SceneService();

    @Override
    public void run() {
        //按照一定优先级的规则轮流攻击玩家
        list = GlobalInfo.getTeamList().get(teamId).getRoleList();
        boolean flagTaunt=false,flagBaby=false,flagRole=false,flagOther=false;
        int keyTaunt=-1,keyBaby=-1,keyRole=-1,keyOther=-1;
        seconds = k*Const.GAP_TIME_BOSS/Const.TO_MS;

        for(int i = 0; i< list.size(); i++){
            Role role = GlobalInfo.getRoleHashMap().get(list.get(i));
            baby = role.getBaby();
            if(role.isUseTaunt()){
                flagTaunt=true;
                keyTaunt=i;
            }//场景中有召唤师，且宝宝血量大于0
            else if(baby!=null && baby.getBabyHp()>0){
                flagBaby=true;
                keyBaby = i;
            }//战士role.getAlive()==1
            else if(role.getCareerId()==Const.FIGHTER_CAREER_ID && role.getHp()>0){
                flagRole=true;
                keyRole = i;
            }//剩余职业，怪物任意选择攻击
            else if(role.getHp()>0){
                flagOther=true;
                keyOther = i;
            }
        }

        if(flagTaunt){
            attackRoleByTaunt(keyTaunt,list);
        }else if(flagBaby){
            attackBaby(keyBaby,list);
        }else if(flagRole){
            attackRole(keyRole,list);
        }else if(flagOther){
            attackRole(keyOther,list);
        }

        k++;
        if(checkTimeOut()){return;}
        if(checkTeamHpOut()){return;}
        if(checkBossHpOut()){
            for(int i = 0; i< list.size(); i++) {
                dungeonsSubject.notifyObserver(this.dungeonsId,GlobalInfo.getRoleHashMap().get(list.get(i)));
            }
        }
    }

    /** 怪物优先攻击战士角色，然后攻击其他角色 */
    private void attackRole(int keyRole,ArrayList<Integer> list){
        Role role = GlobalInfo.getRoleHashMap().get(list.get(keyRole));
        RoleService.checkAndSetHp(role.getHp()+role.getDef()-damage,role);
        System.out.println("角色"+role.getName()+"遭到技能攻击，当前血量为："+role.getHp());
        ServerHandler.notifyGroupRoles(getRoles(),"角色"+role.getName()+"遭到攻击，当前血量为："+role.getHp());
    }

    /** 怪物收到嘲讽，选择攻击战士 */
    private void attackRoleByTaunt(int keyTaunt,ArrayList<Integer> list){
        Role role = GlobalInfo.getRoleHashMap().get(list.get(keyTaunt));
        RoleService.checkAndSetHp(role.getHp()-damage,role);
        Instant nowDate = Instant.now();
        Duration between = Duration.between(GlobalInfo.getUseTauntDate(), nowDate);
        // 嘲讽持续时间
        long l = between.toMillis()/Const.TO_MS;
        if(l>= SkillResource.getSkillStaticHashMap().get(Const.TAUNT_SKILL_ID).getDuration()){
            role.setUseTaunt(false);
        }
        if(role.getHp()<=0){
            role.setUseTaunt(false);
        }
        ServerHandler.notifyGroupRoles(getRoles(),"角色"+role.getName()+"释放嘲讽技能遭到攻击，当前血量为："+role.getHp());
        System.out.println("角色"+role.getName()+"释放嘲讽技能遭到攻击，当前血量为："+role.getHp());
    }

    /** 怪物旁边有宝宝，选择攻击宝宝 */
    private void attackBaby(int keyBaby,ArrayList<Integer> list){
        Role role = GlobalInfo.getRoleHashMap().get(list.get(keyBaby));
        baby = role.getBaby();
        baby.setBabyHp(baby.getBabyHp()-damage);
        ServerHandler.notifyGroupRoles(getRoles(),"宝宝遭到攻击，当前血量为："+baby.getBabyHp());
        System.out.println("宝宝遭到攻击，当前血量为："+baby.getBabyHp());
        if(baby.getBabyHp()<=0){
            baby.setBabyHp(0);
        }
    }

    /** 检查副本时间是否已到 */
    private boolean checkTimeOut(){
        if(seconds>=DungeonsResource.getDungeonsStaticHashMap().get(dungeonsId).getDeadTime()){
            ServerHandler.notifyGroupRoles(getRoles(),"副本时间结束，挑战失败");
            System.out.println("副本时间结束，挑战失败");
            leaveDungeons();
            return true;
        }
        return false;
    }

    /** 检查队伍角色是否全败 */
    private boolean checkTeamHpOut(){
        int allHp=0;
        for(int i = 0; i< list.size(); i++) {
            Role role = GlobalInfo.getRoleHashMap().get(list.get(i));
            allHp = allHp+role.getHp();
        }
        if(allHp==0){
            ServerHandler.notifyGroupRoles(getRoles(),"队伍角色均被打败，挑战失败");
            System.out.println("队伍角色均被打败，挑战失败");
            leaveDungeons();
            return true;
        }
        return false;
    }

    /** 检查Boss是否已被击败 */
    private boolean checkBossHpOut(){
        //GlobalInfo.getScenes().get(sceneId).getMonsterHashMap().get(bossId).getMonsterHp()<=0
        String bossId =  GlobalInfo.getTempIdHashMap().get(sceneId);
        Monster boss = GlobalInfo.getScenes().get(sceneId).getMonsterHashMap().get(bossId);
        if(boss==null){
            //boss.setAlive(0);boss.getMonsterHp()<=0
            ServerHandler.notifyGroupRoles(getRoles(),"通关副本，恭喜每人再获得50银两副本通关奖励");
            success = true;
            leaveDungeons();
            return true;
        }
        return false;
    }

    /** 离开副本 */
    private void leaveDungeons(){
        //重新计时
        k=0;
        //回到副本传送点
        for(int i = 0; i< list.size(); i++) {
            Role role = GlobalInfo.getRoleHashMap().get(list.get(i));
            if(success = true){
                //PackageService packageService = new PackageService();
                PackageService.getInstance().addMoney(Const.DUNGEONS_GAIN,role);
            }
            role.setTeamId(null);
            RoleService roleService = new RoleService();
            sceneService.moveTo(Const.DUNGEONS_START_SCENE,role);
        }
        success = false;
        this.timer.cancel();
        sceneService.deleteTempScene(sceneId,teamId);
    }

    /** 获取队伍角色 */
    private ArrayList<Role> getRoles(){
        ArrayList<Role> roles = DungeonsService.getTeamRoles(GlobalInfo.getTeamList().get(teamId).getRoleList());
        return roles;
    }

    /** 注册成就观察者 */
    Subject dungeonsSubject = new Subject();
    private DungeonsOb dungeonsOb = new DungeonsOb(dungeonsSubject);

}
