package com.game.system.dungeons;

import com.game.common.Const;
import com.game.system.gameserver.GameController;
import com.game.system.role.entity.Role;
import com.game.common.ResponseInf;
import com.game.system.gameserver.GlobalInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.game.common.MyAnnontation;

import java.util.ArrayList;

/**
 * 副本模块调用方法入口
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Controller("dungeonsController")
public class DungeonsController {

    private ArrayList<Integer> intList = GameController.getIntList();

    @Autowired
    private DungeonsService dungeonsService;

    /** 获得所有的队伍列表，使用方式：getTeamList */
    @MyAnnontation(MethodName = "getAllTeam")
    public ResponseInf getTeamList(){
        return ResponseInf.setResponse(dungeonsService.getTeamList(),getRole());
    }

    /** 获得当副本列表，使用方式：getDungeonsList */
    @MyAnnontation(MethodName = "getDungeonsList")
    public ResponseInf getDungeonsList(){
        return ResponseInf.setResponse(dungeonsService.getStaticDungeonsList(),getRole());
    }

    /** 创建队伍，使用方式：create dungeonsId */
    @MyAnnontation(MethodName = "create")
    public ResponseInf createTeam(){
        return ResponseInf.setResponse(dungeonsService.createTeam(intList.get(0), getRole()),getRole());
    }

    /** 加入队伍，使用方式：join teamId */
    @MyAnnontation(MethodName = "join")
    public ResponseInf joinTeam(){
        dungeonsService.joinTeam(String.valueOf(intList.get(0)), getRole());
        String list = dungeonsService.getTeamRoleList(intList.get(0), getRole());;
        return ResponseInf.setResponse(list,getRole());
    }

    /** 开始副本，使用方式：start teamId dungeonsId */
    @MyAnnontation(MethodName = "start")
    public ResponseInf startDungeons(){
        dungeonsService.startDungeons(String.valueOf(intList.get(0)),getRole());
        return ResponseInf.setResponse(Const.Fight.START_DUNGEONS,getRole());
    }

    /** 获取该队伍中其他角色，使用方式：teamMember teamId */
    @MyAnnontation(MethodName = "teamMember")
    public ResponseInf getTeamMember(){
        String list = dungeonsService.getTeamRoleList(intList.get(0), getRole());
        return ResponseInf.setResponse(list,getRole());
    }

    /** 根据输入获得角色，输入参数最后一位为roleId */
    private Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
