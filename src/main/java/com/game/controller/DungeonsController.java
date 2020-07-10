package com.game.controller;

import com.game.common.Const;
import com.game.entity.Role;
import com.game.service.IDungeonsService;
import com.game.service.assist.ResponseInf;
import com.game.service.impl.DungeonsServiceImpl;
import com.game.service.assist.GlobalInfo;
import org.springframework.stereotype.Controller;
import com.game.common.MyAnnontation;

import java.util.ArrayList;

/**
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Controller
public class DungeonsController {

    private ArrayList<Integer> intList = RoleController.getIntList();
    private IDungeonsService iDungeonsService = new DungeonsServiceImpl();

    //获得当前队伍列表，使用举例：getTeamList
    @MyAnnontation(MethodName = "getTeamList")
    public ResponseInf getTeamList(){
        return ResponseInf.setResponse(iDungeonsService.getTeamList(),getRole());
    }

    //获得当副本列表，使用举例：getDungeonsList
    @MyAnnontation(MethodName = "getDungeonsList")
    public ResponseInf getDungeonsList(){
        return ResponseInf.setResponse(iDungeonsService.getStaticDungeonsList(),getRole());
    }

    //创建队伍，使用举例：create dungeonesId，返回teamId；
    @MyAnnontation(MethodName = "create")
    public ResponseInf createTeam(){
        return ResponseInf.setResponse(iDungeonsService.createTeam(intList.get(0), getRole()),getRole());
    }

    //加入队伍，使用举例：join 2233(teamId)；
    @MyAnnontation(MethodName = "join")
    public ResponseInf joinTeam(){
        iDungeonsService.joinTeam(intList.get(0), getRole());
        String list = iDungeonsService.getTeamRoleList(intList.get(0), getRole());;
        return ResponseInf.setResponse(list,getRole());
    }

    //开始副本，使用举例：start 2233(teamId) dungeonesId；
    @MyAnnontation(MethodName = "start")
    public ResponseInf startDungeons(){
        iDungeonsService.startDungeons(intList.get(0)+"",getRole());
        return ResponseInf.setResponse(Const.Fight.START_DUNGEONS,getRole());
    }

    //获得角色，适用于输入参数最后一位为roleId的情况
    private Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
