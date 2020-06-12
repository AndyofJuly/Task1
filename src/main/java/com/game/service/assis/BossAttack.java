package com.game.service.assis;

import com.game.common.Const;
import com.game.controller.FunctionService;
import com.game.entity.Baby;
import com.game.entity.Monster;
import com.game.entity.Role;
import com.game.entity.Skill;
import com.game.entity.store.CareerResource;
import com.game.entity.store.DungeonsResource;
import com.game.entity.store.SkillResource;
import com.game.service.RoleService;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 目前测试中，后续在此基础上进行扩展和修改
 * @Author andy
 * @create 2020/6/8 18:19
 */
public class BossAttack extends TimerTask {
    private Timer timer;
    //private HashMap<Integer, Role> roleHashMap;
    private String teamId;
    private int dungeonsId;
    private int sceneId;

    public BossAttack(Timer timer,String teamId,int dungeonsId,int sceneId) {
        this.timer = timer;
        this.teamId = teamId;
        this.dungeonsId = dungeonsId;
        this.sceneId = sceneId;
    }

    static int k = 0;
    static int seconds = 0;
    @Override
    public void run() {
        int damage = SkillService.normalAttackSkill(1008);
        //for(String teamId : DynamicResource.teamList.keySet()){
        //轮流攻击玩家，暂定用if..else结构
        boolean flagt=false,flagb=false,flagr=false,flago=false;
        int tkey=-1,bkey=-1,rkey=-1,okey=-1;
        seconds = k*Const.GAP_TIME_BOSS/1000;
        ArrayList<Integer> list = DynamicResource.teamList.get(teamId).getRoleList();
        //baby待修改
        Baby baby = null;
        //Monster baby = InitGame.scenes.get(sceneId).getMonsterHashMap().get("101");
        //记录当前场景id，用于回收 todo 应考虑角色离线的情况
        int tempSceneId = sceneId;
        int allHp=0;
        for(int i = 0; i< list.size(); i++){
            Role role = FunctionService.roleHashMap.get(list.get(i));
            baby = role.getBaby();
           //后面可扩展优化为状态设计模式，目前代码扩展能力弱
            if(role.isUseTaunt()){
                flagt=true;
                tkey=i;
                continue;
            }else if(baby!=null && baby.getBabyHp()>0){//场景中有召唤师，且宝宝血量大于0
                flagb=true;
                bkey = i;
                continue;
            }else if(role.getCareerId()==5001 && role.getHp()>0){//战士role.getAlive()==1
                flagr=true;
                rkey = i;//三个都是战士，记录了最后一个战士
                continue;
            }else if(role.getHp()>0){  //剩余职业，怪物任意选择攻击
                flago=true;
                okey = i;
                continue;
            }
        }

        if(flagt){
            Role role = FunctionService.roleHashMap.get(list.get(tkey));
            //攻击战士
            role.setHp(role.getHp()-damage);
            Instant nowDate = Instant.now();
            Duration between = Duration.between(RoleService.useTauntDate, nowDate);
            long l = between.toMillis()/1000;
            System.out.println(l);
            if(l>= SkillResource.skillStaticHashMap.get(1010).getDuration()){ //测试时间后嘲讽技能失效 todo 问题
                role.setUseTaunt(false);
            }
            if(role.getHp()<=0){
                role.setHp(0);
                role.setUseTaunt(false);
            }
            System.out.println("角色"+role.getName()+"释放嘲讽技能遭到攻击，当前血量为："+role.getHp());
        }else if(flagb){
            Role role = FunctionService.roleHashMap.get(list.get(bkey));
            //攻击宝宝
            baby = role.getBaby();
            baby.setBabyHp(baby.getBabyHp()-damage);
            System.out.println("宝宝遭到攻击，当前血量为："+baby.getBabyHp());
            if(baby.getBabyHp()<=0){
                baby.setBabyHp(0);
            }
        }else if(flagr){
            Role role = FunctionService.roleHashMap.get(list.get(rkey));
            //怪物对其进行攻击，直到战死
            role.setHp(role.getHp()-damage);
            if(role.getHp()<=0){
                role.setHp(0);
            }
            System.out.println("角色"+role.getName()+"遭到攻击，当前血量为："+role.getHp());
        }else if(flago){
            Role role = FunctionService.roleHashMap.get(list.get(okey));
            //怪物对其进行攻击，直到战死
            role.setHp(role.getHp()-damage);
            if(role.getHp()<=0){
                role.setHp(0);
            }
            System.out.println("角色"+role.getName()+"遭到攻击，当前血量为："+role.getHp());
        }
        k++;
        System.out.println("boss的第"+k+"次攻击");
        //超时，先假设k为计时，一次代表10s
        RoleService roleService = new RoleService();
        if(seconds>=DungeonsResource.dungeonsStaticHashMap.get(dungeonsId).getDeadTime()){  //36s
            System.out.println("副本时间结束，挑战失败");
            //无论成功与否，回到副本传送点
            for(int i = 0; i< list.size(); i++) {
                Role role = FunctionService.roleHashMap.get(list.get(i));
                roleService.move(Const.DUNGEONS_START_SCENE,role.getId());
            }
            this.timer.cancel();
            TempSceneCreate.deleteTempScene(tempSceneId);
            return;
        }

        // todo 当角色都被boss打败时，挑战失败退出副本
        for(int i = 0; i< list.size(); i++) {
            Role role = FunctionService.roleHashMap.get(list.get(i));
            allHp = allHp+role.getHp();
        }
        if(allHp==0){
            System.out.println("队伍角色均被打败，挑战失败");
            //回到副本传送点
            for(int i = 0; i< list.size(); i++) {
                Role role = FunctionService.roleHashMap.get(list.get(i));
                roleService.move(Const.DUNGEONS_START_SCENE,role.getId());
            }
            this.timer.cancel();
            TempSceneCreate.deleteTempScene(tempSceneId);
            return;
        }

        //当该副本的怪物血量为0的时候，挑战成功退出副本
        for(String key1 : InitGame.scenes.get(sceneId).getMonsterHashMap().keySet()){
            //这里代码有误，场景中只要挂掉一个，就显示挑战成功是有问题的
            if(InitGame.scenes.get(sceneId).getMonsterHashMap().get(key1).getMonsterHp()<=0){//注意该怪物是否为boss
                System.out.println("怪物已被打败，恭喜每人获得50银两奖励");
                //回到副本传送点
                for(int i = 0; i< list.size(); i++) {
                    Role role = FunctionService.roleHashMap.get(list.get(i));
                    role.setMoney(role.getMoney()+50);
                    roleService.move(Const.DUNGEONS_START_SCENE,role.getId());
                }
                this.timer.cancel();
                TempSceneCreate.deleteTempScene(tempSceneId);
                return;
            }
        }
    }

    public static int bossAttack(){
        return SkillService.normalAttackSkill(1008);
    }
}
