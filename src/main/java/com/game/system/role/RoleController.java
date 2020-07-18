package com.game.system.role;

import com.game.common.Const;
import com.game.common.MyAnnontation;
import com.game.system.assist.PotralDao;
import com.game.system.role.pojo.Role;
import com.game.system.assist.GlobalInfo;
import com.game.common.ResponseInf;
import com.game.system.assist.GameService;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

/**
 * 业务逻辑处理，根据不同的输入命令通过反射原理调用不同的方法，使用了spring中的自定义注解来实现
 * 用户模块调用方法入口
 * @Author andy
 * @create 2020/5/17 1:09
 */
@Controller
public class RoleController {

    private static ArrayList<Integer> intList  = new ArrayList<>();
    private static ArrayList<String> strList  = new ArrayList<>();
    private GameService gameService = new GameService();
    private RoleService roleService = new RoleService();
    private PotralDao potralDao = new PotralDao();

    /** 用户注册，使用方式：register userName password */
    @MyAnnontation(MethodName = "register")
    public ResponseInf registerMesseage() {
        if(potralDao.insertRegister(strList.get(1),strList.get(2))){
            return  ResponseInf.setResponse(Const.start.UREGISTER_FAILURE,null);
        }else {
            return ResponseInf.setResponse(Const.start.UREGISTER_SUCCESS,null);
        }
    }

    /** 用户登录，使用方式：login userName password */
    @MyAnnontation(MethodName = "login")
    public ResponseInf loginMesseage() {
        int userId = potralDao.selectLogin(strList.get(1),strList.get(2));
        if(userId!=0){
            String roleMsg = "，用户id："+userId+"，你目前的角色有：";
            ArrayList<Integer> roleList = potralDao.selectRole(userId);
            for(Integer roleId : roleList){
                roleMsg += roleId+" ";
            }
            return ResponseInf.setResponse(Const.start.ULOGIN_SUCCESS+roleMsg,null);
        }else {
            return ResponseInf.setResponse(Const.start.ULOGIN_FAILURE,null);
        }
    }

    /** 角色注册，使用方式：registerR roleName careerId */
    @MyAnnontation(MethodName = "registerR")
    public ResponseInf registerRoleMesseage() { //注册时没有id，id从数据库中拿
        int result = gameService.registerRole(strList.get(1),intList.get(0));
        if(result!=0){
            potralDao.insertUserId(intList.get(1),result);
            return ResponseInf.setResponse(Const.start.REGISTER_SUCCESS+"，你的角色id为："+result,null);
        }else {
            return ResponseInf.setResponse(Const.start.REGISTER_FAILURE,null);
        }
    }

