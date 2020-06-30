package com.game.controller;

import com.game.common.Const;
import com.game.common.MyAnnontation;
import com.game.dao.RoleMapper;
import com.game.entity.Equipment;
import com.game.entity.Monster;
import com.game.entity.Role;
import com.game.entity.store.MonsterResource;
import com.game.entity.store.NpcResource;
import com.game.entity.vo.GridVo;
import com.game.entity.vo.SceneDetailVo;
import com.game.service.AchievementService;
import com.game.service.RoleService;
import com.game.service.UserService;
import com.game.service.assis.AssistService;
import com.game.service.assis.GlobalResource;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 业务逻辑处理，根据不同的输入命令通过反射原理调用不同的方法，使用了spring中的自定义注解来实现
 * @Author andy
 * @create 2020/5/17 1:09
 */
@Controller
public class RoleController {
    //private static String[] strings = new String[]{};

    private static ArrayList<Integer> intList  = new ArrayList<>();
    private static ArrayList<String> strList  = new ArrayList<>();
    UserService userService = new UserService();
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

    //角色注册，使用举例（新增职业选择，插入数据库中，并进入游戏时拿到缓存里）：registerR roleName careerName
    @MyAnnontation(MethodName = "registerR")
    public String registerRoleMesseage() { //注册时没有id，id从数据库中拿
        if(roleMapper.insertRegisterRole(strList.get(1),AssistService.checkCareerId(strList.get(2)))){
            return Const.start.REGISTER_SUCCESS;
        }else {
            return Const.start.REGISTER_FAILURE;
        }
    }

    //角色登录，使用举例：loginR roleName
    @MyAnnontation(MethodName = "loginR")
    public String loginRoleMesseage() {
        return userService.loginRole(strList.get(1), roleMapper.selectRoleIdByName(strList.get(1)));
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
        int sceneId = getRole().getNowScenesId();
        //String sceneName = GlobalResource.getScenes().get(sceneId).getName();
        SceneDetailVo sceneDetailVo = roleService.placeDetail(sceneId);
        return printSceneDetail(sceneDetailVo);
    }

    //查找任意场景信息，使用举例：checkPlace sceneName ：here修改成sceneId
    @MyAnnontation(MethodName = "checkPlace")
    public String checkPlaceMesseage() {
        SceneDetailVo sceneDetailVo = roleService.placeDetail(intList.get(0));
        return printSceneDetail(sceneDetailVo);
    }

    //here
    //与NPC对话，使用举例：talkTo npcName ：here修改成npcId
    @MyAnnontation(MethodName = "talkTo")
    public String talkToNpc(){
        if(AssistService.checkNpcId(intList.get(0),getRole())){
            //task
            AchievementService.ifTalkToOldMan(intList.get(0),getRole());
            return NpcResource.getNpcsStatics().get(intList.get(0)).getWords();
        }
        return "";
    }

    //修理装备，使用举例：repair equipmentName：here修改成equipmentId
    @MyAnnontation(MethodName = "repair")
    public String repair(){
        Equipment equipment = roleService.repairEquipment(intList.get(0),getRole());
        return Const.service.REPAIR_SUCCESS +equipment.getDura();
    }

    //here
    //穿戴装备，使用举例：putOn equipmentName：here修改成equipmentId
    @MyAnnontation(MethodName = "putOn")
    public String putOn(){
        roleService.putOnEquipment(intList.get(0),getRole());
        return Const.service.PUTON_SUCCESS;
    }

    //卸下装备，使用举例：takeOff equipmentName：here修改成equipmentId
    @MyAnnontation(MethodName = "takeOff")
    public String takeOff(){
        roleService.takeOffEquipment(intList.get(0),getRole());
        return Const.service.TAKEOFF_SUCCESS;
    }

    //使用药品，使用举例：use potionName：here修改成potionId
    @MyAnnontation(MethodName = "use")
    public String use(){
        if(roleService.useDrug(intList.get(0),getRole())){
            return Const.service.USE_SUCCESS;
        }
        return Const.service.USE_FAILURE;
    }

