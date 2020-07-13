package com.game.system.skill;

import com.game.common.Const;
import com.game.system.role.pojo.Role;
import com.game.system.role.RoleController;
import com.game.system.assist.GlobalInfo;
import com.game.common.ResponseInf;
import com.game.system.assist.AssistService;
import org.springframework.stereotype.Controller;
import com.game.common.MyAnnontation;

import java.util.ArrayList;

/**
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Controller
public class SkillController {

    private ArrayList<Integer> intList = RoleController.getIntList();
    private ISkillService iSkillService = new SkillServiceImpl();

    //显示当前自己的职业有什么技能
    @MyAnnontation(MethodName = "skillList")
    public ResponseInf getSkillList(){
        return ResponseInf.setResponse(iSkillService.getSkillInfo(getRole()),getRole());
    }

    //技能攻击，使用举例：skill skillName monsterName ：here修改成skillId,monsterId
    @MyAnnontation(MethodName = "skill")
    public ResponseInf useSkillAttack(){
        String key = AssistService.checkMonsterId(intList.get(1),getRole());
        String msg = iSkillService.useSkillAttack(intList.get(0),key,getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    //嘲讽技能测试，使用举例：taunt
    @MyAnnontation(MethodName = "taunt")
    public ResponseInf tauntSkill(){
        iSkillService.tauntSkill(getRole());
        return ResponseInf.setResponse(Const.Fight.SUMMON_MSG,getRole());
    }

    //群伤技能测试，使用举例：groupAtk skillName 默认对当前场景的所有怪物造成群伤，可扩展：传参为怪物集合：here修改成skillId
    @MyAnnontation(MethodName = "groupAtk")
    public ResponseInf groupAtkSkill(){
        iSkillService.groupAtkSkill(intList.get(0),getRole());
        return ResponseInf.setResponse(Const.Fight.GROUPATK_MSG,getRole());
    }

    //群回复技能测试，使用举例：groupHile skillName 默认对当前场景的所有角色，可扩展：传参为角色集合：here修改成skillId
    @MyAnnontation(MethodName = "groupCure")
    public ResponseInf groupCureSkill(){
        iSkillService.groupCureSkill(intList.get(0),getRole());
        return ResponseInf.setResponse(Const.Fight.CURE_MSG,getRole());
    }

    //召唤技能测试，使用举例：summon monsterName ：here修改成,monsterId
    @MyAnnontation(MethodName = "summon")
    public ResponseInf summonSkill(){
        iSkillService.summonSkill(intList.get(0),getRole());
        return ResponseInf.setResponse(Const.Fight.BABY_MSG,getRole());
    }

    /*
    扩展-思路：
         蓝药缓慢恢复：使用蓝药时触发定时任务，定时任务每隔x秒对角色执行一次加蓝操作，累积加蓝到上限时停止该定时任务
         攻击后产生中毒效果：攻击怪物时触发定时任务，怪物每隔x秒扣血，中毒时间截止后或者怪物死亡时停止该定时任务。
         护盾技能：角色使用该技能时触发定时任务，角色血量上限增加护盾能承受的伤害，技能持续时间内可能受到攻击。
                 技能时间结束，此时血量上限重置为原上限，如果角色当前血量大于原上限，则此时满血；角色当前血量小于原上限，就取该血量作为当前血量。
    */

    //获得角色，适用于输入参数最后一位为roleId的情况
    private Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }

    //普通攻击 atk monsterId (role)
    @MyAnnontation(MethodName = "atk")
    public ResponseInf normalAttack(){
        String key = AssistService.checkMonsterId(intList.get(0),getRole());
        return ResponseInf.setResponse(iSkillService.normalAttack(key,getRole()),getRole());
    }

}
