package com.game.service.impl;

import com.game.common.Const;
import com.game.entity.*;
import com.game.entity.bo.SceneDetailBo;
import com.game.entity.excel.EquipmentStatic;
import com.game.entity.store.*;
import com.game.entity.bo.GridBo;
import com.game.service.IAchievementService;
import com.game.service.IPackageService;
import com.game.service.IRoleService;
import com.game.service.assist.AssistService;
import com.game.service.assist.GlobalInfo;
import com.game.service.assist.ResourceSearch;

import java.util.HashMap;


/**
 * 角色一些功能的方法实现
 * @Author andy
 * @create 2020/5/13 18:18
 */

public class RoleServiceImpl implements IRoleService {

    private IAchievementService iAchievementService = new AchievementServiceImpl();
    private IPackageService iPackageService = new PackageServiceImpl();

    //角色移动&场景切换，此处moveTarget为外部名字 tempId为动态随机id，角色所在当前场景id为临时场景id
    @Override
    public boolean moveTo(int lastSceneId,Role role){
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
        //如果可以移动成功，当前场景剔除该角色，目标场景加入该角色
        if(result){
            //Integer lastSceneId = AssistService.checkDynSceneId(moveTarget);//根据静态名，得到外部id
            //视野扩展更新代码两条，先原地址移除，再新地址加入，加入后暂时保持网格id不变
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
        return result;
    }

    //获得场景信息
    @Override
    public SceneDetailBo placeDetail(int sceneId){
        SceneDetailBo sceneDetailBo = new SceneDetailBo();
        Scene o = GlobalInfo.getScenes().get(sceneId);
        sceneDetailBo.setSceneName(o.getName());
        sceneDetailBo.setRoleArrayList(o.getRoleAll());
        sceneDetailBo.setMonsterHashMap(GlobalInfo.getScenes().get(sceneId).getMonsterHashMap());
        sceneDetailBo.setNpcIdArray(SceneResource.getScenesStatics().get(o.getSceneId()).getNpcId());
        return sceneDetailBo;
    }

    //修理装备
    @Override
    public String repairEquipment(int equipmentId,Role role){
        Equipment equipment = role.getEquipmentHashMap().get(equipmentId);
        equipment.setDura(ResourceSearch.getEquipmentDura(equipmentId));
        return Const.service.REPAIR_SUCCESS +equipment.getDura();
    }

    //穿戴装备-从背包里拿出来穿戴
    @Override
    public String putOnEquipment(int equipmentId,Role role){
        int atk = role.getAtk();
        atk = atk + ResourceSearch.getEquipmentAtk(equipmentId);
        role.setAtk(atk);
        //增加其他属性-待扩展
        EquipmentStatic equipmentInfo = EquipmentResource.getEquipmentStaticHashMap().get(equipmentId);
        //遍历身上的装备-6件，看是否是同一类型的装备，类型相同，背包与身上装备互换，类型不同-直接穿无需返回给背包
        for(Integer selfEquipId : role.getEquipmentHashMap().keySet()){
            if(equipmentInfo.getType()==EquipmentResource.getEquipmentStaticHashMap().get(selfEquipId).getType()){
                Equipment equipment1 = new Equipment(equipmentId,equipmentInfo.getDurability());
                role.getEquipmentHashMap().put(equipmentId,equipment1);
                role.getMyPackageBo().getGoodsHashMap().remove(equipmentId);
                role.getMyPackageBo().getGoodsHashMap().put(selfEquipId,1);
                iAchievementService.countSumWearLevel(role);
                return Const.service.PUTON_SUCCESS;
            }
        }
        Equipment equipment1 = new Equipment(equipmentId,equipmentInfo.getDurability());
        role.getEquipmentHashMap().put(equipmentId,equipment1);
        role.getMyPackageBo().getGoodsHashMap().remove(equipmentId);
        iAchievementService.countSumWearLevel(role);
        return Const.service.PUTON_SUCCESS;
    }

    //卸下装备
    @Override
    public String takeOffEquipment(int equipmentId,Role role){
        int atk = role.getAtk();
        atk = atk - ResourceSearch.getEquipmentAtk(equipmentId);
        role.setAtk(atk);
        role.getEquipmentHashMap().remove(equipmentId);
        role.getMyPackageBo().getGoodsHashMap().put(equipmentId,1);
        return Const.service.TAKEOFF_SUCCESS;
    }

    //使用药品
    @Override
    public boolean useDrug(int potionId,Role role){
        int hp = role.getHp();
        int mp = role.getMp();
        if(role.getMyPackageBo().getGoodsHashMap().get(potionId)<=0){
            return false;
        }else {
            int num = role.getMyPackageBo().getGoodsHashMap().get(potionId)-1;
            role.getMyPackageBo().getGoodsHashMap().put(potionId,num);
        }
        hp = hp + ResourceSearch.getPotionAddHp(potionId);
        mp = mp + ResourceSearch.getPotionAddMp(potionId);
        role.setHp(hp);
        role.setMp(mp);
        int maxHp = CareerResource.getCareerStaticHashMap().get(Const.CAREER_ID).getHp();
        int maxMp = CareerResource.getCareerStaticHashMap().get(Const.CAREER_ID).getMp();
        if(role.getHp()>=maxHp){
            role.setHp(maxHp);
        }
        if(role.getMp()>=maxMp){
            role.setMp(maxMp);
        }
        return true;
    }

    //选择：任意同场景可以pk玩家-假设可随意使用技能攻击
    @Override
    public String pkPlayer (int skillId,int TargetRoleId, Role role){
        String result = new SkillServiceImpl().skillCommon(skillId,role);
        if(!Const.Fight.SUCCESS.equals(result)){
            return result;
        }
        //到此没有返回，说明技能已经冷却，可以调用该方法
        Role enemy = GlobalInfo.getRoleHashMap().get(TargetRoleId);
        int hp = enemy.getHp();
        int skillHarm = SkillResource.getSkillStaticHashMap().get(skillId).getAtk();
        hp=hp-role.getAtk()-Const.WEAPON_BUFF-skillHarm;
        if(hp<=0){
            result = Const.Fight.PK_SUCCESS;
            //Listen.monsterIsDead=true;  //对全部客户端进行通知-可改为pk玩家的设置
            enemy.setHp(0);
            //enemy.setAlive(0);
            role.setAtk(role.getAtk()+Const.ABTAIN_ATK);
            iPackageService.addMoney(Const.PK_GET_LOST,role);
            enemy.setMoney(enemy.getMoney()-Const.PK_GET_LOST);
            iAchievementService.ifFirstPkSuccess(role);
            return result;
        }
        enemy.setHp(hp);
        return Const.Fight.TARGET_HP+hp;
    }

    //输入坐标，走动到目标位置；
    @Override
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

    //对移动前后的网格进行数据更新，包括移动前后网格中实体的增删，先考虑角色
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

        //三行三列，每个格子进行封装
        for(int k=0;k<3;k++){
            for(int i=curGridId-Const.GRID_WIDTH-1;i<=curGridId-Const.GRID_WIDTH+1;i++){
                Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
                viewGridHashMap.put(i,scene.getGridHashMap().get(i));
                for(Integer key : viewGridHashMap.get(i).getGridRoleMap().keySet()){
                    role.getGridBo().getGridRoleMap().put(key, GlobalInfo.getRoleHashMap().get(key));
                }
                for(String key : viewGridHashMap.get(i).getGridMonsterMap().keySet()){
                    System.out.println("怪物有:"+viewGridHashMap.get(i).getGridMonsterMap().size());
                    Monster monster = scene.getMonsterHashMap().get(key);
                    role.getGridBo().getGridMonsterMap().put(key,monster);
                }
            }
            curGridId+=Const.GRID_WIDTH;
        }
        return role.getGridBo();
    }

