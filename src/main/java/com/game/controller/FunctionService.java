package com.game.controller;

import com.game.service.assis.CheckIdByName;
import com.game.service.assis.InitRole;
import com.game.common.MyAnnontation;
import com.game.dao.ConnectSql;
import com.game.entity.Role;
import com.game.entity.User;
import com.game.entity.store.SceneResource;
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
    public static HashMap<Integer,User> userHashMap = new HashMap<>();
    public static HashMap<Integer,Role> roleHashMap = new HashMap<>();
    UserService userService = new UserService();
    RoleService roleService = new RoleService();
    public static String[] strings = new String[]{};

    //用户注册，使用举例：register userName password
    @MyAnnontation(MethodName = "register")
    public String registerMesseage() {
        if(strings.length<=2){return "0";}
        return userService.register(strings[1],strings[2]);
    }

    //用户登录，使用举例：login userName password
    @MyAnnontation(MethodName = "login")
    public String loginMesseage() {
        if(strings.length<=2){return "0";}
        //user = new User(strings[1],strings[2]);
        int getRoleId = userService.login(strings[1],strings[2]);
        userHashMap.put(getRoleId,new User(strings[1],strings[2]));
        return userService.login(strings[1],strings[2])+"";
    }

    //角色注册，使用举例：registerR roleName
    @MyAnnontation(MethodName = "registerR")
    public String registerRoleMesseage() {
        if(strings.length<=1){return "0";}
        roleHashMap.put(ConnectSql.sql.selectRoleIdByName(strings[1]),new Role(strings[1], SceneResource.initSceneId));
        InitRole.init(ConnectSql.sql.selectRoleIdByName(strings[1]));
        return userService.registerRole(strings[1],ConnectSql.sql.selectRoleIdByName(strings[1]));
    }

    //角色登录，使用举例：loginR roleName
    @MyAnnontation(MethodName = "loginR")
    public String loginRoleMesseage() {
        if(strings.length<=1){return "0";}
        roleHashMap.put(ConnectSql.sql.selectRoleIdByName(strings[1]),new Role(strings[1],ConnectSql.sql.selectRoleScenesId(strings[1])));
        InitRole.init(ConnectSql.sql.selectRoleIdByName(strings[1]));
        return userService.loginRole(strings[1],ConnectSql.sql.selectRoleIdByName(strings[1]));
    }

    //角色移动&场景切换，使用举例：move scene
    @MyAnnontation(MethodName = "move")
    public String moveMesseage() {
        if(strings.length<=1){return "0";}
        return roleService.move(strings[1],Integer.parseInt(strings[2]));
    }

    //获取当前场景信息，使用举例：aoi
    @MyAnnontation(MethodName = "aoi")
    public String aoiMesseage() {
        return roleService.aoi(Integer.parseInt(strings[1]));
    }

    //查找任意场景信息，使用举例：checkPlace scene
    @MyAnnontation(MethodName = "checkPlace")
    public String checkPlaceMesseage() {
        if(strings.length<=1){return "0";}
        return roleService.checkPlace(strings[1]);
    }

    //与NPC对话，使用举例：talkTo npcName
    @MyAnnontation(MethodName = "talkTo")
    public String talkToNpc(){
        if(strings.length<=1){return "0";}
        return roleService.getNpcReply(strings[1],Integer.parseInt(strings[2]));
    }

    //修理装备，使用举例：repair equipmentName
    @MyAnnontation(MethodName = "repair")
    public String repair(){
        if(strings.length<=1){return "0";}
        return roleService.repairEquipment(strings[1],Integer.parseInt(strings[2]));
    }

    //穿戴装备，使用举例：putOn equipmentName
    @MyAnnontation(MethodName = "putOn")
    public String putOn(){
        if(strings.length<=1){return "0";}
        return roleService.putOnEquipment(strings[1],Integer.parseInt(strings[2]));
    }

    //卸下装备，使用举例：takeOff equipmentName
    @MyAnnontation(MethodName = "takeOff")
    public String takeOff(){
        if(strings.length<=1){return "0";}
        return roleService.takeOffEquipment(strings[1],Integer.parseInt(strings[2]));
    }

    //使用药品，使用举例：use potionName
    @MyAnnontation(MethodName = "use")
    public String use(){
        if(strings.length<=1){return "0";}
        return roleService.useDrug(strings[1],Integer.parseInt(strings[2]));
    }

    //技能攻击，使用举例：skill skillName monsterName
    @MyAnnontation(MethodName = "skill")
    public String useSkillAttack(){
        if(strings.length<=2){return "0";} // todo
        String key = CheckIdByName.checkMonsterId(strings[2],Integer.parseInt(strings[3]));//不是在静态资源中找怪物，而是在该场景下找
        return roleService.useSkillAttack(strings[1],key,Integer.parseInt(strings[3]));
    }

    //额外的一些小功能，自己运行程序时时方便观察变量情况
    //返回角色的hp，mp，武器耐久，当前攻击力，使用举例：getInfo
    @MyAnnontation(MethodName = "getInfo")
    public String getInfo(){
        return roleService.getRoleInfo(Integer.parseInt(strings[1]));
    }

    //返回怪物当前状态，使用举例：getMonster monsterName
    @MyAnnontation(MethodName = "getMonster")
    public String getMonster(){
        if(strings.length<=1){return "0";}
        return roleService.getMonsterInfo(strings[1],Integer.parseInt(strings[2]));
    }



/*   建议技能类放在一个方法中，调整一下
    //扩展方法蓝药缓慢恢复demo
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

    //调试代码用的测试
    @MyAnnontation(MethodName = "test")
    public String testCode(){
        return roleService.testCode();
    }
}
