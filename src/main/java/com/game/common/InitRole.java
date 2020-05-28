package com.game.common;

import com.game.controller.FunctionService;
import com.game.entity.Skill;
import com.game.entity.excel.EquipmentStatic;

import java.time.Instant;

/**
 * 角色进入游戏后，初始化技能
 * @Author andy
 * @create 2020/5/28 11:29
 */
public class InitRole {
    static {

        //目前角色拥有两个技能
        Skill skillA = new Skill(InitStaticResource.skillStaticHashMap.get(1001));
        Skill skillB = new Skill(InitStaticResource.skillStaticHashMap.get(1002));

        //放入角色的技能集合中
        FunctionService.role.getSkillHashMap().put(1001,skillA);
        FunctionService.role.getSkillHashMap().put(1002,skillB);

        //初始化技能的使用时间戳
        Instant start = Instant.now();
        FunctionService.role.getSkillHashMap().get(1001).setStart(start);
        FunctionService.role.getSkillHashMap().get(1002).setStart(start);
    }

}
