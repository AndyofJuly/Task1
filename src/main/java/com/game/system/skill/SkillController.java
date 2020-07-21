package com.game.system.skill;

import com.game.system.gameserver.GameController;
import com.game.system.role.pojo.Role;
import com.game.system.gameserver.GlobalInfo;
import com.game.common.ResponseInf;
import com.game.system.gameserver.AssistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.game.common.MyAnnontation;

import java.util.ArrayList;

/**
 * 技能模块调用方法入口
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Controller("skillController")
public class SkillController {

    private ArrayList<Integer> intList = GameController.getIntList();

    @Autowired
    private SkillService skillService;

    /** 获得当前自己拥有的技能，使用方式：skillList */
    @MyAnnontation(MethodName = "skillList")
    public ResponseInf getSkillList(){
        return ResponseInf.setResponse(skillService.getSkillInfo(getRole()),getRole());
    }

    /** 技能攻击，使用方式：skill skillId monsterId */
    @MyAnnontation(MethodName = "skill")
    public ResponseInf useSkillAttack(){
        String key = AssistService.checkMonsterId(intList.get(1),getRole());
        String msg = skillService.useSkillAttack(intList.get(0),key,getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 嘲讽技能测试，使用方式：taunt */
    @MyAnnontation(MethodName = "taunt")
    public ResponseInf tauntSkill(){
        String result = skillService.tauntSkill(getRole());
        return ResponseInf.setResponse(result,getRole());
    }

    /** 群伤技能测试，使用方式：groupAtk skillId */
    @MyAnnontation(MethodName = "groupAtk")
    public ResponseInf groupAtkSkill(){
        String result = skillService.groupAtkSkill(intList.get(0),getRole());
        return ResponseInf.setResponse(result,getRole());
    }

    /** 群回复技能测试，使用方式：groupCure skillId */
    @MyAnnontation(MethodName = "groupCure")
    public ResponseInf groupCureSkill(){
        String result = skillService.groupCureSkill(intList.get(0),getRole());
        return ResponseInf.setResponse(result,getRole());
    }

    //召唤技能测试，使用举例：summon monsterName ：here修改成,monsterId
    /** 用户注册，使用方式：register userName password */
    @MyAnnontation(MethodName = "summon")
    public ResponseInf summonSkill(){
        String result = skillService.summonSkill(intList.get(0),getRole());
        return ResponseInf.setResponse(result,getRole());
    }

    /*
    扩展-思路：
         蓝药缓慢恢复：使用蓝药时触发定时任务，定时任务每隔x秒对角色执行一次加蓝操作，累积加蓝到上限时停止该定时任务
         攻击后产生中毒效果：攻击怪物时触发定时任务，怪物每隔x秒扣血，中毒时间截止后或者怪物死亡时停止该定时任务。
         护盾技能：角色使用该技能时触发定时任务，角色血量上限增加护盾能承受的伤害，技能持续时间内可能受到攻击。
                 技能时间结束，此时血量上限重置为原上限，如果角色当前血量大于原上限，则此时满血；角色当前血量小于原上限，就取该血量作为当前血量。
    */

    /** 根据输入获得角色，输入参数最后一位为roleId */
    private Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }

    /** 普通攻击，使用方式：atk monsterId  */
    @MyAnnontation(MethodName = "atk")
    public ResponseInf normalAttack(){
        String key = AssistService.checkMonsterId(intList.get(0),getRole());
        return ResponseInf.setResponse(skillService.normalAttack(key,getRole()),getRole());
    }

    /** pk玩家，使用方式：pk skillId roleId */
    @MyAnnontation(MethodName = "pk")
    public ResponseInf pkPlayer(){
        String msg = skillService.pkPlayer(intList.get(0), intList.get(1),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

}
