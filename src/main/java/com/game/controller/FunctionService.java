package com.game.controller;

import com.game.common.InitRole;
import com.game.common.InitStaticResource;
import com.game.common.MyAnnontation;
import com.game.dao.ConnectSql;
import com.game.entity.Role;
import com.game.entity.User;
import com.game.service.RoleService;
import com.game.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 业务逻辑处理，根据不同的输入命令通过反射原理调用不同的方法，使用了spring中的自定义注解来实现
 * @Author andy
 * @create 2020/5/17 1:09
 */
@Service
public class FunctionService {
    //public static User user;
    //public static Role role;
    public static HashMap<Integer,User> userHashMap = new HashMap<>();
    public static HashMap<Integer,Role> roleHashMap = new HashMap<>();
    UserService userService = new UserService();
    RoleService roleService = new RoleService();
    public static String[] strings = new String[]{};
    ConnectSql connectSql = new ConnectSql();

    //用户注册
    @MyAnnontation(MethodName = "register")
    public String registerMesseage() {
        if(strings.length<=2){return "0";}
        return userService.register(strings[1],strings[2]);
    }

    //用户登录
    @MyAnnontation(MethodName = "login")
    public String loginMesseage() {
        if(strings.length<=2){return "0";}
        //user = new User(strings[1],strings[2]);
        int getRoleId = userService.login(strings[1],strings[2]);
        userHashMap.put(getRoleId,new User(strings[1],strings[2]));
        return userService.login(strings[1],strings[2])+"";
    }

    //角色注册
    @MyAnnontation(MethodName = "registerR")
    public String registerRoleMesseage() {
        if(strings.length<=1){return "0";}
        roleHashMap.put(connectSql.selectRoleIdByName(strings[1]),new Role(strings[1], InitStaticResource.initSceneId));
        //role = new Role(strings[1], InitStaticResource.initSceneId);
        //user.setRole(role);
        //role.setId(connectSql.selectRoleIdByName(strings[1]));
        InitRole.init(connectSql.selectRoleIdByName(strings[1]));
        return userService.registerRole(strings[1],connectSql.selectRoleIdByName(strings[1]));
    }

    //角色登录
    @MyAnnontation(MethodName = "loginR")
    public String loginRoleMesseage() {
        if(strings.length<=1){return "0";}
        roleHashMap.put(connectSql.selectRoleIdByName(strings[1]),new Role(strings[1],connectSql.selectRoleScenesId(strings[1])));
        //role = new Role(strings[1],connectSql.selectRoleScenesId(strings[1]));
        //user.setRole(role);
        InitRole.init(connectSql.selectRoleIdByName(strings[1]));
        return userService.loginRole(strings[1],connectSql.selectRoleIdByName(strings[1]));
    }

    //角色移动&场景切换
    @MyAnnontation(MethodName = "move")
    public String moveMesseage() {
        if(strings.length<=1){return "0";}
        return roleService.move(strings[1],Integer.valueOf(strings[2]));
    }

    //获取当前场景信息
    @MyAnnontation(MethodName = "aoi")
    public String aoiMesseage() {
        return roleService.aoi(Integer.valueOf(strings[1]));
    }

    //查找任意场景信息
    @MyAnnontation(MethodName = "checkPlace")
    public String checkPlaceMesseage() {
        if(strings.length<=1){return "0";}
        return roleService.checkPlace(strings[1]);
    }

    //与NPC对话
    @MyAnnontation(MethodName = "talkTo")
    public String talkToNpc(){
        if(strings.length<=1){return "0";}
        return roleService.getNpcReply(strings[1],Integer.valueOf(strings[2]));
    }

    //修理装备
    @MyAnnontation(MethodName = "repair")
    public String repair(){
        if(strings.length<=1){return "0";}
        return roleService.repairEquipment(strings[1],Integer.valueOf(strings[2]));
    }

    //穿戴装备
    @MyAnnontation(MethodName = "putOn")
    public String putOn(){
        if(strings.length<=1){return "0";}
        return roleService.putOnEquipment(strings[1],Integer.valueOf(strings[2]));
    }

    //卸下装备
    @MyAnnontation(MethodName = "takeOff")
    public String takeOff(){
        if(strings.length<=1){return "0";}
        return roleService.takeOffEquipment(strings[1],Integer.valueOf(strings[2]));
    }

    //使用药品
    @MyAnnontation(MethodName = "use")
    public String use(){
        if(strings.length<=1){return "0";}
        return roleService.useDrug(strings[1],Integer.valueOf(strings[2]));
    }

    //技能攻击
    @MyAnnontation(MethodName = "skill")
    public String useSkillAttack(){
        if(strings.length<=2){return "0";}
        return roleService.useSkillAttack(strings[1],strings[2],Integer.valueOf(strings[3]));
    }

    //额外的一些小功能，自己运行程序时时方便观察变量情况
    //返回角色的hp，mp，武器耐久，当前攻击力
    @MyAnnontation(MethodName = "getInfo")
    public String getInfo(){
        return roleService.getRoleInfo(Integer.valueOf(strings[1]));
    }

    //返回怪物当前状态
    @MyAnnontation(MethodName = "getMonster")
    public String getMonster(){
        if(strings.length<=1){return "0";}
        return roleService.getMonsterInfo(strings[1],Integer.valueOf(strings[1]));
    }

/*    //扩展方法蓝药缓慢恢复demo
    @MyAnnontation(MethodName = "sR")
    public String slowlyRecoverd(){
        return roleService.slowlyRecoverd();
    }

    //扩展方法毒素技能和护盾技能
    @MyAnnontation(MethodName = "sK")
    public String useSkill(){
        if(strings.length<=2){return "";}
        return roleService.useSkill(strings[1],strings[2]);
    }*/
}
