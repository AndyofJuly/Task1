package com.game.service;

import com.game.common.Const;
import com.game.entity.*;
import com.game.entity.excel.EquipmentStatic;
import com.game.entity.store.*;
import com.game.entity.vo.GridVo;
import com.game.entity.vo.SceneDetailVo;
import com.game.service.assis.AssistService;
import com.game.service.assis.GlobalResource;
import com.game.service.helper.EquipmentHelper;
import com.game.service.helper.PotionHelper;

import java.util.HashMap;


/**
 * 角色一些功能的方法实现
 * @Author andy
 * @create 2020/5/13 18:18
 */

public class RoleService {

    //角色移动&场景切换，此处moveTarget为外部名字 tempId为动态随机id，角色所在当前场景id为临时场景id
    public boolean moveTo(int lastSceneId,Role role){
        int scenesId = GlobalResource.getScenes().get(role.getNowScenesId()).getSceneId();//获得内部id
        String nowPlace = SceneResource.getScenesStatics().get(scenesId).getName();
        //将当前场景坐标与要移动的场景的坐标进行对比，此处arr为位置关系数组
        String[] arr = SceneResource.getPlaces().get(AssistService.checkSceneId(nowPlace));
        boolean result = false;
        for (String value : arr) {
            int sceneId = GlobalResource.getScenes().get(lastSceneId).getSceneId();
            String innTarget = SceneResource.getScenesStatics().get(sceneId).getName();//根据动态id找到静态名
            if (innTarget.equals(SceneResource.getScenesStatics().get(Integer.valueOf(value)).getName())){
                result = true;
                break;
            }
        }
        //如果可以移动成功，当前场景剔除该角色，目标场景加入该角色
        if(result){
            //Integer lastSceneId = AssistService.checkDynSceneId(moveTarget);//根据静态名，得到外部id
            //视野扩展更新代码两条，先原地址移除，再新地址加入，加入后暂时保持网格id不变
            Grid before = GlobalResource.getScenes().get(role.getNowScenesId()).getGridHashMap().get(role.getCurGridId());
            before.getGridRoleMap().remove(role.getId());
            Grid after = GlobalResource.getScenes().get(lastSceneId).getGridHashMap().get(role.getCurGridId());
            after.getGridRoleMap().put(role.getId(),role);

            GlobalResource.getScenes().get(role.getNowScenesId()).getRoleAll().remove(role);
            GlobalResource.getScenes().get(lastSceneId).getRoleAll().add(role);

            role.setNowScenesId(lastSceneId);

            if(role.getBaby()!=null){
                role.getBaby().setScenneId(lastSceneId);
            }
        }
        return result;
    }

    //获得场景信息
    public SceneDetailVo placeDetail(int sceneId){
        SceneDetailVo sceneDetailVo = new SceneDetailVo();
        Scene o = GlobalResource.getScenes().get(sceneId);
        sceneDetailVo.setSceneName(o.getName());
        sceneDetailVo.setRoleArrayList(o.getRoleAll());
        sceneDetailVo.setMonsterHashMap(GlobalResource.getScenes().get(sceneId).getMonsterHashMap());
        sceneDetailVo.setNpcIdArray(SceneResource.getScenesStatics().get(o.getSceneId()).getNpcId());
        return sceneDetailVo;
    }

    //修理装备
    public String repairEquipment(int equipmentId,Role role){
        Equipment equipment = role.getEquipmentHashMap().get(equipmentId);
        equipment.setDura(EquipmentHelper.getEquipmentDura(equipmentId));
        return Const.service.REPAIR_SUCCESS +equipment.getDura();
    }

    //穿戴装备-从背包里拿出来穿戴
    public String putOnEquipment(int equipmentId,Role role){
        int atk = role.getAtk();
        atk = atk + EquipmentHelper.getEquipmentAtk(equipmentId);
        role.setAtk(atk);
        //增加其他属性-待扩展
        EquipmentStatic equipmentInfo = EquipmentResource.getEquipmentStaticHashMap().get(equipmentId);
        //遍历身上的装备-6件，看是否是同一类型的装备，类型相同，背包与身上装备互换，类型不同-直接穿无需返回给背包
        for(Integer selfEquipId : role.getEquipmentHashMap().keySet()){
            if(equipmentInfo.getType()==EquipmentResource.getEquipmentStaticHashMap().get(selfEquipId).getType()){
                Equipment equipment1 = new Equipment(equipmentId,equipmentInfo.getDurability());
                role.getEquipmentHashMap().put(equipmentId,equipment1);
                role.getMyPackage().getGoodsHashMap().remove(equipmentId);
                role.getMyPackage().getGoodsHashMap().put(selfEquipId,1);
                AchievementService.ifSumEquipmentLevelToFourty(role);
                return Const.service.PUTON_SUCCESS;
            }
        }
        Equipment equipment1 = new Equipment(equipmentId,equipmentInfo.getDurability());
        role.getEquipmentHashMap().put(equipmentId,equipment1);
        role.getMyPackage().getGoodsHashMap().remove(equipmentId);
        AchievementService.ifSumEquipmentLevelToFourty(role);
        return Const.service.PUTON_SUCCESS;
    }

