package com.game.system.role;

import com.game.system.achievement.observer.*;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.achievement.subject.*;
import com.game.system.bag.PackageService;
import com.game.common.Const;
import com.game.system.bag.pojo.Equipment;
import com.game.system.bag.pojo.EquipmentResource;
import com.game.system.scene.pojo.*;
import com.game.system.bag.pojo.EquipmentStatic;
import com.game.system.assist.AssistService;
import com.game.system.assist.GlobalInfo;
import com.game.system.role.pojo.CareerResource;
import com.game.system.role.pojo.Role;
import com.game.system.skill.SkillService;
import com.game.system.skill.pojo.SkillResource;

import java.util.HashMap;


/**
 * 角色一些功能的方法实现
 * @Author andy
 * @create 2020/5/13 18:18
 */

public class RoleService {

    //private IAchieveService iAchieveService = new AchieveServiceImpl();
    private PackageService packageService = new PackageService();

    /**
     * 移动切换场景
     * @param lastSceneId 目标场景id
     * @param role 角色
     * @return boolean
     */
    //角色移动&场景切换，此处moveTarget为外部名字 tempId为动态随机id，角色所在当前场景id为临时场景id
    public boolean moveTo(int lastSceneId, Role role){
        int scenesId = GlobalInfo.getScenes().get(role.getNowScenesId()).getSceneId();//获得内部id
        String nowPlace = SceneResource.getScenesStatics().get(scenesId).getName();

        //将当前场景坐标与要移动的场景的坐标进行对比，此处arr为位置关系数组
        int[] arr = SceneResource.getPlaces().get(AssistService.checkSceneId(nowPlace));
        boolean result = false;
        for (int value : arr) {
            int sceneId = GlobalInfo.getScenes().get(lastSceneId).getSceneId();
            String innTarget = SceneResource.getScenesStatics().get(sceneId).getName();//根据动态id找到静态名
            if (innTarget.equals(SceneResource.getScenesStatics().get(value).getName())){
                result = true;
                break;
            }
        }

        if(result){
            sendToScene(lastSceneId, role);
        }
        return result;
    }

    /**
     * 移动切换场景
     * @param lastSceneId 目标场景id
     * @param role 角色
     */
    //传送至某地
    public void sendToScene(int lastSceneId, Role role){
        Grid before = GlobalInfo.getScenes().get(role.getNowScenesId()).getGridHashMap().get(role.getCurGridId());
        before.getGridRoleMap().remove(role.getId());
        Grid after = GlobalInfo.getScenes().get(lastSceneId).getGridHashMap().get(role.getCurGridId());
        after.getGridRoleMap().put(role.getId(),role);

        //如果可以移动成功，当前场景剔除该角色，目标场景加入该角色
        GlobalInfo.getScenes().get(role.getNowScenesId()).getRoleAll().remove(role);
        GlobalInfo.getScenes().get(lastSceneId).getRoleAll().add(role);

        role.setNowScenesId(lastSceneId);

        if(role.getBaby()!=null){
            role.getBaby().setScenneId(lastSceneId);
        }
    }

    /**
     * 获取并设置场景信息，包括场景名、角色、怪物和npc集合
     * @param sceneId 场景id
     * @return SceneDetailBo
     */
    private SceneDetailBo placeDetail(int sceneId){
        SceneDetailBo sceneDetailBo = new SceneDetailBo();
        Scene o = GlobalInfo.getScenes().get(sceneId);
        sceneDetailBo.setSceneName(o.getName());
        sceneDetailBo.setRoleArrayList(o.getRoleAll());
        sceneDetailBo.setMonsterHashMap(GlobalInfo.getScenes().get(sceneId).getMonsterHashMap());
        sceneDetailBo.setNpcHashMap(GlobalInfo.getScenes().get(sceneId).getNpcHashMap());
        return sceneDetailBo;
    }

    /**
     * 维修装备
     * @param equipmentId 装备id
     * @param role 角色
     * @return String
     */
    //修理装备
    public String repairEquipment(int equipmentId,Role role){
        Equipment equipment = role.getEquipmentHashMap().get(equipmentId);
        equipment.setDura(AssistService.getEquipmentDura(equipmentId));
        return Const.service.REPAIR_SUCCESS +equipment.getDura();
    }

