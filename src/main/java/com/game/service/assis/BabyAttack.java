package com.game.service.assis;

import com.game.common.Const;
import com.game.entity.Monster;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author andy
 * @create 2020/6/10 17:49
 */
public class BabyAttack extends TimerTask {

    //定时器操作对象
    private Timer timer;
    //可以用构造方法来传参！
    //召唤出来攻击的怪物id
    private String monsterId;
    //角色所在场景id
    private int sceneId;

    public BabyAttack(Timer timer,String monsterId,int sceneId) {
        this.timer = timer;
        this.monsterId = monsterId;
        this.sceneId = sceneId;
    }

    static int k = 0;
    @Override
    public void run() {
        int damage = SkillService.normalAttackSkill(Const.BOSS_SKILL_ID);
        k++;
        //不在场景中了
        if(InitGame.scenes.get(sceneId)==null){
            System.out.println(Const.DUNGEONS_START_SCENE);
            this.timer.cancel();
            return;
        }
        //表现效果就是，怪物定时收到技能伤害，该类使用构造方法来传参
        Monster monster = InitGame.scenes.get(sceneId).getMonsterHashMap().get(monsterId);
        monster.setMonsterHp(monster.getMonsterHp()-damage);
        if(monster.getMonsterHp()<=0){
            this.timer.cancel();
            monster.setMonsterHp(0);
        }
        System.out.println("宝宝的第"+k+"次攻击，怪物的血量还有"+monster.getMonsterHp());
    }
}
