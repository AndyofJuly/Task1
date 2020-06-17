package com.game.service;

import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.entity.Role;
import com.game.entity.Team;
import com.game.entity.store.SceneResource;
import com.game.service.assis.*;

import java.util.ArrayList;
import java.util.Timer;

/**
 * @Author andy
 * @create 2020/6/15 15:08
 */
public class DungeonsService {

    static RoleService roleService = new RoleService();

    //创建队伍，返回一个队伍id，并将该id加入到全局的teamList中
    public static String createTeam(int dungeonesId, int roleId){
        String teamId = IdGenerator.generateTeamId();
        DynamicResource.teamList.put(teamId,new Team(teamId,dungeonesId));
        joinTeam(teamId,roleId);
        return teamId;
    }

    //加入队伍，角色放入队伍集合中，返回队伍中的角色列表
    public static ArrayList<Integer> joinTeam(String teamId, int roleId){
        DynamicResource.teamList.get(teamId).getRoleList().add(roleId);
        return getRoleList(teamId);
    }

    //难度最大的方法，其中还涉及了其他比较多的方法，这些方法需要提取到外部，统一化管理
    //创建临时副本以开始副本，玩家一起攻打BOSS
    public static void startDungeons (String teamId, int roleId){
        Team team = DynamicResource.teamList.get(teamId);
        String target = InitGame.scenes.get(TempSceneService.createTempScene(team.getDungeonsId())).getName();
        //队伍角色进入副本中
        for(int i=0;i<team.getRoleList().size();i++){
            roleService.moveTo(target,team.getRoleList().get(i));
        }
        //如果角色距离boss较近，调用boss定时攻击角色的方法
        bossAttackRole(teamId,team.getDungeonsId(), RoleController.roleHashMap.get(roleId).getNowScenesId());
    }

    //boss定时使用技能攻击角色方法，简单AI
    public static void bossAttackRole(String teamId,int dungeonsId,int sceneId){
        Timer timer = new Timer();
        BossAttack bossAttack = new BossAttack(timer,teamId,dungeonsId,sceneId);
        timer.schedule(bossAttack, Const.DELAY_TIME, Const.GAP_TIME_BOSS);
    }

    //获取队伍中的角色列表，传参为teamId，返回角色列表集合元素信息，集合中为每个roleName
    public static ArrayList<Integer> getRoleList(String teamId){
        return DynamicResource.teamList.get(teamId).getRoleList();
    }
}
