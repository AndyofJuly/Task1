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
    @Override
    public void run() {
        //可以设置距离检查，boss离角色较远时不做处理

        int damage = SkillService.normalAttackSkill(Const.BOSS_SKILL_ID);
        //按照一定规则轮流攻击玩家，暂定用if..else结构
        boolean flagt=false,flagb=false,flagr=false,flago=false;
        int tkey=-1,bkey=-1,rkey=-1,okey=-1;
        seconds = k*Const.GAP_TIME_BOSS/Const.TO_MS;
        ArrayList<Integer> list = DynamicResource.teamList.get(teamId).getRoleList();
        Baby baby = null;
        int allHp=Const.ZERO;
        for(int i = 0; i< list.size(); i++){
            Role role = RoleController.roleHashMap.get(list.get(i));
            baby = role.getBaby();
            if(role.isUseTaunt()){
                flagt=true;
                tkey=i;
                continue;
            }else if(baby!=null && baby.getBabyHp()>Const.ZERO){//场景中有召唤师，且宝宝血量大于0
                flagb=true;
                bkey = i;
                continue;
            }else if(role.getCareerId()==Const.FIGHTER_CAREER_ID && role.getHp()>0){//战士role.getAlive()==1
                flagr=true;
                rkey = i;
                continue;
            }else if(role.getHp()>Const.ZERO){  //剩余职业，怪物任意选择攻击
                flago=true;
                okey = i;
                continue;
            }
        }

        if(flagt){
            Role role = RoleController.roleHashMap.get(list.get(tkey));
            //攻击战士
            role.setHp(role.getHp()-damage);
            Instant nowDate = Instant.now();
            Duration between = Duration.between(RoleService.useTauntDate, nowDate);
            long l = between.toMillis()/Const.TO_MS;
            System.out.println(l);
            if(l>= SkillResource.skillStaticHashMap.get(Const.TAUNT_SKILL_ID).getDuration()){
                role.setUseTaunt(false);
            }
            if(role.getHp()<=Const.ZERO){
                role.setHp(Const.ZERO);
                role.setUseTaunt(false);
            }
            System.out.println("角色"+role.getName()+"释放嘲讽技能遭到攻击，当前血量为："+role.getHp());
        }else if(flagb){
            Role role = RoleController.roleHashMap.get(list.get(bkey));
            //攻击宝宝
            baby = role.getBaby();
            baby.setBabyHp(baby.getBabyHp()-damage);
            System.out.println("宝宝遭到攻击，当前血量为："+baby.getBabyHp());
            if(baby.getBabyHp()<=Const.ZERO){
                baby.setBabyHp(Const.ZERO);
            }
        }else if(flagr){
            Role role = RoleController.roleHashMap.get(list.get(rkey));
            //怪物对其进行攻击，直到战死
            role.setHp(role.getHp()-damage);
            if(role.getHp()<=Const.ZERO){
                role.setHp(Const.ZERO);
            }
            System.out.println("角色"+role.getName()+"遭到攻击，当前血量为："+role.getHp());
        }else if(flago){
            Role role = RoleController.roleHashMap.get(list.get(okey));
            //怪物对其进行攻击，直到战死
            role.setHp(role.getHp()-damage);
            if(role.getHp()<=Const.ZERO){
                role.setHp(Const.ZERO);
            }
            System.out.println("角色"+role.getName()+"遭到攻击，当前血量为："+role.getHp());
        }
        k++;
        System.out.println("boss的第"+k+"次攻击");
        //超时，先假设k为计时，一次代表10s
        RoleService roleService = new RoleService();
        if(seconds>=DungeonsResource.dungeonsStaticHashMap.get(dungeonsId).getDeadTime()){
            System.out.println("副本时间结束，挑战失败");
            //无论成功与否，回到副本传送点
            for(int i = 0; i< list.size(); i++) {
                Role role = RoleController.roleHashMap.get(list.get(i));
                roleService.moveTo(Const.DUNGEONS_START_SCENE,role.getId());
            }
            this.timer.cancel();
            TempSceneService.deleteTempScene(sceneId,teamId);
            return;
        }

        // 当角色都被boss打败时，挑战失败退出副本
        for(int i = 0; i< list.size(); i++) {
            Role role = RoleController.roleHashMap.get(list.get(i));
            allHp = allHp+role.getHp();
        }
        if(allHp==0){
            System.out.println("队伍角色均被打败，挑战失败");
            //回到副本传送点
            for(int i = 0; i< list.size(); i++) {
                Role role = RoleController.roleHashMap.get(list.get(i));
                roleService.moveTo(Const.DUNGEONS_START_SCENE,role.getId());
            }
            this.timer.cancel();
            TempSceneService.deleteTempScene(sceneId,teamId);
            return;
        }

        //得出该BOSSid，根据副本id
        String bossId =  DynamicResource.tempIdHashMap.get(sceneId);
            if(InitGame.scenes.get(sceneId).getMonsterHashMap().get(bossId).getMonsterHp()<=0){//注意该怪物是否为boss
                System.out.println("怪物已被打败，恭喜每人获得50银两奖励");
                //回到副本传送点
                for(int i = 0; i< list.size(); i++) {
                    Role role = RoleController.roleHashMap.get(list.get(i));
                    role.setMoney(role.getMoney()+Const.DUNGEONS_GAIN);
                    roleService.moveTo(Const.DUNGEONS_START_SCENE,role.getId());
                }
                this.timer.cancel();
                TempSceneService.deleteTempScene(sceneId,teamId);
                return;
            }
    }

    public static int bossAttack(){
        return SkillService.normalAttackSkill(Const.BOSS_SKILL_ID);
    }
}
