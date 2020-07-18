package com.game.system.role;

import com.game.netty.server.ServerHandler;
import com.game.system.assist.GlobalInfo;
import com.game.common.Const;
import com.game.system.role.pojo.Baby;
import com.game.system.scene.pojo.Monster;
import com.game.system.scene.pojo.Scene;
import com.game.system.skill.SkillService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 角色的宝宝简单Ai
 * @Author andy
 * @create 2020/6/10 17:49
 */
public class RoleBabyAi extends TimerTask {

    /** 定时器操作对象 */
    private Timer timer;
    /** 召唤出来攻击的怪物id */
    private String monsterId;
    /** 角色所在场景id */
    private int sceneId;
    /** 角色id-用于进行通知*/
    private int roleId;

    public RoleBabyAi(Timer timer, String monsterId, int sceneId, int roleId) {
        this.timer = timer;
        this.monsterId = monsterId;
        this.sceneId = sceneId;
        this.roleId = roleId;
    }

    /** 测试用 */
    static int k = 0;
    @Override
    public void run() {
        SkillService skillService = new SkillService();
        int damage = skillService.normalAttackSkill(Const.BOSS_SKILL_ID);
        k++;

        //宝宝进行场景切换后停止攻击
        if(GlobalInfo.getScenes().get(sceneId)==null){
            this.timer.cancel();
            return;
        }

        //怪物定时收到技能伤害
        Scene scene = GlobalInfo.getScenes().get(sceneId);
        Monster monster = scene.getMonsterHashMap().get(monsterId);
        monster.setMonsterHp(monster.getMonsterHp()-damage);
        if(monster.getMonsterHp()<=0){
            this.timer.cancel();
        }
        ServerHandler.notifySelf(roleId,"宝宝的第"+k+"次攻击，怪物的血量还有"+monster.getMonsterHp());
        System.out.println("宝宝的第"+k+"次攻击，怪物的血量还有"+monster.getMonsterHp());
    }
}
