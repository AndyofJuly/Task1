package com.game.controller;

import com.game.service.DungeonsService;
import com.game.service.assis.AssistService;
import com.game.service.assis.DynamicResource;
import com.game.service.assis.InitGame;
import org.springframework.stereotype.Service;
import com.game.common.MyAnnontation;

import java.util.ArrayList;

/**
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Service
public class DungeonsController {

    String[] strings = RoleController.strings;

    //获得当前队伍列表，使用举例：getTeamList
    @MyAnnontation(MethodName = "getTeamList")
    public String getTeamList(){
        String output = "队伍列表：";
        for(String teamId : DynamicResource.teamList.keySet()){
            output+=teamId+"; ";
        }
        return output;
    }

    //获得当副本列表，使用举例：getDungeonsList
    @MyAnnontation(MethodName = "getDungeonsList")
    public String getDungeonsList(){
        return InitGame.dungeonsList;
    }

    //创建队伍，使用举例：create dungeonesId，返回teamId；
    @MyAnnontation(MethodName = "create")
    public String createTeam(){
        String teamId =DungeonsService.createTeam(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
        return "已创建，队伍id："+teamId;
    }

    //加入队伍，使用举例：join 2233(teamId) ；
    @MyAnnontation(MethodName = "join")
    public String joinTeam(){
        ArrayList<Integer> roleList = DungeonsService.joinTeam(strings[1],Integer.parseInt(strings[2]));
        String output="当前队伍角色：";
        for(Integer roleId : roleList){
            output = output+RoleController.roleHashMap.get(roleId).getName()+" ";
        }
        return output;
    }

    //开始副本，使用举例：start 2233(teamId) dungeonesId；
    @MyAnnontation(MethodName = "start")
    public String startDungeons(){
        DungeonsService.startDungeons(strings[1],Integer.parseInt(strings[2]));
        return "副本已开启";
    }

}