    /**
     * 穿戴装备
     * @param equipmentId 装备id
     * @param role 角色
     * @return String
     */
    //穿戴装备-从背包里拿出来穿戴
    public String putOnEquipment(int equipmentId,Role role){
        int atk = role.getAtk();
        atk = atk + AssistService.getEquipmentAtk(equipmentId);
        role.setAtk(atk);

        EquipmentStatic equipmentInfo = EquipmentResource.getEquipmentStaticHashMap().get(equipmentId);
        boolean alreadyWear = false;
        int wearEquipId = 0;
        for(Integer selfEquipId : role.getEquipmentHashMap().keySet()){
            if(equipmentInfo.getType()==EquipmentResource.getEquipmentStaticHashMap().get(selfEquipId).getType()){
                alreadyWear = true;
                wearEquipId=selfEquipId;
            }
        }
        Equipment equipment1 = new Equipment(equipmentId,equipmentInfo.getDurability());
        role.getEquipmentHashMap().put(equipmentId,equipment1);
        packageService.getFromPackage(equipmentId,1,role);
        BodyEquipLvSB.notifyObservers(0,role);

        //类型相同，背包与身上装备互换,类型不同-直接穿无需返回给背包
        if(alreadyWear){
            packageService.putIntoPackage(wearEquipId,1,role);
        }
        return Const.service.PUTON_SUCCESS;
    }

    /**
     * 脱下装备
     * @param equipmentId 装备id
     * @param role 角色
     * @return String
     */
    //卸下装备
    public String takeOffEquipment(int equipmentId,Role role){
        int atk = role.getAtk();
        atk = atk - AssistService.getEquipmentAtk(equipmentId);
        role.setAtk(atk);
        role.getEquipmentHashMap().remove(equipmentId);
        packageService.putIntoPackage(equipmentId,1,role);
        //role.getMyPackageBo().getGoodsHashMap().put(equipmentId,1);
        return Const.service.TAKEOFF_SUCCESS;
    }

    /**
     * 使用药品
     * @param potionId 药品id
     * @param role 角色
     * @return boolean
     */
    //使用药品
    public boolean useDrug(int potionId,Role role){
        int hp = role.getHp();
        int mp = role.getMp();
        if(role.getMyPackageBo().getGoodsHashMap().get(potionId)<=0){
            return false;
        }else {
            packageService.getFromPackage(potionId,1,role);
        }
        role.setHp(hp + AssistService.getPotionAddHp(potionId));
        role.setMp(mp + AssistService.getPotionAddMp(potionId));
        int maxHp = CareerResource.getCareerStaticHashMap().get(Const.CAREER_ID).getHp();
        int maxMp = CareerResource.getCareerStaticHashMap().get(Const.CAREER_ID).getMp();
        if(role.getHp()>=maxHp || role.getMp()>=maxMp){
            role.setHp(maxHp);
            role.setMp(maxMp);
        }
        return true;
    }

    /**
     * 与玩家pk
     * @param skillId 技能id
     * @param targetRoleId pk角色id
     * @param role 角色
     * @return String
     */
    //选择：任意同场景可以pk玩家-假设可随意使用技能攻击
    public String pkPlayer (int skillId,int targetRoleId, Role role){
        Role enemy = GlobalInfo.getRoleHashMap().get(targetRoleId);
        //检查距离是否足够
        if(!AssistService.checkDistance(role,enemy)){
            return Const.Fight.DISTACNE_LACK;
        }
        String result = new SkillService().skillCommon(skillId,role);
        if(!Const.Fight.SUCCESS.equals(result)){
            return result;
        }
        return pkAffect(skillId,enemy,role);
    }

    //攻击时的双方的状态变化
    private String pkAffect(int skillId,Role enemy,Role role){
        int hp = enemy.getHp();
        int skillHarm = SkillResource.getSkillStaticHashMap().get(skillId).getAtk();
        int weaponBuff = 0;
        if(role.getEquipmentHashMap()!=null){//有装备，加攻击buff
            weaponBuff=Const.WEAPON_BUFF;
        }
        hp=hp-role.getAtk()-weaponBuff-skillHarm;

        if(hp<=0){
            role.setAtk(role.getAtk()+Const.ABTAIN_ATK);
            packageService.addMoney(Const.PK_GET_LOST,role);
            packageService.lostMoney(Const.PK_GET_LOST,enemy);
            FsPkSuccessSB.notifyObservers(Const.achieve.TASK_PK_SUCCESS,role);
            return Const.Fight.PK_SUCCESS;
        }

        enemy.setHp(hp);
        return Const.Fight.TARGET_HP+hp;
    }