    //额外的一些小功能，自己运行程序时时方便观察变量情况
    //返回角色的hp，mp，武器耐久，当前攻击力，使用举例：getInfo
    @MyAnnontation(MethodName = "getInfo")
    public String getInfo(){
        Role role = getRole();
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

    //返回怪物当前状态，使用举例：getMonster monsterName：here修改成monsterId
    @MyAnnontation(MethodName = "getMonster")
    public String getMonster(){
        String key = AssistService.checkMonsterId(intList.get(0),getRole());
        int nowScenesId = getRole().getNowScenesId();
        Monster nowMonster = GlobalResource.getScenes().get(nowScenesId).getMonsterHashMap().get(key);
        return "hp：" + nowMonster.getMonsterHp() + "，状态："+ nowMonster.getAlive();
    }

    //任意场景可以pk玩家，使用举例：pk skillName ss(对方roleId)：here修改成skillId和对方roleId
    @MyAnnontation(MethodName = "pk")
    public String pkPlayer(){
        return roleService.pkPlayer(intList.get(0), intList.get(1),getRole());
    }

    //获得当前视野范围内的其他角色，使用举例：view
    @MyAnnontation(MethodName = "view")
    public String getView(){
        GridVo gridVo = roleService.getGridVo(getRole());
        return printViewDetail(gridVo);
    }

    //角色在场景内移动，使用举例：walk x y
    @MyAnnontation(MethodName = "walk")
    public String walkTo(){
        int[] position = roleService.walkTo(intList.get(0),intList.get(1),getRole());
        return "["+position[0]+","+position[1]+"]";
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

    //打印场景信息
    private String printSceneDetail(SceneDetailVo sceneDetailVo){
        StringBuilder stringBuilder = new StringBuilder("地点："+sceneDetailVo.getSceneName()+"。 角色：");
        for(int i=0;i<sceneDetailVo.getRoleArrayList().size();i++) {
            stringBuilder.append(sceneDetailVo.getRoleArrayList().get(i).getName()).append(" ");
        }

        stringBuilder.append("。 NPC：");
        for(int i=0;i<sceneDetailVo.getNpcIdArray().length;i++) {
            Integer npcId = Integer.valueOf(sceneDetailVo.getNpcIdArray()[i]);
            stringBuilder.append(NpcResource.getNpcsStatics().get(npcId).getName()).append(" ");
        }

        stringBuilder.append("。 怪物：");
        for(String key: sceneDetailVo.getMonsterHashMap().keySet()){
            if(sceneDetailVo.getMonsterHashMap().get(key).getAlive()==1){
                Integer monsterId = sceneDetailVo.getMonsterHashMap().get(key).getMonsterId();
                stringBuilder.append(MonsterResource.getMonstersStatics().get(monsterId).getName()).append(" ");
            }
        }
        return stringBuilder.toString();
    }

    //打印视野信息-角色集合
    private String printViewDetail(GridVo gridVo){
        StringBuilder stringBuilder = new StringBuilder("角色：");
        for(Integer key : gridVo.getGridRoleMap().keySet()){
            Role role =GlobalResource.getRoleHashMap().get(key);
            stringBuilder.append(role.getName()+" 位置："+
                    role.getPosition()[0]+","+
                    role.getPosition()[1]+" 网格id："+
                    role.getCurGridId()+"；");
        }
        stringBuilder.append("。 怪物：");
        for(String key : gridVo.getGridMonsterMap().keySet()){
            Monster monster = gridVo.getGridMonsterMap().get(key);
            stringBuilder.append(MonsterResource.getMonstersStatics().get(monster.getMonsterId()).getName()+" 位置："
                    +monster.getPosition()[0]+","
                    +monster.getPosition()[1]+"；");
        }
        return stringBuilder.toString();
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
