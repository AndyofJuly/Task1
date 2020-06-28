package com.game.controller;

import com.game.common.Const;
import com.game.entity.store.SkillResource;
import com.game.service.SkillService;
import com.game.service.assis.AssistService;
import com.game.service.assis.GlobalResource;
import org.springframework.stereotype.Service;
import com.game.common.MyAnnontation;

import java.util.ArrayList;

/**
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Service
public class SkillController {

    private ArrayList<String> strList = RoleController.getStrList();
    private ArrayList<Integer> intList = RoleController.getIntList();

    //显示当前自己的职业有什么技能
    @MyAnnontation(MethodName = "skillList")
    public String getSkillList(){
        String output = Const.Fight.SKILL_LIST;
        String[] skillName = SkillService.getSkillList(intList.get(0));
        for(int i=0;i<skillName.length;i++){
            output+=skillName[i]+" ";
        }
        return output;
    }

    //技能攻击，使用举例：skill skillName monsterName
    @MyAnnontation(MethodName = "skill")
    public String useSkillAttack(){
        String key = AssistService.checkMonsterId(strList.get(2),intList.get(0));
        return SkillService.useSkillAttack(strList.get(1),key,intList.get(0));
    }

    //嘲讽技能测试，使用举例：taunt
    @MyAnnontation(MethodName = "taunt")
    public String tauntSkill(){
        SkillService.tauntSkill(intList.get(0));
        return Const.Fight.SUMMON_MSG;
    }

    //群伤技能测试，使用举例：groupAtk skillName 默认对当前场景的所有怪物造成群伤，可扩展：传参为怪物集合
    @MyAnnontation(MethodName = "groupAtk")
    public String groupAtkSkill(){
        SkillService.groupAtkSkill(strList.get(1),intList.get(0));
        return Const.Fight.GROUPATK_MSG;
    }

    //群回复技能测试，使用举例：groupHile skillName 默认对当前场景的所有角色，可扩展：传参为角色集合
    @MyAnnontation(MethodName = "groupCure")
    public String groupCureSkill(){
        SkillService.groupCureSkill(strList.get(1),intList.get(0));
        return Const.Fight.CURE_MSG;
    }

    //召唤技能测试，使用举例：summon monsterName
    @MyAnnontation(MethodName = "summon")
    public String summonSkill(){
        SkillService.summonSkill(strList.get(1),intList.get(0));
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
}