    //添加好友申请
    @Override
    public void askFriend(int FriendId,Role role){
        role.getFriendBo().getApplyIdList().add(FriendId);
    }

    //可以增加获取好友申请列表，查看列表后可以执行以下的同意添加方法

    //同意添加好友
    @Override
    public void addFriend(int FriendId,Role role){
        role.getFriendBo().getFriendIdList().add(role.getId());
        role.getFriendBo().getApplyIdList().remove(FriendId);
        iAchievementService.countAddFriend(FriendId,role);
    }

    //升级
    @Override
    public void levelUp(int level,Role role){
        role.setLevel(level);
        iAchievementService.countLevel(role);
    }

    @Override
    public String getAchievment(Role role){
        return iAchievementService.getAchievmentList(role);
    }

    @Override
    public String getPackage(Role role){
        String list="";
        for(Integer goodsId : role.getMyPackageBo().getGoodsHashMap().keySet()){
            if(role.getMyPackageBo().getGoodsHashMap().get(goodsId)>0){
                list+=goodsId+" ";
            }
        }
        return list;
    }

    @Override
    public String getBodyEquip(Role role){
        String list="";
        for(Integer equipId : role.getEquipmentHashMap().keySet()){
            list+=equipId+" ";
        }
        return list;
    }

    //打印场景信息
    @Override
    public String printSceneDetail(int sceneId){
        SceneDetailBo sceneDetailBo = placeDetail(sceneId);
        StringBuilder stringBuilder = new StringBuilder("地点："+ sceneDetailBo.getSceneName()+"。 角色：");
        for(int i = 0; i< sceneDetailBo.getRoleArrayList().size(); i++) {
            stringBuilder.append(sceneDetailBo.getRoleArrayList().get(i).getName()).append(" ");
        }
        if(sceneDetailBo.getNpcIdArray()!=null){
            stringBuilder.append("。 NPC：");
            for(int i = 0; i< sceneDetailBo.getNpcIdArray().length; i++) {
                Integer npcId = Integer.valueOf(sceneDetailBo.getNpcIdArray()[i]);
                stringBuilder.append(NpcResource.getNpcsStatics().get(npcId).getName()).append(" ");
            }
        }
        stringBuilder.append("。 怪物：");
        for(String key: sceneDetailBo.getMonsterHashMap().keySet()){
            if(sceneDetailBo.getMonsterHashMap().get(key).getAlive()==1){
                Integer monsterId = sceneDetailBo.getMonsterHashMap().get(key).getMonsterId();
                stringBuilder.append(MonsterResource.getMonstersStatics().get(monsterId).getName()).append(" ");
            }
        }
        return stringBuilder.toString();
    }