    /**
     * 场景内走路移动
     * @param x 水平方向移动距离
     * @param y 垂直方向移动距离
     * @param role 角色
     * @return String
     */
    //输入坐标，走动到目标位置；
    public String walkTo(int x,int y,Role role){
        int oldX = role.getPosition()[0];
        int oldY = role.getPosition()[1];
        //一次只能移动一个格子，左右距离差不超过16，上下距离差不超过8；只能改变其中一个坐标的位置；
        if(Math.abs(oldX-x)>=Const.GRID_LENGTH
                || Math.abs(oldY-y)>=Const.GRID_WIDTH
                || (oldX-x!=0 && oldY-y!=0)){
            return "can not move";
        }
        refleshGrid(x,y,role);
        role.getPosition()[0]=x;
        role.getPosition()[1]=y;
        return "["+role.getPosition()[0]+","+role.getPosition()[1]+"]";
    }

    //对移动前后的网格进行数据更新，包括移动前后网格中实体的增删，先考虑角色，怪物和NPC放在场景模块
    private void refleshGrid(int x,int y,Role role){
        int oldGridId = getGridId(role.getPosition()[0],role.getPosition()[1]);
        int newGridId = getGridId(x,y);
        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        if(oldGridId==newGridId){return;}
        scene.getGridHashMap().get(oldGridId).getGridRoleMap().remove(role.getId());
        scene.getGridHashMap().get(newGridId).getGridRoleMap().put(role.getId(),role);
        role.setCurGridId(newGridId);
    }

    public static int getGridId(int x,int y){
        return x/Const.GRID_LENGTH+1+y/Const.GRID_WIDTH*Const.GRID_WIDTH;
    }

    //封装成九宫格
    private GridBo getGridVo(Role role){
        int curGridId = role.getCurGridId();
        HashMap<Integer,Grid> viewGridHashMap = new HashMap<>();
        role.getGridBo().getGridRoleMap().clear();//清理上一次留存下来的
        role.getGridBo().getGridMonsterMap().clear();
        role.getGridBo().getGridNpcMap().clear();
        //三行三列，每个格子进行封装
        for(int k=0;k<3;k++){
            for(int i=curGridId-Const.GRID_WIDTH-1;i<=curGridId-Const.GRID_WIDTH+1;i++){
                Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
                viewGridHashMap.put(i,scene.getGridHashMap().get(i));
                for(Integer key : viewGridHashMap.get(i).getGridRoleMap().keySet()){
                    role.getGridBo().getGridRoleMap().put(key, GlobalInfo.getRoleHashMap().get(key));
                }
                for(String key : viewGridHashMap.get(i).getGridMonsterMap().keySet()){
                    System.out.println("怪物个数:"+viewGridHashMap.get(i).getGridMonsterMap().size());
                    Monster monster = scene.getMonsterHashMap().get(key);
                    role.getGridBo().getGridMonsterMap().put(key,monster);
                }
                for(Integer key : viewGridHashMap.get(i).getGridNpcMap().keySet()){
                    System.out.println("NPC个数:"+viewGridHashMap.get(i).getGridNpcMap().size());
                    Npc npc = scene.getNpcHashMap().get(key);
                    role.getGridBo().getGridNpcMap().put(key,npc);
                }
            }
            curGridId+=Const.GRID_WIDTH;
        }
        return role.getGridBo();
    }

    /**
     * 申请添加好友
     * @param friendId 好友id
     * @param role 角色
     */
    //添加好友申请
    public void askFriend(int friendId,Role role){
        role.getFriendBo().getApplyIdList().add(friendId);
    }

    //可以增加获取好友申请列表，查看列表后可以执行以下的同意添加方法

    /**
     * 接受好友申请
     * @param friendId 好友id
     * @param role 角色
     */
    //同意添加好友
    public void addFriend(Integer friendId,Role role){
        role.getFriendBo().getFriendIdList().add(role.getId());
        role.getFriendBo().getApplyIdList().remove(friendId);
        //iAchieveService.countAddFriend(FriendId,role);
        FriendSB.notifyObservers(0,role);
        Role friendRole = GlobalInfo.getRoleHashMap().get(friendId);
        FriendSB.notifyObservers(0,friendRole);
    }