    //卸下装备
    public String takeOffEquipment(int equipmentId,Role role){
        int atk = role.getAtk();
        atk = atk - EquipmentHelper.getEquipmentAtk(equipmentId);
        role.setAtk(atk);
        role.getEquipmentHashMap().remove(equipmentId);
        role.getMyPackage().getGoodsHashMap().put(equipmentId,1);
        return Const.service.TAKEOFF_SUCCESS;
    }

    //使用药品
    public boolean useDrug(int potionId,Role role){
        int hp = role.getHp();
        int mp = role.getMp();
        if(role.getMyPackage().getGoodsHashMap().get(potionId)<=0){
            return false;
        }else {
            int num = role.getMyPackage().getGoodsHashMap().get(potionId)-1;
            role.getMyPackage().getGoodsHashMap().put(potionId,num);
        }
        hp = hp + PotionHelper.getPotionAddHp(potionId);
        mp = mp + PotionHelper.getPotionAddMp(potionId);
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

    //选择：任意同场景可以pk玩家-假设可随意使用技能攻击  可选，仅限竞技场pk玩家
    public String pkPlayer (int skillId,int TargetRoleId, Role role){
        SkillService skillService = new SkillService();
        String result = skillService.skillCommon(skillId,role);
        if(!Const.Fight.SUCCESS.equals(result)){
            return result;
        }
        //到此没有返回，说明技能已经冷却，可以调用该方法
        Role enemy = GlobalResource.getRoleHashMap().get(TargetRoleId);
        int hp = enemy.getHp();
        int skillHarm = SkillResource.getSkillStaticHashMap().get(skillId).getAtk();
        hp=hp-role.getAtk()-Const.WEAPON_BUFF-skillHarm;
        if(hp<=0){
            result = Const.Fight.PK_SUCCESS;
            //Listen.monsterIsDead=true;  //对全部客户端进行通知-可改为pk玩家的设置
            enemy.setHp(Const.ZERO);
            //enemy.setAlive(0);
            role.setAtk(role.getAtk()+Const.ABTAIN_ATK);
            PackageService.addMoney(Const.PK_GET_LOST,role);
            enemy.setMoney(enemy.getMoney()-Const.PK_GET_LOST);
            AchievementService.ifFirstPkSuccess(role);
            return result;
        }
        enemy.setHp(hp);
        return Const.Fight.TARGET_HP+hp;
    }

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

    //对移动前后的网格进行数据更新，包括移动前后网格中实体的增删，先考虑角色
    private void refleshGrid(int x,int y,Role role){
        int oldGridId = getGridId(role.getPosition()[0],role.getPosition()[1]);
        int newGridId = getGridId(x,y);
        Scene scene = GlobalResource.getScenes().get(role.getNowScenesId());
        if(oldGridId==newGridId){return;}
        scene.getGridHashMap().get(oldGridId).getGridRoleMap().remove(role.getId());
        scene.getGridHashMap().get(newGridId).getGridRoleMap().put(role.getId(),role);
        role.setCurGridId(newGridId);
    }

    public static int getGridId(int x,int y){
        return x/Const.GRID_LENGTH+1+y/Const.GRID_WIDTH*Const.GRID_WIDTH;
    }

    //封装成九宫格
    public GridVo getGridVo(Role role){
        int curGridId = role.getCurGridId();
        HashMap<Integer,Grid> viewGridHashMap = new HashMap<>();
        role.getGridVo().getGridRoleMap().clear();//清理上一次留存下来的
        role.getGridVo().getGridMonsterMap().clear();

        //三行三列，每个格子进行封装
        for(int k=0;k<3;k++){
            for(int i=curGridId-Const.GRID_WIDTH-1;i<=curGridId-Const.GRID_WIDTH+1;i++){
                Scene scene = GlobalResource.getScenes().get(role.getNowScenesId());
                viewGridHashMap.put(i,scene.getGridHashMap().get(i));
                for(Integer key : viewGridHashMap.get(i).getGridRoleMap().keySet()){
                    role.getGridVo().getGridRoleMap().put(key,GlobalResource.getRoleHashMap().get(key));
                }
                for(String key : viewGridHashMap.get(i).getGridMonsterMap().keySet()){
                    System.out.println("怪物有:"+viewGridHashMap.get(i).getGridMonsterMap().size());
                    Monster monster = scene.getMonsterHashMap().get(key);
                    role.getGridVo().getGridMonsterMap().put(key,monster);
                }
            }
            curGridId+=Const.GRID_WIDTH;
        }
        return role.getGridVo();
    }

    //测试用命令
    public String testCode(Role role){
        return AchievementService.getAchievmentList(role);
    }

    //添加好友申请
    public void askFriend(int FriendId,Role role){
        role.getFriendVo().getApplyIdList().add(FriendId);
    }

    //可以增加获取好友申请列表，查看列表后可以执行以下的同意添加方法

    //同意添加好友
    public void addFriend(Integer FriendId,Role role){
        role.getFriendVo().getFriendIdList().add(role.getId());
        role.getFriendVo().getApplyIdList().remove(FriendId);
        AchievementService.ifFirstAddOneFriend(FriendId,role);
    }

    //升级
    public void levelUp(int level,Role role){
        role.setLevel(level);
        AchievementService.ifLevelUpToTwenty(role);
    }

    //计算穿戴装备总等级
    public static int sumWearLevel(Role role){
        int sumLevel = 0;
        for(Integer equipmentId : role.getEquipmentHashMap().keySet()){
            sumLevel+=role.getEquipmentHashMap().get(equipmentId).getLevel();
        }
        return sumLevel;
    }

    public String getAchievment(Role role){
        return AchievementService.getAchievmentList(role);
    }

    public String getPackage(Role role){
        String list="";
        for(Integer goodsId : role.getMyPackage().getGoodsHashMap().keySet()){
            list+=goodsId+" ";
        }
        return list;
    }

    public String getBodyEquip(Role role){
        String list="";
        for(Integer equipId : role.getEquipmentHashMap().keySet()){
            list+=equipId+" ";
        }
        return list;
    }

/*    public String printSceneDetail(Role role){
        int sceneId = role.getNowScenesId();
        SceneDetailVo sceneDetailVo = placeDetail(sceneId);
        return printSceneDetail(sceneDetailVo);
    }*/

/*    public String printSceneDetail(int sceneId){
        SceneDetailVo sceneDetailVo = placeDetail(sceneId);
        return printSceneDetail(sceneDetailVo);
    }*/

    //打印场景信息
    public String printSceneDetail(int sceneId){
        SceneDetailVo sceneDetailVo = placeDetail(sceneId);
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
    public String printViewDetail(Role myRole){
        GridVo gridVo = getGridVo(myRole);
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

    public String getNpcReply(int npcId,Role role){
        if(AssistService.checkNpcId(npcId,role)){
            AchievementService.ifTalkToOldMan(npcId,role);
            return NpcResource.getNpcsStatics().get(npcId).getWords();
        }
        return "wrong";
    }

    public String getRoleInfo(Role role){
        return  "id:"+role.getId()+"\n"+"name:"+role.getName()+"\n"+"hp："+role.getHp()+"\n"+
                "mp："+role.getMp()+"\n"+"atk："+role.getAtk()+"\n"+"money："+role.getMoney()+"\n"+
                "nowScenesId:"+role.getNowScenesId()+"\n"+"careerId:"+role.getCareerId()+"\n"+
                "unionId:"+role.getUnionId()+"\n"+"level:"+role.getLevel()+"\n"+
                "背包物品："+getPackage(role)+"\n"+"身上装备："+getBodyEquip(role)+"\n";
    }

    public String getMonsterInfo(int monsterId,Role role){
        int nowScenesId = role.getNowScenesId();
        Monster nowMonster = GlobalResource.getScenes().get(nowScenesId).getMonsterHashMap().get(monsterId);
        return "hp：" + nowMonster.getMonsterHp() + "，状态："+ nowMonster.getAlive();
    }
}


