package com.game.system.role;

import com.game.netty.server.ServerHandler;
import com.game.system.gameserver.GlobalInfo;
import com.game.common.Const;
import com.game.system.role.entity.Role;
import com.game.system.scene.SceneService;
import com.game.system.scene.entity.Monster;
import com.game.system.scene.entity.Scene;
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
    /** 角色-用于进行通知*/
    private Role role;

    public RoleBabyAi(Timer timer, String monsterId, int sceneId, Role role) {
        this.timer = timer;
        this.monsterId = monsterId;
        this.sceneId = sceneId;
        this.role = role;
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
        SceneService.checkAndSetMonsterHp(monster.getMonsterHp()-damage,monster);
        if(monster.getMonsterHp()<=0){
            skillService.getReward(monster,role);
            ServerHandler.notifySelf(role.getId(),"怪物已被打败"+Const.Fight.SLAY_SUCCESS);
            this.timer.cancel();
            return;
        }
        ServerHandler.notifySelf(role.getId(),"宝宝的第"+k+"次攻击，怪物的血量还有"+monster.getMonsterHp());
        System.out.println("宝宝的第"+k+"次攻击，怪物的血量还有"+monster.getMonsterHp());
    }
}