    //打印视野信息-角色集合
    @Override
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
        stringBuilder.append("。 怪物：");
        for(String key : gridBo.getGridMonsterMap().keySet()){
            Monster monster = gridBo.getGridMonsterMap().get(key);
            stringBuilder.append(MonsterResource.getMonstersStatics().get(monster.getMonsterId()).getName()+" 位置："
                    +monster.getPosition()[0]+","
                    +monster.getPosition()[1]+"；");
        }
        return stringBuilder.toString();
    }

    @Override
    public String getNpcReply(int npcId,Role role){
        if(AssistService.checkNpcId(npcId,role)){
            iAchievementService.talkToNpc(npcId,role);
            return NpcResource.getNpcsStatics().get(npcId).getWords();
        }
        return "wrong";
    }

    @Override
    public String getRoleInfo(Role role){
        return  "id:"+role.getId()+"\n"+"name:"+role.getName()+"\n"+"hp："+role.getHp()+"\n"+
                "mp："+role.getMp()+"\n"+"atk："+role.getAtk()+"\n"+"money："+role.getMoney()+"\n"+
                "nowScenesId:"+role.getNowScenesId()+"\n"+"careerId:"+role.getCareerId()+"\n"+
                "unionId:"+role.getUnionId()+"\n"+"level:"+role.getLevel()+"\n"+
                "背包物品："+getPackage(role)+"\n"+"身上装备："+getBodyEquip(role)+"\n";
    }

    @Override
    public String getMonsterInfo(int monsterId,Role role){
        int nowScenesId = role.getNowScenesId();
        Monster nowMonster = GlobalInfo.getScenes().get(nowScenesId).getMonsterHashMap().get(monsterId);
        return "hp：" + nowMonster.getMonsterHp() + "，状态："+ nowMonster.getAlive();
    }

    //测试用命令
    @Override
    public String testCode(Role role){
        role.getMyPackageBo().randPackageGrid();
        return role.getMyPackageBo().getPackageGrid();
    }

    @Override
    public String orderPackage(Role role){
        role.getMyPackageBo().orderPackageGrid();
        return getPackageInfo(role);
    }

    @Override
    public String randPackage(Role role){
        role.getMyPackageBo().randPackageGrid();
        return getPackageInfo(role);
    }

    @Override
    public String getPackageInfo(Role role){
        return role.getMyPackageBo().getPackageGrid();
    }

}


