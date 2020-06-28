package com.game.service.assis;

import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.entity.Baby;
import com.game.entity.Role;
import com.game.entity.store.DungeonsResource;
import com.game.entity.store.SkillResource;
import com.game.service.RoleService;
import com.game.service.SkillService;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 目前测试中，后续在此基础上进行扩展和修改
 * @Author andy
 * @create 2020/6/8 18:19
 */
public class BossAttack extends TimerTask {
    //定时器操作对象
    private Timer timer;
    //队伍id
    private String teamId;
    //副本id
    private int dungeonsId;
    //临时场景id
    private int sceneId;

    public BossAttack(Timer timer,String teamId,int dungeonsId,int sceneId) {
        this.timer = timer;
        this.teamId = teamId;
        this.dungeonsId = dungeonsId;
        this.sceneId = sceneId;
    }

    //调用该方法的次数
    static int k;
    //1秒，计时
    static int seconds;
    int damage = SkillService.normalAttackSkill(Const.BOSS_SKILL_ID);
    Baby baby = null;
    ArrayList<Integer> list;
    RoleService roleService = new RoleService();
    @Override
    public void run() {
        //按照一定优先级的规则轮流攻击玩家
        list = GlobalResource.getTeamList().get(teamId).getRoleList();
        boolean flagTaunt=false,flagBaby=false,flagRole=false,flagOther=false;
        int keyTaunt=-1,keyBaby=-1,keyRole=-1,keyOther=-1;
        seconds = k*Const.GAP_TIME_BOSS/Const.TO_MS;

        for(int i = 0; i< list.size(); i++){
            Role role = GlobalResource.getRoleHashMap().get(list.get(i));
            baby = role.getBaby();
            if(role.isUseTaunt()){
                flagTaunt=true;
                keyTaunt=i;
                continue;
            }else if(baby!=null && baby.getBabyHp()>Const.ZERO){//场景中有召唤师，且宝宝血量大于0
                flagBaby=true;
                keyBaby = i;
                continue;
            }else if(role.getCareerId()==Const.FIGHTER_CAREER_ID && role.getHp()>0){//战士role.getAlive()==1
                flagRole=true;
                keyRole = i;
                continue;
            }else if(role.getHp()>Const.ZERO){  //剩余职业，怪物任意选择攻击
                flagOther=true;
                keyOther = i;
                continue;
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
        if(checkBossHpOut()){return;}
    }

    //怪物收到嘲讽，选择攻击战士
    private void attackRoleByTaunt(int keyTaunt,ArrayList<Integer> list){
        Role role = GlobalResource.getRoleHashMap().get(list.get(keyTaunt));
        role.setHp(role.getHp()-damage);
        Instant nowDate = Instant.now();
        Duration between = Duration.between(GlobalResource.getUseTauntDate(), nowDate);
        long l = between.toMillis()/Const.TO_MS; // 嘲讽持续时间
        if(l>= SkillResource.getSkillStaticHashMap().get(Const.TAUNT_SKILL_ID).getDuration()){
            role.setUseTaunt(false);
        }
        if(role.getHp()<=Const.ZERO){
            role.setHp(Const.ZERO);
            role.setUseTaunt(false);
        }
        System.out.println("角色"+role.getName()+"释放嘲讽技能遭到攻击，当前血量为："+role.getHp());
    }

    //怪物旁边有宝宝，选择攻击宝宝
    private void attackBaby(int keyBaby,ArrayList<Integer> list){
        Role role = GlobalResource.getRoleHashMap().get(list.get(keyBaby));
        baby = role.getBaby();
        baby.setBabyHp(baby.getBabyHp()-damage);
        System.out.println("宝宝遭到攻击，当前血量为："+baby.getBabyHp());
        if(baby.getBabyHp()<=Const.ZERO){
            baby.setBabyHp(Const.ZERO);
        }
    }

    //怪物优先攻击战士角色，然后攻击其他角色
    private void attackRole(int keyRole,ArrayList<Integer> list){
        Role role = GlobalResource.getRoleHashMap().get(list.get(keyRole));
        role.setHp(role.getHp()-damage);
        if(role.getHp()<=Const.ZERO){
            role.setHp(Const.ZERO);
        }
        System.out.println("角色"+role.getName()+"遭到攻击，当前血量为："+role.getHp());
    }

    private boolean checkTimeOut(){
        if(seconds>=DungeonsResource.getDungeonsStaticHashMap().get(dungeonsId).getDeadTime()){
            System.out.println("副本时间结束，挑战失败");
            leaveDungeons();
            return true;
        }
        return false;
    }

    private boolean checkTeamHpOut(){
        int allHp=Const.ZERO;
        for(int i = 0; i< list.size(); i++) {
            Role role = GlobalResource.getRoleHashMap().get(list.get(i));
            allHp = allHp+role.getHp();
        }
        if(allHp==0){
            System.out.println("队伍角色均被打败，挑战失败");
            leaveDungeons();
            return true;
        }
        return false;
    }

    private boolean checkBossHpOut(){
        String bossId =  GlobalResource.getTempIdHashMap().get(sceneId);
        if(GlobalResource.getScenes().get(sceneId).getMonsterHashMap().get(bossId).getMonsterHp()<=0){
            System.out.println("怪物已被打败，恭喜每人获得50银两奖励");
            leaveDungeons();
            return true;
        }
        return false;
    }

    private void leaveDungeons(){
        //重新计时
        k=0;
        //回到副本传送点
        for(int i = 0; i< list.size(); i++) {
            Role role = GlobalResource.getRoleHashMap().get(list.get(i));
            role.setMoney(role.getMoney()+Const.DUNGEONS_GAIN);
            roleService.moveTo(Const.DUNGEONS_START_SCENE,role.getId());
        }
        this.timer.cancel();
        TempSceneService.deleteTempScene(sceneId,teamId);
    }

}
