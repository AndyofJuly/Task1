package com.game.service.assis;

import com.game.controller.FunctionService;
import com.game.service.RoleService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 目前测试中，后续在此基础上进行扩展和修改
 * @Author andy
 * @create 2020/6/8 18:19
 */
public class BossAttack extends TimerTask {
    private Timer timer;

    public BossAttack(Timer timer) {
        this.timer = timer;
    }

    static int k = 0;
    @Override
    public void run() {
        int damage = SkillService.normalAttackSkill(1008);
        for(String teamId : DynamicResource.teamList.keySet()){
            if(DynamicResource.teamList.get(teamId).getDungeonsId()==4001){
                //for(int i = 0; i< DynamicResource.teamList.get(teamId).getRoleList().size(); i++){
                    //假设玩家轮流承受伤害
                if(k%2==0){
                    FunctionService.roleHashMap.get(1).setHp(FunctionService.roleHashMap.get(1).getHp()-damage);
                    System.out.println("角色1遭到攻击，当前血量为："+ FunctionService.roleHashMap.get(1).getHp());
                }else {
                    FunctionService.roleHashMap.get(16).setHp(FunctionService.roleHashMap.get(16).getHp()-damage);
                    System.out.println("角色2遭到攻击当前血量为："+ FunctionService.roleHashMap.get(16).getHp());
                }
                //}
            }
        }
        k++;
        System.out.println("boss的第"+k+"次攻击");
        //当该副本的怪物血量为0时，退出副本
        for(String key : InitGame.scenes.get(10007).getMonsterHashMap().keySet()){
            if(InitGame.scenes.get(10007).getMonsterHashMap().get(key).getMonsterHp()<=0){
                System.out.println("怪物已被打败");
                //无论成功与否，回到副本传送点
                RoleService roleService = new RoleService();
                roleService.move("副本传送点",FunctionService.roleHashMap.get(1).getId());
                roleService.move("副本传送点",FunctionService.roleHashMap.get(16).getId());
                this.timer.cancel();
            }
            //超时，先假设k为计时，一次代表10s
            if(k>=20){  //200s
                System.out.println("副本时间结束");
                this.timer.cancel();
                //无论成功与否，回到副本传送点
                RoleService roleService = new RoleService();
                roleService.move("副本传送点",FunctionService.roleHashMap.get(1).getId());
                roleService.move("副本传送点",FunctionService.roleHashMap.get(16).getId());
            }
        }
    }

    public static int bossAttack(){
        return SkillService.normalAttackSkill(1008);
    }
}
