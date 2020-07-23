package com.game.system.achievement;

import com.game.common.MyAnnontation;
import com.game.common.ResponseInf;
import com.game.system.gameserver.GameController;
import com.game.system.gameserver.GlobalInfo;
import com.game.system.role.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

/**
 * 成就模块调用方法入口
 * @Author andy
 * @create 2020/7/21 11:22
 */
@Controller("achievementController")
public class AchievementController {

    private ArrayList<Integer> intList = GameController.getIntList();

    @Autowired
    private AchievementService achievementService;

    /** 获取自己的成就，使用方式：getAchievement-ac */
    @MyAnnontation(MethodName = "ac")
    public ResponseInf getAchievement(){
        return ResponseInf.setResponse(achievementService.getAchievementList(getRole()),getRole());
    }


    /** 获取自己的成就，使用方式：getAchievement-sc */
    @MyAnnontation(MethodName = "sc")
    public ResponseInf getTaskSchedule(){
        return ResponseInf.setResponse(achievementService.getTaskSchedule(getRole()),getRole());
    }

    /** 根据输入获得角色，输入参数最后一位为roleId */
    public Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
