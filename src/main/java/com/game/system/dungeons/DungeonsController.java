package com.game.system.dungeons;

import com.game.common.Const;
import com.game.system.role.RoleController;
import com.game.system.role.pojo.Role;
import com.game.common.ResponseInf;
import com.game.system.assist.GlobalInfo;
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
    private DungeonsService dungeonsService = new DungeonsService();

    //获得所有的队伍列表，使用举例：getTeamList
    @MyAnnontation(MethodName = "getAllTeam")
    public ResponseInf getTeamList(){
        return ResponseInf.setResponse(dungeonsService.getTeamList(),getRole());
    }

    //获得当副本列表，使用举例：getDungeonsList
    @MyAnnontation(MethodName = "getDungeonsList")
    public ResponseInf getDungeonsList(){
        return ResponseInf.setResponse(dungeonsService.getStaticDungeonsList(),getRole());
    }

    //创建队伍，使用举例：create dungeonesId，返回teamId；
    @MyAnnontation(MethodName = "create")
    public ResponseInf createTeam(){
        return ResponseInf.setResponse(dungeonsService.createTeam(intList.get(0), getRole()),getRole());
    }

    //加入队伍，使用举例：join 2233(teamId)；
    @MyAnnontation(MethodName = "join")
    public ResponseInf joinTeam(){
        dungeonsService.joinTeam(intList.get(0), getRole());
        String list = dungeonsService.getTeamRoleList(intList.get(0), getRole());;
        return ResponseInf.setResponse(list,getRole());
    }

    //开始副本，使用举例：start 2233(teamId) dungeonesId；
    @MyAnnontation(MethodName = "start")
    public ResponseInf startDungeons(){
        dungeonsService.startDungeons(intList.get(0)+"",getRole());
        return ResponseInf.setResponse(Const.Fight.START_DUNGEONS,getRole());
    }

    //获取该队伍中其他角色 teamMember teamId
    @MyAnnontation(MethodName = "teamMember")
    public ResponseInf getTeamMember(){
        String list = dungeonsService.getTeamRoleList(intList.get(0), getRole());
        return ResponseInf.setResponse(list,getRole());
    }


    //获得角色，适用于输入参数最后一位为roleId的情况
    private Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
