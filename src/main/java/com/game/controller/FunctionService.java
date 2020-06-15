package com.game.controller;

import com.game.common.MyAnnontation;
import com.game.dao.ConnectSql;
import com.game.entity.Role;
import com.game.entity.User;
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
public class FunctionService {
    public static HashMap<Integer,User> userHashMap = new HashMap<>();
    public static HashMap<Integer,Role> roleHashMap = new HashMap<>();
    UserService userService = new UserService();
    RoleService roleService = new RoleService();
    public static String[] strings = new String[]{};

    //用户注册，使用举例：register userName password
    @MyAnnontation(MethodName = "register")
    public String registerMesseage() {
        return userService.register(strings[1],strings[2]);
    }

    //用户登录，使用举例：login userName password
    @MyAnnontation(MethodName = "login")
    public String loginMesseage() {
        return userService.login(strings[1],strings[2])+"";
    }

    //角色注册，使用举例（新增职业选择，插入数据库中，并进入游戏时拿到缓存里）：registerR roleName careerName
    @MyAnnontation(MethodName = "registerR")
    public String registerRoleMesseage() { //注册时没有id，id从数据库中拿
        return userService.registerRole(strings[1],AssistService.checkCareerId(strings[2]));
    }

    //角色登录，使用举例：loginR roleName
    @MyAnnontation(MethodName = "loginR")
    public String loginRoleMesseage() {
        return userService.loginRole(strings[1],ConnectSql.sql.selectRoleIdByName(strings[1]));
    }

    //角色移动&场景切换，使用举例：move scene
    @MyAnnontation(MethodName = "move")
    public String moveMesseage() {
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
        return roleService.checkPlace(strings[1]);
    }

    //与NPC对话，使用举例：talkTo npcName
    @MyAnnontation(MethodName = "talkTo")
    public String talkToNpc(){
        return roleService.getNpcReply(strings[1],Integer.parseInt(strings[2]));
    }

    //修理装备，使用举例：repair equipmentName
    @MyAnnontation(MethodName = "repair")
    public String repair(){
        return roleService.repairEquipment(strings[1],Integer.parseInt(strings[2]));
    }

    //穿戴装备，使用举例：putOn equipmentName
    @MyAnnontation(MethodName = "putOn")
    public String putOn(){
        return roleService.putOnEquipment(strings[1],Integer.parseInt(strings[2]));
    }

    //卸下装备，使用举例：takeOff equipmentName
    @MyAnnontation(MethodName = "takeOff")
    public String takeOff(){
        return roleService.takeOffEquipment(strings[1],Integer.parseInt(strings[2]));
    }

    //使用药品，使用举例：use potionName
    @MyAnnontation(MethodName = "use")
    public String use(){
        return roleService.useDrug(strings[1],Integer.parseInt(strings[2]));
    }

    //技能攻击，使用举例：skill skillName monsterName
    @MyAnnontation(MethodName = "skill")
    public String useSkillAttack(){
        String key = AssistService.checkMonsterId(strings[2],Integer.parseInt(strings[3]));//不是在静态资源中找怪物，而是在该场景下找
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
        return roleService.getMonsterInfo(strings[1],Integer.parseInt(strings[2]));
    }

    //获得商店的商品列表，使用举例：getGoodsList
    @MyAnnontation(MethodName = "getGoodsList")
    public String getGoodsList(){
        return roleService.getGoodsList();
    }

    //获得当前队伍列表，使用举例：getTeamList
    @MyAnnontation(MethodName = "getTeamList")
    public String getTeamList(){
        return AssistService.getTeamIdList();
    }

    //获得当副本列表，使用举例：getDungeonsList
    @MyAnnontation(MethodName = "getDungeonsList")
    public String getDungeonsList(){
        return InitGame.dungeonsList;
    }

    //购买药品or装备，使用举例：buy 清泉酒 20
    @MyAnnontation(MethodName = "buy")
    public String buyGoods(){
        return roleService.buyGoods(strings[1],strings[2],Integer.parseInt(strings[3]));
    }

    //创建队伍，使用举例：create dungeonesId，返回teamId；
    @MyAnnontation(MethodName = "create")
    public String createTeam(){
        return roleService.createTeam(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
    }

    //加入队伍，使用举例：join 2233(teamId) ；todo 掉线时队伍的状态管理
    @MyAnnontation(MethodName = "join")
    public String joinTeam(){
        return roleService.joinTeam(strings[1],Integer.parseInt(strings[2]));
    }

    //开始副本，使用举例：start 2233(teamId) dungeonesId；todo 掉线时队伍的状态管理和副本的状态管理
    @MyAnnontation(MethodName = "start")
    public String startDungeons(){
        return roleService.startDungeons(strings[1],Integer.parseInt(strings[2]));
    }

    //任意场景可以pk玩家，使用举例：pk skillName ss(对方roleId)
    @MyAnnontation(MethodName = "pk")
    public String pkPlayer(){
        return roleService.pkPlayer(strings[1],ConnectSql.sql.selectRoleIdByName(strings[2]),Integer.parseInt(strings[3]));
    }

    // 全服聊天：在netty服务端进行处理：say words...
    // 私人聊天：在netty服务端进行处理：sayTo roleName words...

    //发送邮件，使用举例：email ss(roleId) words... goods；假设一次只能邮寄一个物品
    @MyAnnontation(MethodName = "email")
    public String emailToPlayer(){
        return roleService.emailToPlayer(Integer.parseInt(strings[1]),strings[2],strings[3],Integer.parseInt(strings[4]));
    }

/*   待调整
    //扩展方法蓝药缓慢恢复demo
    @MyAnnontation(MethodName = "sR")
    public String slowlyRecoverd(){
        return roleService.slowlyRecoverd();
    }

    //扩展方法毒素技能和护盾技能
    @MyAnnontation(MethodName = "sK")
    public String useSkill(){
        return roleService.useSkill(strings[1],strings[2]);
    }*/

    //显示当前自己的职业有什么技能
    @MyAnnontation(MethodName = "skillList")
    public String getSkillList(){
        return roleService.getSkillList(Integer.parseInt(strings[1]));
    }

    //普通攻击技能测试，使用举例：atk skillName monsterName todo 与前面的skill命令合并
    @MyAnnontation(MethodName = "atk")
    public String atkSkill(){
        return roleService.atkSkill(strings[1],strings[2],Integer.parseInt(strings[3]));
    }

    //嘲讽技能测试，使用举例：taunt
    @MyAnnontation(MethodName = "taunt")
    public String tauntSkill(){
        return roleService.tauntSkill(Integer.parseInt(strings[1]));
    }

    //群伤技能测试，使用举例：groupAtk skillName 默认对当前场景的所有怪物造成群伤，可扩展：传参为怪物集合
    @MyAnnontation(MethodName = "groupAtk")
    public String groupAtkSkill(){
        return roleService.groupAtkSkill(strings[1],Integer.parseInt(strings[2]));
    }

    //群回复技能测试，使用举例：groupHile skillName 默认对当前场景的所有角色，可扩展：传参为角色集合
    @MyAnnontation(MethodName = "groupCure")
    public String groupCureSkill(){
        return roleService.groupCureSkill(strings[1],Integer.parseInt(strings[2]));
    }

    //召唤技能测试，使用举例：summon monsterName
    @MyAnnontation(MethodName = "summon")
    public String summonSkill(){
        return roleService.summonSkill(strings[1],Integer.parseInt(strings[2]));
    }

    //调试代码用的测试
    @MyAnnontation(MethodName = "test")
    public String testCode(){
        return roleService.testCode(Integer.parseInt(strings[1]));
    }


}
