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

/**
 * 业务逻辑处理，根据不同的输入命令通过反射原理调用不同的方法，使用了spring中的自定义注解来实现
 * @Author andy
 * @create 2020/5/17 1:09
 */
@Service
public class FunctionService {
    public static User user;
    public static Role role;
    UserService userService = new UserService();
    RoleService roleService = new RoleService();
    public static String[] strings = new String[]{};
    ConnectSql connectSql = new ConnectSql();


    @MyAnnontation(MethodName = "register")
    public String registerMesseage() {
        if(strings.length<=2){return "";}
        return userService.register(strings[1],strings[2]);
    }

    @MyAnnontation(MethodName = "login")
    public String loginMesseage() {
        if(strings.length<=2){return "";}
        user = new User(strings[1],strings[2]);
        return userService.login(strings[1],strings[2]);
    }

    @MyAnnontation(MethodName = "registerR")
    public String registerRoleMesseage() {
        if(strings.length<=1){return "";}
        role = new Role(strings[1], InitStaticResource.initSceneId);
        user.setRole(role);
        role.setId(connectSql.selectRoleIdByName(strings[1]));
        new InitRole();
        return userService.registerRole(strings[1]);
    }

    @MyAnnontation(MethodName = "loginR")
    public String loginRoleMesseage() {
        if(strings.length<=1){return "";}
        role = new Role(strings[1],connectSql.selectRoleScenesId(strings[1]));
        user.setRole(role);
        new InitRole();
        return userService.loginRole(strings[1]);
    }


    @MyAnnontation(MethodName = "move")
    public String moveMesseage() {
        if(strings.length<=1){return "";}
        return roleService.move(strings[1]);
    }

    @MyAnnontation(MethodName = "aoi")
    public String aoiMesseage() {
        return roleService.aoi();
    }

    @MyAnnontation(MethodName = "checkPlace")
    public String checkPlaceMesseage() {
        if(strings.length<=1){return "";}
        return roleService.checkPlace(strings[1]);
    }

    @MyAnnontation(MethodName = "talkTo")
    public String talkToNpc(){
        if(strings.length<=1){return "";}
        return roleService.getNpcReply(strings[1]);
    }

    @MyAnnontation(MethodName = "repair")
    public String repair(){
        if(strings.length<=1){return "";}
        return roleService.repairEquipment(strings[1]);
    }

    @MyAnnontation(MethodName = "putOn")
    public String putOn(){
        if(strings.length<=1){return "";}
        return roleService.putOnEquipment(strings[1]);
    }

    @MyAnnontation(MethodName = "takeOff")
    public String takeOff(){
        if(strings.length<=1){return "";}
        return roleService.takeOffEquipment(strings[1]);
    }

    @MyAnnontation(MethodName = "use")
    public String use(){
        if(strings.length<=1){return "";}
        return roleService.useDrug(strings[1]);
    }

    //额外的一些小功能，自己运行程序时时方便观察变量情况
    //返回角色的hp，mp，武器耐久，当前攻击力
    @MyAnnontation(MethodName = "getInfo")
    public String getInfo(){
        return roleService.getRoleInfo();
    }

}
