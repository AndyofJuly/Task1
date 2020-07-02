package com.game.controller;

import com.game.common.Const;
import com.game.entity.Role;
import com.game.service.DungeonsService;
import com.game.service.assis.GlobalResource;
import com.game.service.assis.InitGame;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import com.game.common.MyAnnontation;

import java.util.ArrayList;

/**
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Controller
public class DungeonsController {

    private ArrayList<Integer> intList = RoleController.getIntList();
    private DungeonsService dungeonsService = new DungeonsService();

    //获得当前队伍列表，使用举例：getTeamList
    @MyAnnontation(MethodName = "getTeamList")
    public String getTeamList(){
        return dungeonsService.getTeamList();
    }

    //获得当副本列表，使用举例：getDungeonsList
    @MyAnnontation(MethodName = "getDungeonsList")
    public String getDungeonsList(){
        return dungeonsService.getStaticDungeonsList();
    }

    //创建队伍，使用举例：create dungeonesId，返回teamId；
    @MyAnnontation(MethodName = "create")
    public String createTeam(){
        return dungeonsService.createTeam(intList.get(0), getRole());
    }

    //加入队伍，使用举例：join 2233(teamId)；
    @MyAnnontation(MethodName = "join")
    public String joinTeam(){
        dungeonsService.joinTeam(intList.get(0), getRole());
        return dungeonsService.getTeamRoleList(intList.get(0), getRole());
    }

    //开始副本，使用举例：start 2233(teamId) dungeonesId；
    @MyAnnontation(MethodName = "start")
    public String startDungeons(){
        dungeonsService.startDungeons(intList.get(0)+"",getRole());
        return Const.Fight.START_DUNGEONS;
    }

    //获得角色，适用于输入参数最后一位为roleId的情况
    public Role getRole(){
        return GlobalResource.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