    /** 角色登录，使用方式：loginR roleId */
    @MyAnnontation(MethodName = "loginR")
    public ResponseInf loginRoleMesseage() {
        String msg = gameService.loginRole(intList.get(0));
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 手动保存数据至数据库，使用方式：save */
    @MyAnnontation(MethodName = "save")
    public ResponseInf saveDataBase() {
        return ResponseInf.setResponse(gameService.saveDataBase(),getRole());
    }

    /** 角色移动&场景切换，使用方式：move sceneId */
    @MyAnnontation(MethodName = "move")
    public ResponseInf moveMesseage() {
        if(roleService.moveTo(intList.get(0),getRole())){
            return ResponseInf.setResponse(Const.service.MOVE_SUCCESS,getRole());
        }
        return ResponseInf.setResponse(Const.service.MOVE_FAILURE,getRole());
    }

    /** 传送，使用方式：sendTo sceneId */
    @MyAnnontation(MethodName = "sendTo")
    public ResponseInf sendToScene() {
        roleService.sendToScene(intList.get(0),getRole());
        return ResponseInf.setResponse("传送成功",getRole());
    }

    /** 获取当前场景信息，使用方式：aoi */
    @MyAnnontation(MethodName = "aoi")
    public ResponseInf aoiMesseage() {
        String msg = roleService.printSceneDetail(getRole().getNowScenesId());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 查找任意场景信息，使用方式：：checkPlace sceneId */
    @MyAnnontation(MethodName = "checkPlace")
    public ResponseInf checkPlaceMesseage() {
        String msg = roleService.printSceneDetail(intList.get(0));
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 与NPC对话，使用方式：talkTo npcId */
    @MyAnnontation(MethodName = "talkTo")
    public ResponseInf talkToNpc(){
        String msg = roleService.getNpcReply(intList.get(0),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 修理装备，使用方式：repair equipmentId */
    @MyAnnontation(MethodName = "repair")
    public ResponseInf repair(){
        String msg = roleService.repairEquipment(intList.get(0),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 穿戴装备，使用方式：putOn equipmentId */
    @MyAnnontation(MethodName = "putOn")
    public ResponseInf putOn(){
        String msg = roleService.putOnEquipment(intList.get(0),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 获取当前武器耐久，使用方式：dura */
    @MyAnnontation(MethodName = "dura")
    public ResponseInf getWeaponDura(){
        return ResponseInf.setResponse(roleService.getWeaponDura(getRole()),getRole());
    }

    /** 卸下装备，使用方式：takeOff equipmentId */
    @MyAnnontation(MethodName = "takeOff")
    public ResponseInf takeOff(){
        String msg = roleService.takeOffEquipment(intList.get(0),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 使用药品，使用方式：use potionId */
    @MyAnnontation(MethodName = "use")
    public ResponseInf use(){
        if(roleService.useDrug(intList.get(0),getRole())){
            return ResponseInf.setResponse(Const.service.USE_SUCCESS,getRole());
        }
        return ResponseInf.setResponse(Const.service.USE_FAILURE,getRole());
    }

    /** 返回角色的信息，使用方式：getInfo */
    @MyAnnontation(MethodName = "getInfo")
    public ResponseInf getInfo(){
        return ResponseInf.setResponse(roleService.getRoleInfo(getRole()),getRole());
    }

    /** 返回怪物当前状态，使用方式：getMonster monsterId */
    @MyAnnontation(MethodName = "getMonster")
    public ResponseInf getMonster(){
        String msg = roleService.getMonsterInfo(intList.get(0),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** pk玩家，使用方式：pk skillId roleId */
    @MyAnnontation(MethodName = "pk")
    public ResponseInf pkPlayer(){
        String msg = roleService.pkPlayer(intList.get(0), intList.get(1),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 获得当前视野范围内实体信息，使用方式：view */
    @MyAnnontation(MethodName = "view")
    public ResponseInf getView(){
        return ResponseInf.setResponse(roleService.printViewDetail(getRole()),getRole());
    }

    /** 角色在场景内移动，使用方式：walk x y */
    @MyAnnontation(MethodName = "walk")
    public ResponseInf walkTo(){
        String msg = roleService.walkTo(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 调试代码用的测试，使用方式：test */
    @MyAnnontation(MethodName = "test")
    public ResponseInf testCode(){
        return ResponseInf.setResponse(roleService.testCode(getRole()),getRole());
    }

    /** 添加好友申请，使用方式：askFriend friendId */
    @MyAnnontation(MethodName = "askFriend")
    public ResponseInf askFriend(){
        roleService.askFriend(intList.get(0),getRole());
        return ResponseInf.setResponse("申请已提交",getRole());
    }

    /** 同意添加好友，使用方式：addFriend friendId */
    @MyAnnontation(MethodName = "addFriend")
    public ResponseInf addFriend(){
        roleService.addFriend(intList.get(0),getRole());
        return ResponseInf.setResponse("已添加好友",getRole());
    }

    /** 升级-测试，使用方式：levelUp level */
    @MyAnnontation(MethodName = "levelUp")
    public ResponseInf levelUp(){
        roleService.levelUp(intList.get(0),getRole());
        return ResponseInf.setResponse("已升级",getRole());
    }

    /** 获取自己的成就，使用方式：getAchievment */
    @MyAnnontation(MethodName = "getAchievment")
    public ResponseInf getAchievment(){
        return ResponseInf.setResponse(roleService.getAchievmentList(getRole()),getRole());
    }

    /** 背包整理&获取背包信息，使用方式：getOrderPackage */
    @MyAnnontation(MethodName = "getOrderPackage")
    public ResponseInf getOrderPackage(){
        return ResponseInf.setResponse(roleService.orderPackage(getRole()),getRole());
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

    /** 根据输入获得角色，输入参数最后一位为roleId */
    public Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
