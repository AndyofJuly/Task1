package com.game.service.impl;

import com.game.common.Const;
import com.game.entity.Role;
import com.game.entity.Team;
import com.game.entity.excel.DungeonsStatic;
import com.game.entity.store.DungeonsResource;
import com.game.service.IAchievementService;
import com.game.service.IDungeonsService;
import com.game.service.IRoleService;
import com.game.service.ISceneService;
import com.game.service.assist.*;

import java.util.ArrayList;
import java.util.Timer;

/**
 * @Author andy
 * @create 2020/6/15 15:08
 */
public class DungeonsServiceImpl implements IDungeonsService {
    private IAchievementService iAchievementService = new AchievementServiceImpl();
    private ISceneService iSceneService = new SceneServiceImpl();

    //创建队伍，返回一个队伍id，并将该id加入到全局的teamList中
    @Override
    public String createTeam(int dungeonesId, Role role){
        String teamId = AssistService.generateTeamId();
        GlobalInfo.getTeamList().put(teamId,new Team(teamId,dungeonesId));
        joinTeam(Integer.parseInt(teamId),role);
        return Const.Fight.CREATE_SUCCESS+teamId;
    }

    //加入队伍，角色放入队伍集合中，返回队伍中的角色列表
    @Override
    public void joinTeam(int teamId, Role role){
        String newId= teamId+"";
        GlobalInfo.getTeamList().get(newId).getRoleList().add(role.getId());
        iAchievementService.ifFirstJoinTeam(role);
        getRoleList(newId);
    }

    @Override
    public String getTeamRoleList(int teamId,Role role){
        ArrayList<Integer> roleList = getRoleList(teamId+"");//joinTeam(teamId,role);
        String output=Const.Fight.TEAM_ROLELIST;
        for(Integer roleId : roleList){
            output = output + GlobalInfo.getRoleHashMap().get(roleId).getName()+" ";
        }
        return output;
    }

    //难度最大的方法，其中还涉及了其他比较多的方法，这些方法需要提取到外部，统一化管理
    //创建临时副本以开始副本，玩家一起攻打BOSS
    @Override
    public void startDungeons (String teamId, Role role){
        Team team = GlobalInfo.getTeamList().get(teamId);
        int tempSceneId = iSceneService.createTempScene(team.getDungeonsId());
        //队伍角色进入副本中
        for(int i=0;i<team.getRoleList().size();i++){
            IRoleService iRoleService = new RoleServiceImpl();
            iRoleService.moveTo(tempSceneId, GlobalInfo.getRoleHashMap().get(team.getRoleList().get(i)));
        }
        //如果角色距离boss较近，调用boss定时攻击角色的方法
        int nowScenesId = role.getNowScenesId();
        bossAttackRole(teamId,team.getDungeonsId(),nowScenesId);
    }

    //boss定时使用技能攻击角色方法，简单AI
    @Override
    public void bossAttackRole(String teamId,int dungeonsId,int sceneId){
        Timer timer = new Timer();
        BossAttack bossAttack = new BossAttack(timer,teamId,dungeonsId,sceneId);
        timer.schedule(bossAttack, Const.DELAY_TIME, Const.GAP_TIME_BOSS);
    }

    //获取队伍中的角色列表，传参为teamId，返回角色列表集合元素信息，集合中为每个roleName
    @Override
    public ArrayList<Integer> getRoleList(String teamId){
        return GlobalInfo.getTeamList().get(teamId).getRoleList();
    }

    @Override
    public String getTeamList(){
        String output = Const.Fight.TEAM_LIST;
        for(String teamId : GlobalInfo.getTeamList().keySet()){
            output+=teamId+"; ";
        }
        return output;
    }

    //副本列表初始化，返回副本列表集合元素信息，包括id和副本名
    @Override
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
}
