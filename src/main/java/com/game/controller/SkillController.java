package com.game.controller;

import com.game.common.Const;
import com.game.entity.Role;
import com.game.entity.store.SkillResource;
import com.game.service.SkillService;
import com.game.service.assis.AssistService;
import com.game.service.assis.GlobalResource;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import com.game.common.MyAnnontation;

import java.util.ArrayList;

/**
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Controller
public class SkillController {

    private ArrayList<Integer> intList = RoleController.getIntList();
    SkillService skillService = new SkillService();

    //显示当前自己的职业有什么技能
    @MyAnnontation(MethodName = "skillList")
    public String getSkillList(){
        String output = Const.Fight.SKILL_LIST;
        String[] skillName = skillService.getSkillList(getRole());
        for(int i=0;i<skillName.length;i++){
            output+=skillName[i]+" ";
        }
        return output;
    }

    //技能攻击，使用举例：skill skillName monsterName ：here修改成skillId,monsterId
    @MyAnnontation(MethodName = "skill")
    public String useSkillAttack(){
        String key = AssistService.checkMonsterId(intList.get(1),getRole());
        return skillService.useSkillAttack(intList.get(0),key,getRole());
    }

    //嘲讽技能测试，使用举例：taunt
    @MyAnnontation(MethodName = "taunt")
    public String tauntSkill(){
        skillService.tauntSkill(getRole());
        return Const.Fight.SUMMON_MSG;
    }

    //群伤技能测试，使用举例：groupAtk skillName 默认对当前场景的所有怪物造成群伤，可扩展：传参为怪物集合：here修改成skillId
    @MyAnnontation(MethodName = "groupAtk")
    public String groupAtkSkill(){
        skillService.groupAtkSkill(intList.get(0),getRole());
        return Const.Fight.GROUPATK_MSG;
    }

    //群回复技能测试，使用举例：groupHile skillName 默认对当前场景的所有角色，可扩展：传参为角色集合：here修改成skillId
    @MyAnnontation(MethodName = "groupCure")
    public String groupCureSkill(){
        skillService.groupCureSkill(intList.get(0),getRole());
        return Const.Fight.CURE_MSG;
    }

    //here
    //召唤技能测试，使用举例：summon monsterName ：here修改成,monsterId
    @MyAnnontation(MethodName = "summon")
    public String summonSkill(){
        skillService.summonSkill(intList.get(0),getRole());
        return Const.Fight.BABY_MSG;
    }

    /*   待调整
    //扩展方法蓝药缓慢恢复demo
    @MyAnnontation(MethodName = "sR")
    public String slowlyRecoverd(){
        return roleService.slowlyRecoverd();
    }

    //扩展方法毒素技能和护盾技能
    @MyAnnontation(MethodName = "sK")
    public String useSkill(){
        return roleService.useSkill(strings[1],strings[2]);
    }*/

    //获得角色，适用于输入参数最后一位为roleId的情况
    public Role getRole(){
        return GlobalResource.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
