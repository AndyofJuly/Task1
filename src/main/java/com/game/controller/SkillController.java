package com.game.controller;

import com.game.entity.store.SkillResource;
import com.game.service.SkillService;
import com.game.service.assis.AssistService;
import org.springframework.stereotype.Service;
import com.game.common.MyAnnontation;

/**
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Service
public class SkillController {

    String[] strings = RoleController.strings;
    //显示当前自己的职业有什么技能
    @MyAnnontation(MethodName = "skillList")
    public String getSkillList(){
        String output = "可用技能：";
        String[] skillName = SkillService.getSkillList(Integer.parseInt(strings[1]));
        for(int i=0;i<skillName.length;i++){
            output+=skillName[i]+" ";
        }
        return output;
    }

    //技能攻击，使用举例：skill skillName monsterName
    @MyAnnontation(MethodName = "skill")
    public String useSkillAttack(){
        String key = AssistService.checkMonsterId(strings[2],Integer.parseInt(strings[3]));
        return SkillService.useSkillAttack(strings[1],key,Integer.parseInt(strings[3]));
    }

    //嘲讽技能测试，使用举例：taunt
    @MyAnnontation(MethodName = "taunt")
    public String tauntSkill(){
        SkillService.tauntSkill(Integer.parseInt(strings[1]));
        return "怪物受到嘲讽";
    }

    //群伤技能测试，使用举例：groupAtk skillName 默认对当前场景的所有怪物造成群伤，可扩展：传参为怪物集合
    @MyAnnontation(MethodName = "groupAtk")
    public String groupAtkSkill(){
        SkillService.groupAtkSkill(strings[1],Integer.parseInt(strings[2]));
        return "使用群伤技能，场景怪物均受到攻击";
    }

    //群回复技能测试，使用举例：groupHile skillName 默认对当前场景的所有角色，可扩展：传参为角色集合
    @MyAnnontation(MethodName = "groupCure")
    public String groupCureSkill(){
        SkillService.groupCureSkill(strings[1],Integer.parseInt(strings[2]));
        return "使用群体治疗技能";
    }

    //召唤技能测试，使用举例：summon monsterName
    @MyAnnontation(MethodName = "summon")
    public String summonSkill(){
        SkillService.summonSkill(strings[1],Integer.parseInt(strings[2]));
        return "召唤出宝宝，开始自动释放技能攻击怪物";
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
