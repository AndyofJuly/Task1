package com.game.system.role;

import com.game.netty.server.ServerHandler;
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
import com.game.system.role.pojo.Role;
import com.game.system.skill.SkillService;
import com.game.system.skill.pojo.SkillResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;


/**
 * 角色模块的业务逻辑处理
 * @Author andy
 * @create 2020/5/13 18:18
 */

public class RoleService {

    private final PackageService packageService = new PackageService();

    /**
     * 移动切换场景-临时场景考虑在内
     * @param lastSceneId 目标场景id
     * @param role 角色
     * @return 是否成功移动
     */
    public boolean moveTo(int lastSceneId, Role role){
        int scenesId = GlobalInfo.getScenes().get(role.getNowScenesId()).getSceneId();
        String nowPlace = SceneResource.getScenesStatics().get(scenesId).getName();
        int[] arr = SceneResource.getPlaces().get(AssistService.checkSceneId(nowPlace));
        //判断是否能移动到目标场景
        boolean result = false;
        for (int value : arr) {
            int sceneId = GlobalInfo.getScenes().get(lastSceneId).getSceneId();
            String innTarget = SceneResource.getScenesStatics().get(sceneId).getName();
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
     * 移动切换场景，或传送至某地
     * @param lastSceneId 目标场景id
     * @param role 角色
     */
    public void sendToScene(int lastSceneId, Role role){
        Grid before = GlobalInfo.getScenes().get(role.getNowScenesId()).getGridHashMap().get(role.getCurGridId());
        before.getGridRoleMap().remove(role.getId());
        Grid after = GlobalInfo.getScenes().get(lastSceneId).getGridHashMap().get(role.getCurGridId());
        after.getGridRoleMap().put(role.getId(),role);

        GlobalInfo.getScenes().get(role.getNowScenesId()).getRoleAll().remove(role);
        GlobalInfo.getScenes().get(lastSceneId).getRoleAll().add(role);
        role.setNowScenesId(lastSceneId);

        if(role.getBaby()!=null){
            role.getBaby().setScenneId(lastSceneId);
        }
    }

    /**
     * 获取并设置场景所有实体，包括场景名、角色、怪物和npc集合
     * @param sceneId 场景id
     * @return 目标场景中的所有实体对象
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
     * 维修装备-free
     * @param equipmentId 装备id
     * @param role 角色
     * @return 信息提示
     */
    public String repairEquipment(int equipmentId,Role role){
        if(!role.getEquipmentHashMap().containsKey(equipmentId)){
            return "没有此装备";
        }
        Equipment equipment = role.getEquipmentHashMap().get(equipmentId);
        equipment.setDura(AssistService.getEquipmentDura(equipmentId));
        return Const.service.REPAIR_SUCCESS +equipment.getDura();
    }

    /**
     * 穿戴装备-从背包里拿出来穿戴-身上已有装备与背包互换
     * @param equipmentId 装备id
     * @param role 角色
     * @return 信息提示
     */
    public String putOnEquipment(int equipmentId,Role role){
        int atk = role.getAtk();
        atk = atk + AssistService.getEquipmentAtk(equipmentId);
        role.setAtk(atk);
        //判断装备类型，根据身上是否穿戴，与背包进行交换
        EquipmentStatic equipmentInfo = EquipmentResource.getEquipmentStaticHashMap().get(equipmentId);
        boolean alreadyWear = false;
        int wearEquipId = 0;
        for(Integer selfEquipId : role.getEquipmentHashMap().keySet()){
            if(equipmentInfo.getType()==EquipmentResource.getEquipmentStaticHashMap().get(selfEquipId).getType()){
                alreadyWear = true;
                wearEquipId=selfEquipId;
            }
        }
        Equipment equipment1 = new Equipment(equipmentId);
        packageService.getFromPackage(equipmentId,1,role);
        role.getEquipmentHashMap().put(equipmentId,equipment1);

        Subject.notifyObservers(0,role,bodyEquipLvOb);

        if(alreadyWear){
            packageService.putIntoPackage(wearEquipId,1,role);
        }
        return Const.service.PUTON_SUCCESS;
    }

    /**
     * 获得武器的耐久
     * @param role 角色
     * @return 信息提示
     */
    public String getWeaponDura(Role role){
        for(Integer selfEquipId : role.getEquipmentHashMap().keySet()){
            if(EquipmentResource.getEquipmentStaticHashMap().get(selfEquipId).getType()==1){
                return "当前武器耐久："+role.getEquipmentHashMap().get(selfEquipId).getDura();
            }
        }
        return "没有佩戴武器！";
    }


    /**
     * 脱下装备
     * @param equipmentId 装备id
     * @param role 角色
     * @return 信息提示
     */
    public String takeOffEquipment(int equipmentId,Role role){
        int atk = role.getAtk();
        atk = atk - AssistService.getEquipmentAtk(equipmentId);
        role.setAtk(atk);
        role.getEquipmentHashMap().remove(equipmentId);
        packageService.putIntoPackage(equipmentId,1,role);
        return Const.service.TAKEOFF_SUCCESS;
    }

    /**
     * 使用药品
     * @param potionId 药品id
     * @param role 角色
     * @return 是否成功使用
     */
    public boolean useDrug(int potionId,Role role){
        int hp = role.getHp();
        int mp = role.getMp();
        if(!packageService.getFromPackage(potionId,1,role)){
            return false;
        }
        role.setHp(hp + AssistService.getPotionAddHp(potionId));
        role.setMp(mp + AssistService.getPotionAddMp(potionId));
        if(role.getHp()>=role.getMaxHp()){
            role.setHp(role.getMaxHp());
        }
        if(role.getMp()>=role.getMaxMp()){
            role.setMp(role.getMaxMp());
        }
        return true;
    }

    /**
     * 与玩家pk-任意同场景相同视野下可以发动，效果与技能和普攻相同
     * @param skillId 技能id
     * @param targetRoleId pk角色id
     * @param role 角色
     * @return 信息提示
     */
    public String pkPlayer (int skillId,int targetRoleId, Role role){
        Role enemy = GlobalInfo.getRoleHashMap().get(targetRoleId);
        if(enemy.getNowScenesId()!=role.getNowScenesId()){
            return "不再同一场景，无法pk";
        }
        if(AssistService.isNotInView(role,enemy)){
            return Const.Fight.DISTACNE_LACK;
        }
        String result = new SkillService().skillCommon(skillId,role);
        if(!Const.Fight.SUCCESS.equals(result)){
            return result;
        }
        return pkAffect(skillId,enemy,role);
    }

    /**
     * pk攻击时的双方的状态处理
     * @param skillId 技能id
     * @param enemy pk的对象
     * @param role 角色
     * @return 信息提示
     */
    private String pkAffect(int skillId,Role enemy,Role role){
        int hp = enemy.getHp();
        int skillHarm = SkillResource.getSkillStaticHashMap().get(skillId).getAtk();
        int weaponBuff = 0;
        if(role.getEquipmentHashMap()!=null){
            weaponBuff=Const.WEAPON_BUFF;
        }
        hp=hp-role.getAtk()-weaponBuff-skillHarm;

        if(hp<=0){
            role.setAtk(role.getAtk()+Const.ABTAIN_ATK);
            packageService.addMoney(Const.PK_GET_LOST,role);
            if(!packageService.lostMoney(Const.PK_GET_LOST,enemy)){
                enemy.setMoney(0);
            }

            Subject.notifyObservers(Const.achieve.TASK_PK_SUCCESS,role,fsPkSuccessOb);
            return Const.Fight.PK_SUCCESS;
        }

        ServerHandler.notifyRole(enemy.getId(),"你遭到pk，"+role.getName()+"向你发起了攻击",
                role.getId(),"你向对方发起了pk，攻击了对方");
        enemy.setHp(hp);
        return Const.Fight.TARGET_HP+hp;
    }

    /**
     * 场景内移动到目标位置。约定：一次最多跨越一个格子，左右移动距离差不超过16，上下距离差不超过8；且每次移动只能改变其中一个坐标的位置；
     * @param x 水平方向移动距离
     * @param y 垂直方向移动距离
     * @param role 角色
     * @return 信息提示
     */
    public String walkTo(int x,int y,Role role){
        int oldX = role.getPosition()[0];
        int oldY = role.getPosition()[1];
        boolean xMove = Math.abs(oldX-x)>=Const.GRID_LENGTH;
        boolean yMove = Math.abs(oldY-y)>=Const.GRID_WIDTH;
        boolean xyMove = (oldX-x!=0 && oldY-y!=0);
        if(xMove || yMove || xyMove){
            return "can not move";
        }
        refleshGrid(x,y,role);
        role.getPosition()[0]=x;
        role.getPosition()[1]=y;
        return "["+role.getPosition()[0]+","+role.getPosition()[1]+"]";
    }

    /**
     * 对移动前后的小网格进行数据更新，包括移动前后小网格中的实体进行增删和更新
     * @param x 水平方向移动距离
     * @param y 垂直方向移动距离
     * @param role 角色
     */
    private void refleshGrid(int x,int y,Role role){
        int oldGridId = getGridId(role.getPosition()[0],role.getPosition()[1]);
        int newGridId = getGridId(x,y);
        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        if(oldGridId==newGridId){return;}
        scene.getGridHashMap().get(oldGridId).getGridRoleMap().remove(role.getId());
        scene.getGridHashMap().get(newGridId).getGridRoleMap().put(role.getId(),role);
        role.setCurGridId(newGridId);
    }

    /**
     * 根据坐标位置计算小网格id
     * @param x 水平方向移动距离
     * @param y 垂直方向移动距离
     * @return 小网格id
     */
    public static int getGridId(int x,int y){
        return x/Const.GRID_LENGTH+1+y/Const.GRID_WIDTH*Const.GRID_WIDTH;
    }

    /**
     * 将角色周围的九个小网格封装成一个九宫格视野对象
     * @param role 角色
     * @return 九个小网格组成的视野对象
     */
    private ViewGridBo getGridVo(Role role){
        int curGridId = role.getCurGridId();
        HashMap<Integer,Grid> viewGridHashMap = new HashMap<>();
        //清理和更新
        role.getViewGridBo().getGridRoleMap().clear();
        role.getViewGridBo().getGridMonsterMap().clear();
        role.getViewGridBo().getGridNpcMap().clear();
        //角色周围的九个网格，对三行三列的每个格子进行统计
        for(int k=0;k<3;k++){
            for(int i=curGridId-Const.GRID_WIDTH-1;i<=curGridId-Const.GRID_WIDTH+1;i++){
                Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
                viewGridHashMap.put(i,scene.getGridHashMap().get(i));
                for(Integer key : viewGridHashMap.get(i).getGridRoleMap().keySet()){
                    role.getViewGridBo().getGridRoleMap().put(key, GlobalInfo.getRoleHashMap().get(key));
                }
                for(String key : viewGridHashMap.get(i).getGridMonsterMap().keySet()){
                    Monster monster = scene.getMonsterHashMap().get(key);
                    role.getViewGridBo().getGridMonsterMap().put(key,monster);
                }
                for(Integer key : viewGridHashMap.get(i).getGridNpcMap().keySet()){
                    Npc npc = scene.getNpcHashMap().get(key);
                    role.getViewGridBo().getGridNpcMap().put(key,npc);
                }
            }
            curGridId+=Const.GRID_WIDTH;
        }
        return role.getViewGridBo();
    }

    /**
     * 添加好友申请
     * @param friendId 申请目标id
     * @param role 角色
     */
    public void askFriend(int friendId,Role role){
        ServerHandler.notifyRole(friendId,"收到好友添加申请",role.getId(),"请求已发送");
        role.getFriendBo().getApplyIdList().add(friendId);
    }

    /**
     * 同意添加好友
     * @param friendId 申请人id
     * @param role 角色
     */
    public void addFriend(Integer friendId,Role role){
        role.getFriendBo().getFriendIdList().add(role.getId());
        role.getFriendBo().getApplyIdList().remove(friendId);
        Subject.notifyObservers(0,role,friendOb);
        Role friendRole = GlobalInfo.getRoleHashMap().get(friendId);
        Subject.notifyObservers(0,friendRole,friendOb);
        ServerHandler.notifyRole(friendId,role.getName()+"已同意你的好友申请",role.getId(),"已添加好友");
    }

    /**
     * 升级-测试
     * @param level 等级
     * @param role 角色
     */
    public void levelUp(int level,Role role){
        role.setLevel(level);
        Subject.notifyObservers(0,role,levelOb);
    }

    /**
     * 获取背包物品信息-简略
     * @param role 角色
     * @return 信息提示
     */
    public String getPackage(Role role){
        StringBuilder list= new StringBuilder();
        for(Integer goodsId : role.getMyPackageBo().getGoodsHashMap().keySet()){
            if(role.getMyPackageBo().getGoodsHashMap().get(goodsId)>0){
                list.append(goodsId).append(" ");
            }
        }
        return list.toString();
    }

    /**
     * 获得身上装备信息-简略
     * @param role 角色
     * @return 信息提示
     */
    public String getBodyEquip(Role role){
        StringBuilder list= new StringBuilder();
        for(Integer equipId : role.getEquipmentHashMap().keySet()){
            list.append(equipId).append(" ");
        }
        return list.toString();
    }

    /**
     * 打印场景详细信息，打印的怪物id为静态资源id，而非其UUID
     * @param sceneId 场景id
     * @return 信息提示
     */
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
                stringBuilder.append(monster.getMonsterName()).append("-").append(monster.getMonsterId()).append("-血量：").append(hp).append(" ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 获得当前视野信息，包括其他角色、怪物和NPC
     * @param myRole 角色
     * @return 信息提示
     */
    public String printViewDetail(Role myRole){
        ViewGridBo viewGridBo = getGridVo(myRole);
        StringBuilder stringBuilder = new StringBuilder("角色：");
        for(Integer key : viewGridBo.getGridRoleMap().keySet()){
            Role role = GlobalInfo.getRoleHashMap().get(key);
            stringBuilder.append(role.getName()).
                    append(" 位置：").append(role.getPosition()[0]).append(",").append(role.getPosition()[1]).
                    append(" 网格id：").append(role.getCurGridId()).append("；");
        }
        stringBuilder.append("\n怪物：");
        for(String key : viewGridBo.getGridMonsterMap().keySet()){
            Monster monster = viewGridBo.getGridMonsterMap().get(key);
            stringBuilder.append(monster.getMonsterName()).append("-").
                    append(monster.getMonsterId()).append("-位置：").append(monster.getPosition()[0]).
                    append(",").append(monster.getPosition()[1]).append("-血量：").append(monster.getMonsterHp()).append("； ");
        }
        stringBuilder.append("\nNPC：");
        for(Integer key : viewGridBo.getGridNpcMap().keySet()){
            Npc npc = viewGridBo.getGridNpcMap().get(key);
            stringBuilder.append(npc.getName()).append("-").
                    append(npc.getNpcId()).append("-位置：").append(npc.getPosition()[0]).append(",").append(npc.getPosition()[1]).append("； ");
        }
        return stringBuilder.toString();
    }

    /**
     * 与Npc对话
     * @param npcId npcId
     * @param role  角色
     * @return String
     */
    public String getNpcReply(int npcId,Role role){

        Subject.notifyObservers(npcId,role,talkNpcOb);

        //talkNpcOb.fireTalkNpc(role,npcId);
        //ITalkNpcObserver.fireTalkNpc(Role role,int npcId)

        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        Npc npc = scene.getNpcHashMap().get(npcId);
        if(AssistService.isNotInView(role, npc) || AssistService.isNotInScene(npcId,role)){
            return Const.NPC_NOTICE;
        }
        return npc.getWords();
    }

    /**
     * 获得角色当前自身信息
     * @param role 角色
     * @return 信息提示
     */
    public String getRoleInfo(Role role){
        return  "id:"+role.getId()+"\n"+"name:"+role.getName()+"\n"+"hp："+role.getHp()+"\n"+
                "mp："+role.getMp()+"\n"+"atk："+role.getAtk()+"\n"+"money："+role.getMoney()+"\n"+
                "nowScenesId:"+role.getNowScenesId()+"\n"+"careerId:"+role.getCareerId()+"\n"+
                "unionId:"+role.getUnionId()+"\n"+"level:"+role.getLevel()+"\n"+
                "背包物品："+getPackage(role)+"\n"+"身上装备："+getBodyEquip(role)+"\n";
    }

    /**
     * 获得某个怪物信息
     * @param monsterId 怪物id
     * @param role 角色
     * @return 信息提示
     */
    public String getMonsterInfo(int monsterId,Role role){
        int nowScenesId = role.getNowScenesId();
        String monsterUUID = AssistService.checkMonsterId(monsterId,role);
        Monster nowMonster = GlobalInfo.getScenes().get(nowScenesId).getMonsterHashMap().get(monsterUUID);
        return "hp：" + nowMonster.getMonsterHp() + "，状态："+ nowMonster.getAlive();
    }

    /**
     * 测试用命令
     * @param role 角色
     * @return 信息提示
     */
    public String testCode(Role role){
        return role.getMyPackageBo().getPackageGrid();
    }

    /**
     * 整理背包
     * @param role 角色
     * @return 信息提示
     */
    public String orderPackage(Role role){
        role.getMyPackageBo().orderPackageGrid();
        return getPackageInfo(role);
    }

    /**
     * 获得背包中每个格子的信息
     * @param role 角色
     * @return 信息提示
     */
    public String getPackageInfo(Role role){
        return role.getMyPackageBo().getPackageGrid();
    }

    /**
     * 获取当前所有成就的信息
     * @param role 角色
     * @return 信息提示
     */
    public String getAchievmentList(Role role){
        StringBuilder result= new StringBuilder();
        for(Integer achieveId : AchieveResource.getAchieveStaticHashMap().keySet()){
            result.append(AchieveResource.getAchieveStaticHashMap().get(achieveId).getDesc()).append("：").
                    append(role.getAchievementBo().getAchievementHashMap().get(achieveId)).append("\n");
        }
        return result.toString();
    }

    //角色方法处理模块，预先注册好相应的监听器，当触发事件时，对已注册的监听器进行通知处理
    //一对一处理
/*    static {
        //TalkNpcSB.registerObserver(new TalkNpcOb());
        BodyEquipLvSB.registerObserver(new BodyEquipLvOb());
        FriendSB.registerObserver(new FriendOb());
        FsPkSuccessSB.registerObserver(new FsPkSuccessOb());
        LevelSB.registerObserver(new LevelOb());
    }*/

    //注册观察者
    private TalkNpcOb talkNpcOb = new TalkNpcOb();
    private BodyEquipLvOb bodyEquipLvOb = new BodyEquipLvOb();
    private FriendOb friendOb = new FriendOb();
    private FsPkSuccessOb fsPkSuccessOb = new FsPkSuccessOb();
    private LevelOb levelOb = new LevelOb();


}


