package com.game.controller;

import com.game.common.Const;
import com.game.common.MyAnnontation;
import com.game.dao.RoleMapper;
import com.game.entity.Role;
import com.game.service.GameService;
import com.game.service.RoleService;
import com.game.service.assis.GlobalResource;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

/**
 * 业务逻辑处理，根据不同的输入命令通过反射原理调用不同的方法，使用了spring中的自定义注解来实现
 * @Author andy
 * @create 2020/5/17 1:09
 */
@Controller
public class RoleController {

    private static ArrayList<Integer> intList  = new ArrayList<>();
    private static ArrayList<String> strList  = new ArrayList<>();
    GameService gameService = new GameService();
    RoleService roleService = new RoleService();
    RoleMapper roleMapper = new RoleMapper();

    //用户注册，使用举例：register userName password
    @MyAnnontation(MethodName = "register")
    public String registerMesseage() {
        if(roleMapper.insertRegister(strList.get(1),strList.get(2))){
            return  Const.start.UREGISTER_FAILURE;
        }else {
            return Const.start.UREGISTER_SUCCESS;
        }
    }

    //用户登录，使用举例：login userName password
    @MyAnnontation(MethodName = "login")
    public String loginMesseage() {
        if(roleMapper.selectLogin(strList.get(1),strList.get(2))){
            return Const.start.ULOGIN_SUCCESS;
        }else {
            return Const.start.ULOGIN_FAILURE;
        }
    }

    //角色注册，使用举例（新增职业选择，插入数据库中，并进入游戏时拿到缓存里）：registerR roleName careerName:careerId
    @MyAnnontation(MethodName = "registerR")
    public String registerRoleMesseage() { //注册时没有id，id从数据库中拿
        if(gameService.registerRole(strList.get(1),intList.get(0))){
            return Const.start.REGISTER_SUCCESS;
        }else {
            return Const.start.REGISTER_FAILURE;
        }
    }

    //角色登录，使用举例：loginR roleId
    @MyAnnontation(MethodName = "loginR")
    public String loginRoleMesseage() {
        return gameService.loginRole(intList.get(0));
    }

    //角色登出，使用举例：loginRoleOut-暂用于测试数据库保存
    @MyAnnontation(MethodName = "save")
    public String saveDataBase() {
        return gameService.saveDataBase();
    }

    //角色移动&场景切换，使用举例：move scene：here修改成sceneId
    @MyAnnontation(MethodName = "move")
    public String moveMesseage() {
        if(roleService.moveTo(intList.get(0),getRole())){
            return Const.service.MOVE_SUCCESS;
        }
        return Const.service.MOVE_FAILURE;
    }

    //获取当前场景信息，使用举例：aoi
    @MyAnnontation(MethodName = "aoi")
    public String aoiMesseage() {
        return roleService.printSceneDetail(getRole().getNowScenesId());
    }

    //查找任意场景信息，使用举例：checkPlace sceneName ：here修改成sceneId
    @MyAnnontation(MethodName = "checkPlace")
    public String checkPlaceMesseage() {
        return roleService.printSceneDetail(intList.get(0));
    }

    //与NPC对话，使用举例：talkTo npcName ：here修改成npcId
    @MyAnnontation(MethodName = "talkTo")
    public String talkToNpc(){
        return roleService.getNpcReply(intList.get(0),getRole());
    }

    //修理装备，使用举例：repair equipmentName：here修改成equipmentId
    @MyAnnontation(MethodName = "repair")
    public String repair(){
        return roleService.repairEquipment(intList.get(0),getRole());
    }

    //穿戴装备，使用举例：putOn equipmentName：here修改成equipmentId
    @MyAnnontation(MethodName = "putOn")
    public String putOn(){
        return roleService.putOnEquipment(intList.get(0),getRole());
    }

    //卸下装备，使用举例：takeOff equipmentName：here修改成equipmentId
    @MyAnnontation(MethodName = "takeOff")
    public String takeOff(){
        return roleService.takeOffEquipment(intList.get(0),getRole());
    }

    //使用药品，使用举例：use potionName：here修改成potionId
    @MyAnnontation(MethodName = "use")
    public String use(){
        if(roleService.useDrug(intList.get(0),getRole())){
            return Const.service.USE_SUCCESS;
        }
        return Const.service.USE_FAILURE;
    }

    //返回角色的hp，mp，武器耐久，当前攻击力，使用举例：getInfo
    @MyAnnontation(MethodName = "getInfo")
    public String getInfo(){
        return roleService.getRoleInfo(getRole());
    }

    //返回怪物当前状态，使用举例：getMonster monsterName：here修改成monsterId
    @MyAnnontation(MethodName = "getMonster")
    public String getMonster(){
        return roleService.getMonsterInfo(intList.get(0),getRole());
    }

    //任意场景可以pk玩家，使用举例：pk skillName ss(对方roleId)：here修改成skillId和对方roleId
    @MyAnnontation(MethodName = "pk")
    public String pkPlayer(){
        return roleService.pkPlayer(intList.get(0), intList.get(1),getRole());
    }

    //获得当前视野范围内的其他角色，使用举例：view
    @MyAnnontation(MethodName = "view")
    public String getView(){
        return roleService.printViewDetail(getRole());
    }

    //角色在场景内移动，使用举例：walk x y
    @MyAnnontation(MethodName = "walk")
    public String walkTo(){
        return roleService.walkTo(intList.get(0),intList.get(1),getRole());
    }

    //调试代码用的测试
    @MyAnnontation(MethodName = "test")
    public String testCode(){
        return roleService.testCode(getRole());
    }

    //添加好友申请 askFriend friendId (roleId)
    @MyAnnontation(MethodName = "askFriend")
    public String askFriend(){
        roleService.askFriend(intList.get(0),getRole());
        return "申请已提交";
    }

    //同意添加好友 addFriend friendId (roleId)
    @MyAnnontation(MethodName = "addFriend")
    public String addFriend(){
        roleService.addFriend(intList.get(0),getRole());
        return "已添加好友";
    }

    //升级-测试 levelUp level (roleId)
    //后续扩展为打怪获得经验升级，每一级所需经验使用配置表配置
    @MyAnnontation(MethodName = "levelUp")
    public String levelUp(){
        roleService.levelUp(intList.get(0),getRole());
        return "已升级";
    }

    //获取自己的成就 getAchievment
    @MyAnnontation(MethodName = "getAchievment")
    public String getAchievment(){
        return roleService.getAchievment(getRole());
    }



    //封装
    public static ArrayList<Integer> getIntList() {
        return intList;
    }

    public static void setIntList(ArrayList<Integer> intList) {
        RoleController.intList = intList;
    }

    public static ArrayList<String> getStrList() {
        return strList;
    }

    public static void setStrList(ArrayList<String> strList) {
        RoleController.strList = strList;
    }

    //获得角色，适用于输入参数最后一位为roleId的情况
    public Role getRole(){
        return GlobalResource.getRoleHashMap().get(intList.get(intList.size()-1));
    }

}