    /**
     * 升级-测试用
     * @param level 等级
     * @param role 角色
     */
    //升级
    public void levelUp(int level,Role role){
        role.setLevel(level);
        //iAchieveService.countLevel(role);
        LevelSB.notifyObservers(0,role);
    }

/*    *//**
     * 获取当前成就信息
     * @param role 角色
     * @return String
     *//*
    public String getAchievment(Role role){
        return getAchievmentList(role);
    }*/

    /**
     * 获取背包物品
     * @param role 角色
     * @return String
     */
    public String getPackage(Role role){
        String list="";
        for(Integer goodsId : role.getMyPackageBo().getGoodsHashMap().keySet()){
            if(role.getMyPackageBo().getGoodsHashMap().get(goodsId)>0){
                list+=goodsId+" ";
            }
        }
        return list;
    }

    /**
     * 获得身上装备信息
     * @param role 角色
     * @return String
     */
    public String getBodyEquip(Role role){
        String list="";
        for(Integer equipId : role.getEquipmentHashMap().keySet()){
            list+=equipId+" ";
        }
        return list;
    }

    /**
     * 打印场景详细信息
     * @param sceneId 场景id
     * @return String
     */
    //打印场景信息
    public String printSceneDetail(int sceneId){
        SceneDetailBo sceneDetailBo = placeDetail(sceneId);
        StringBuilder stringBuilder = new StringBuilder("地点："+ sceneDetailBo.getSceneName()+"\n"+"角色：");
        for(int i = 0; i< sceneDetailBo.getRoleArrayList().size(); i++) {
            stringBuilder.append(sceneDetailBo.getRoleArrayList().get(i).getName()).append(" ");
        }
        if(sceneDetailBo.getNpcHashMap()!=null){
            stringBuilder.append("\nNPC：");
            for(Integer key : sceneDetailBo.getNpcHashMap().keySet()) {
                stringBuilder.append(sceneDetailBo.getNpcHashMap().get(key).getName()).append(" ");
            }
        }
        stringBuilder.append("\n怪物：");
        for(String key: sceneDetailBo.getMonsterHashMap().keySet()){
            if(sceneDetailBo.getMonsterHashMap().get(key).getAlive()==1){
                Monster monster = sceneDetailBo.getMonsterHashMap().get(key);
                int hp = sceneDetailBo.getMonsterHashMap().get(key).getMonsterHp();
                stringBuilder.append(monster.getMonsterName()+"-"+monster.getMonsterId()+"-血量："+hp).append(" ");//为了观察方便，打印出来的是静态资源id
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 获得当前视野信息
     * @param myRole 角色
     * @return String
     */
    //打印视野信息-角色集合
    public String printViewDetail(Role myRole){
        GridBo gridBo = getGridVo(myRole);
        StringBuilder stringBuilder = new StringBuilder("角色：");
        for(Integer key : gridBo.getGridRoleMap().keySet()){
            Role role = GlobalInfo.getRoleHashMap().get(key);
            stringBuilder.append(role.getName()+" 位置："+
                    role.getPosition()[0]+","+
                    role.getPosition()[1]+" 网格id："+
                    role.getCurGridId()+"；");
        }
        stringBuilder.append("\n怪物：");
        for(String key : gridBo.getGridMonsterMap().keySet()){
            Monster monster = gridBo.getGridMonsterMap().get(key);
            stringBuilder.append(monster.getMonsterName()+"-"+monster.getMonsterId()+"-位置："
                    +monster.getPosition()[0]+","
                    +monster.getPosition()[1]+"-血量："
                    +monster.getMonsterHp()+"； ");
        }
        stringBuilder.append("\nNPC：");
        for(Integer key : gridBo.getGridNpcMap().keySet()){
            Npc npc = gridBo.getGridNpcMap().get(key);
            stringBuilder.append(npc.getName()+"-"+npc.getNpcId()+"-位置："
                    +npc.getPosition()[0]+","
                    +npc.getPosition()[1]+"； ");
        }
        return stringBuilder.toString();
    }

/*    //private List<AchievObserver> observers = new ArrayList<AchievObserver>();//此observers注册了该role方法中涉及的不同种类的成就观察者。-不需要
    private TalkNpcOB talkNpcOB;
    public void registerObserver(TalkNpcOB observer){
        talkNpcOB=observer;
    }

    public void notifyObservers(int targetId, Role role) {
        //new TalkNpcOB(this);-提前先注册好
        //for (TalkNpcOB observer : observers) {
        talkNpcOB.checkAchievement(targetId,role);
        //}
    }*/


    /**
     * 与Npc对话
     * @param npcId npcId
     * @param role  角色
     * @return String
     */
    public String getNpcReply(int npcId,Role role){
        //new TalkNpcSB().notifyObservers(npcId,role);
        TalkNpcSB.notifyObservers(npcId,role);
        //iAchieveService.talkToNpc(npcId,role);
        //此处可新增监听判断
        //如果不在视野内，无法对话
        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        Npc npc = scene.getNpcHashMap().get(npcId);
        if(!AssistService.checkDistance(role, npc)){
            return "请站近一点对话";
        }
        return npc.getWords();
    }


    /**
     * 获得角色自己当前信息
     * @param role 角色
     * @return String
     */
    public String getRoleInfo(Role role){
        return  "id:"+role.getId()+"\n"+"name:"+role.getName()+"\n"+"hp："+role.getHp()+"\n"+
                "mp："+role.getMp()+"\n"+"atk："+role.getAtk()+"\n"+"money："+role.getMoney()+"\n"+
                "nowScenesId:"+role.getNowScenesId()+"\n"+"careerId:"+role.getCareerId()+"\n"+
                "unionId:"+role.getUnionId()+"\n"+"level:"+role.getLevel()+"\n"+
                "背包物品："+getPackage(role)+"\n"+"身上装备："+getBodyEquip(role)+"\n";
    }

    /**
     * 获得怪物信息
     * @param monsterId 怪物id
     * @param role 角色
     * @return String
     */
    public String getMonsterInfo(int monsterId,Role role){
        int nowScenesId = role.getNowScenesId();
        //找到UUID
        String monsterUUID = AssistService.checkMonsterId(monsterId,role);
        Monster nowMonster = GlobalInfo.getScenes().get(nowScenesId).getMonsterHashMap().get(monsterUUID);
        return "hp：" + nowMonster.getMonsterHp() + "，状态："+ nowMonster.getAlive();
    }

    /**
     * 测试
     * @param role 角色
     * @return String
     */
    //测试用命令
    public String testCode(Role role){
        //role.getMyPackageBo().randPackageGrid();
        return role.getMyPackageBo().getPackageGrid();
    }

    /**
     * 整理背包
     * @param role 角色
     * @return String
     */
    public String orderPackage(Role role){
        role.getMyPackageBo().orderPackageGrid();
        return getPackageInfo(role);
    }

    /**
     * 背包物品随机存放
     * @param role 角色
     * @return String
     */
/*    public String randPackage(Role role){
        role.getMyPackageBo().randPackageGrid();
        return getPackageInfo(role);
    }*/

    /**
     * 获得背包格子信息
     * @param role 角色
     * @return String
     */
    public String getPackageInfo(Role role){
        return role.getMyPackageBo().getPackageGrid();
    }

    /**
     * 获取当前成就信息
     * @param role 角色
     * @return String
     */
    //获得角色当前所有成就信息
    public String getAchievmentList(Role role){
        String result="";
        for(Integer achieveId : AchieveResource.getAchieveStaticHashMap().keySet()){
            result+=AchieveResource.getAchieveStaticHashMap().get(achieveId).getDesc()+"："
                    +role.getAchievementBo().getAchievementHashMap().get(achieveId)+"\n";
        }
        return result;
    }

    //角色方法处理模块，预先注册好相应的监听器，当触发时间时，对已注册的监听器进行通知处理
    static {
        TalkNpcSB.registerObserver(new TalkNpcOB());
        BodyEquipLvSB.registerObserver(new BodyEquipLvOB());
        FriendSB.registerObserver(new FriendOB());
        FsPkSuccessSB.registerObserver(new FsPkSuccessOB());
        LevelSB.registerObserver(new LevelOB());
    }
}


