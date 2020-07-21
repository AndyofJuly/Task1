package com.game.system.dungeons;

import com.game.netty.server.ServerHandler;
import com.game.system.achievement.observer.FsJoinTeamOb;
import com.game.system.achievement.pojo.Subject;
import com.game.system.gameserver.AssistService;
import com.game.system.gameserver.GlobalInfo;
import com.game.common.Const;
import com.game.system.role.pojo.Role;
import com.game.system.dungeons.pojo.Team;
import com.game.system.dungeons.pojo.DungeonsStatic;
import com.game.system.dungeons.pojo.DungeonsResource;
import com.game.system.role.RoleService;
import com.game.system.scene.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Timer;

/**
 * 副本模块的业务逻辑处理
 * @Author andy
 * @create 2020/6/15 15:08
 */
@Service
public class DungeonsService {
    @Autowired
    private SceneService sceneService;

    /**
     * 创建队伍
     * @param dungeonsId 副本id
     * @param role 角色
     * @return 队伍id
     */
    public String createTeam(int dungeonsId, Role role){
        if(role.getNowScenesId()!=Const.DUNGEONS_START_SCENE){return "请到副本传送点创建队伍";}
        String teamId = AssistService.generateTeamId();
        GlobalInfo.getTeamList().put(teamId,new Team(teamId,dungeonsId));
        joinTeam(teamId,role);
        return Const.Fight.CREATE_SUCCESS+teamId;
    }

    /**
     * 参加队伍
     * @param teamId 队伍id
     * @param role 角色
     */
    public void joinTeam(String teamId, Role role){
        role.setTeamId(teamId);
        GlobalInfo.getTeamList().get(teamId).getRoleList().add(role.getId());

        dungeonsSubject.notifyObserver(0,role);

        ArrayList<Role> roles = getTeamRoles(GlobalInfo.getTeamList().get(teamId).getRoleList());
        ServerHandler.notifyGroupRoles(roles,role.getName()+"加入了队伍");
        getRoleList(teamId);
    }

    /**
     * 获得队伍中角色列表
     * @param teamId 队伍id
     * @param role 角色
     * @return 信息提示
     */
    public String getTeamRoleList(int teamId,Role role){
        ArrayList<Integer> roleList = getRoleList(String.valueOf(teamId));
        String output = Const.Fight.TEAM_ROLELIST;
        for(Integer roleId : roleList){
            output = output + GlobalInfo.getRoleHashMap().get(roleId).getName()+" ";
        }
        return output;
    }

    /**
     * 根据队伍角色id获得队伍角色集合
     * @param roleList 队伍id
     * @return 角色集合
     */
    public static ArrayList<Role> getTeamRoles(ArrayList<Integer> roleList){
        ArrayList<Role> roles = new ArrayList<>();
        for(Integer key: roleList){
            roles.add(GlobalInfo.getRoleHashMap().get(key));
        }
        return roles;
    }

    /**
     * 开始副本
     * @param teamId 队伍id
     * @param role 角色
     */
    public void startDungeons (String teamId, Role role){
        ArrayList<Role> roles = getTeamRoles(GlobalInfo.getTeamList().get(teamId).getRoleList());
        ServerHandler.notifyGroupRoles(roles,"副本已开启，进入到副本中");
        Team team = GlobalInfo.getTeamList().get(teamId);
        int tempSceneId = sceneService.createTempScene(team.getDungeonsId());

        //队伍角色进入副本中
        for(int i=0;i<team.getRoleList().size();i++){
            RoleService roleService = new RoleService();
            sceneService.moveTo(tempSceneId, GlobalInfo.getRoleHashMap().get(team.getRoleList().get(i)));
        }

        int nowScenesId = role.getNowScenesId();
        bossAttackRole(teamId,team.getDungeonsId(),nowScenesId);
    }

    /**
     * Boss定时使用技能攻击角色，简单AI
     * @param teamId 队伍id
     * @param dungeonsId 副本id
     * @param sceneId 场景id
     */
    public void bossAttackRole(String teamId,int dungeonsId,int sceneId){
        Timer timer = new Timer();
        DungeonsBossAI dungeonsBossAI = new DungeonsBossAI(timer,teamId,dungeonsId,sceneId);
        timer.schedule(dungeonsBossAI, Const.DELAY_TIME, Const.GAP_TIME_BOSS);
    }

    /**
     * 获得队伍中角色列表
     * @param teamId 队伍id
     * @return 角色列表集合
     */
    public ArrayList<Integer> getRoleList(String teamId){
        return GlobalInfo.getTeamList().get(teamId).getRoleList();
    }

    /**
     * 获得所有组队列表
     * @return 信息提示
     */
    public String getTeamList(){
        String output = Const.Fight.TEAM_LIST;
        for(String teamId : GlobalInfo.getTeamList().keySet()){
            output+=teamId+"; ";
        }
        return output;
    }

    /**
     * 获得所有副本列表
     * @return String
     */
    public String getStaticDungeonsList(){
        StringBuilder stringBuilder = new StringBuilder("目前可参加的副本有：\n");
        for(Integer key : DungeonsResource.getDungeonsStaticHashMap().keySet()){
            DungeonsStatic dungeons = DungeonsResource.getDungeonsStaticHashMap().get(key);
            stringBuilder.append(dungeons.getId()+":"+
                    dungeons.getName()+"，限时"+
                    dungeons.getDeadTime()+"秒。\n");
        }
        return stringBuilder.toString();
    }

    /** 注册成就观察者 */
    Subject dungeonsSubject = new Subject();
    private FsJoinTeamOb fsJoinTeamOb = new FsJoinTeamOb(dungeonsSubject);
}
