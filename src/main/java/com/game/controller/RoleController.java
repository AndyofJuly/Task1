package com.game.controller;

import com.game.common.MyAnnontation;
import com.game.dao.ConnectSql;
import com.game.entity.Equipment;
import com.game.entity.Monster;
import com.game.entity.Role;
import com.game.entity.User;
import com.game.entity.store.NpcResource;
import com.game.service.RoleService;
import com.game.service.UserService;
import com.game.service.assis.AssistService;
import com.game.service.assis.InitGame;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 业务逻辑处理，根据不同的输入命令通过反射原理调用不同的方法，使用了spring中的自定义注解来实现
 * @Author andy
 * @create 2020/5/17 1:09
 */
@Service
public class RoleController {
    public static HashMap<Integer,User> userHashMap = new HashMap<>();
    public static HashMap<Integer,Role> roleHashMap = new HashMap<>();
    UserService userService = new UserService();
    RoleService roleService = new RoleService();
    public static String[] strings = new String[]{};

    //用户注册，使用举例：register userName password
    @MyAnnontation(MethodName = "register")
    public String registerMesseage() {
        if(ConnectSql.sql.insertRegister(strings[1],strings[2])){
            return "注册失败，该用户名已有人使用";
        }else {
            return "注册成功";
        }
    }

    //用户登录，使用举例：login userName password
    @MyAnnontation(MethodName = "login")
    public String loginMesseage() {
        if(ConnectSql.sql.selectLogin(strings[1],strings[2])){
            return "登陆成功";
        }else {
            return "用户名或密码错误，登陆失败";
        }
    }

    //角色注册，使用举例（新增职业选择，插入数据库中，并进入游戏时拿到缓存里）：registerR roleName careerName
    @MyAnnontation(MethodName = "registerR")
    public String registerRoleMesseage() { //注册时没有id，id从数据库中拿
        if(ConnectSql.sql.insertRegisterRole(strings[1],AssistService.checkCareerId(strings[2]))){
            return "注册成功，请进行登录";
        }else {
            return "注册失败，该角色名称已有人使用";
        }
    }

    //角色登录，使用举例：loginR roleName
    @MyAnnontation(MethodName = "loginR")
    public String loginRoleMesseage() {
        return userService.loginRole(strings[1],ConnectSql.sql.selectRoleIdByName(strings[1]));
    }

    //角色移动&场景切换，使用举例：move scene
    @MyAnnontation(MethodName = "move")
    public String moveMesseage() {
        if(roleService.moveTo(strings[1],Integer.parseInt(strings[2]))){
            return "移动成功";
        }
        return "不可以从这里去这个地方";
    }

    //获取当前场景信息，使用举例：aoi
    @MyAnnontation(MethodName = "aoi")
    public String aoiMesseage() {
        return roleService.placeDetail(InitGame.scenes.get(RoleController.roleHashMap.get(Integer.parseInt(strings[1])).
                getNowScenesId()).getName());
    }

    //查找任意场景信息，使用举例：checkPlace sceneName
    @MyAnnontation(MethodName = "checkPlace")
    public String checkPlaceMesseage() {
        return roleService.placeDetail(strings[1]);
    }

    //与NPC对话，使用举例：talkTo npcName
    @MyAnnontation(MethodName = "talkTo")
    public String talkToNpc(){
        int key = AssistService.checkNpcId(strings[1],Integer.parseInt(strings[2]));
        return NpcResource.npcsStatics.get(key).getWords();
    }

    //修理装备，使用举例：repair equipmentName
    @MyAnnontation(MethodName = "repair")
    public String repair(){
        Equipment equipment = roleService.repairEquipment(strings[1],Integer.parseInt(strings[2]));
        return "修理成功！当前武器耐久为："+equipment.getDura();
    }

    //穿戴装备，使用举例：putOn equipmentName
    @MyAnnontation(MethodName = "putOn")
    public String putOn(){
        roleService.putOnEquipment(strings[1],Integer.parseInt(strings[2]));
        return "你已成功装备该武器";
    }

    //卸下装备，使用举例：takeOff equipmentName
    @MyAnnontation(MethodName = "takeOff")
    public String takeOff(){
        roleService.takeOffEquipment(strings[1],Integer.parseInt(strings[2]));
        return "你已成功卸下该武器";
    }

    //使用药品，使用举例：use potionName
    @MyAnnontation(MethodName = "use")
    public String use(){
        if(roleService.useDrug(strings[1],Integer.parseInt(strings[2]))){
            return "使用成功";
        }
        return "已用完";
    }

    //额外的一些小功能，自己运行程序时时方便观察变量情况
    //返回角色的hp，mp，武器耐久，当前攻击力，使用举例：getInfo
    @MyAnnontation(MethodName = "getInfo")
    public String getInfo(){
        Role role = RoleController.roleHashMap.get(Integer.parseInt(strings[1]));
        int weaponId =0;
        for (Integer key : role.getEquipmentHashMap().keySet()) { //todo 改为一个容量
            weaponId = key;
        }
        return "当前角色的hp："+role.getHp()+
                "。 mp："+role.getMp()+
                "。 武器耐久："+role.getEquipmentHashMap().get(weaponId).getDura()+
                "。 攻击力："+role.getAtk()+
                "。 金钱："+role.getMoney();
    }

    //返回怪物当前状态，使用举例：getMonster monsterName
    @MyAnnontation(MethodName = "getMonster")
    public String getMonster(){
        String key = AssistService.checkMonsterId(strings[1],Integer.parseInt(strings[2]));
        Monster nowMonster = InitGame.scenes.get(RoleController.roleHashMap.get(Integer.parseInt(strings[2]))
                .getNowScenesId()).getMonsterHashMap().get(key);
        return "该怪物hp：" + nowMonster.getMonsterHp() + "，存活状态："+ nowMonster.getAlive();
    }

    //任意场景可以pk玩家，使用举例：pk skillName ss(对方roleId)
    @MyAnnontation(MethodName = "pk")
    public String pkPlayer(){
        return roleService.pkPlayer(strings[1],ConnectSql.sql.selectRoleIdByName(strings[2]),Integer.parseInt(strings[3]));
    }

    //获得当前视野范围内的其他角色，使用举例：view
    @MyAnnontation(MethodName = "view")
    public String getView(){
        return roleService.getView(Integer.parseInt(strings[1]));
    }

    //角色在场景内移动，使用举例：walk x y
    @MyAnnontation(MethodName = "walk")
    public String walkTo(){
        int[] position = roleService.walkTo(Integer.parseInt(strings[1]),
                Integer.parseInt(strings[2]),Integer.parseInt(strings[3]));
        return "你目前的位置坐标为：["+position[0]+","+position[1]+"]";
    }

    //调试代码用的测试
    @MyAnnontation(MethodName = "test")
    public String testCode(){
        return roleService.testCode(Integer.parseInt(strings[1]),Integer.parseInt(strings[2]));
    }


}
