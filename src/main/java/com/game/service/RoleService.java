package com.game.service;

import com.game.common.Const;
import com.game.dao.RoleMapper;
import com.game.entity.*;
import com.game.entity.excel.EquipmentStatic;
import com.game.entity.store.CareerResource;
import com.game.entity.store.EquipmentResource;
import com.game.entity.store.SceneResource;
import com.game.entity.store.SkillResource;
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

    //here
    //角色移动&场景切换，此处moveTarget为外部名字 tempId为动态随机id，角色所在当前场景id为临时场景id
    public boolean moveTo(int lastSceneId,Role role){
        //nowPlace还是用静态的名字，传送时用的是外部的 todo 需要统一使用外部；已有静态id与外部id相同，新建临时场景外部id是随机的，与静态id有所不同，但可以根据外部id查找内部静态id
        int scenesId = GlobalResource.getScenes().get(role.getNowScenesId()).getSceneId();//获得内部id
        String nowPlace = SceneResource.getScenesStatics().get(scenesId).getName();
        //将当前场景坐标与要移动的场景的坐标进行对比，此处arr为位置关系数组
        String[] arr = SceneResource.getPlaces().get(AssistService.checkSceneId(nowPlace));
        boolean result = false;
        for (String value : arr) {
            //int tempId = AssistService.checkDynSceneId(moveTarget);
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
            //数据库相关操作可以留在用户退出时再调用
            RoleMapper roleMapper = new RoleMapper();
            roleMapper.insertRoleScenes(role.getNowScenesId(),role);

            //宝宝进行跟随
            if(role.getBaby()!=null){
                role.getBaby().setScenneId(lastSceneId);
            }
        }
        return result;
    }

    //获得场景信息
    public SceneDetailVo placeDetail(int sceneId){//printPlaceDetail
        SceneDetailVo sceneDetailVo = new SceneDetailVo();
        //int j = AssistService.checkDynSceneId(scenesName);
        Scene o = GlobalResource.getScenes().get(sceneId);
        sceneDetailVo.setSceneName(o.getName());
        sceneDetailVo.setRoleArrayList(o.getRoleAll());
        sceneDetailVo.setMonsterHashMap(GlobalResource.getScenes().get(sceneId).getMonsterHashMap());
        sceneDetailVo.setNpcIdArray(SceneResource.getScenesStatics().get(o.getSceneId()).getNpcId());
        return sceneDetailVo;
    }

    //修理装备
    public Equipment repairEquipment(int equipmentId,Role role){
        //int key = AssistService.checkEquipmentId(equipmentName);
        Equipment equipment = role.getEquipmentHashMap().get(equipmentId);
        equipment.setDura(EquipmentHelper.getEquipmentDura(equipmentId));
        return equipment;
    }

    //穿戴装备-从背包里拿出来穿戴
    public void putOnEquipment(int equipmentId,Role role){
        int atk = role.getAtk();
        //int key = AssistService.checkEquipmentId(equipment);
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
                //task
                AchievementService.ifSumEquipmentLevelToFourty(role);
                return;
            }
        }
        Equipment equipment1 = new Equipment(equipmentId,equipmentInfo.getDurability());
        role.getEquipmentHashMap().put(equipmentId,equipment1);
        role.getMyPackage().getGoodsHashMap().remove(equipmentId);
        //task
        AchievementService.ifSumEquipmentLevelToFourty(role);
    }

    //卸下装备
    public void takeOffEquipment(int equipmentId,Role role){
        int atk = role.getAtk();
        //int key = AssistService.checkEquipmentId(equipment);
        atk = atk - EquipmentHelper.getEquipmentAtk(equipmentId);
        role.setAtk(atk);
        role.getEquipmentHashMap().remove(equipmentId);
        role.getMyPackage().getGoodsHashMap().put(equipmentId,1);
    }

    //使用药品
    public boolean useDrug(int potionId,Role role){
        int hp = role.getHp();
        int mp = role.getMp();
        //int key = AssistService.checkPotionId(drugName);
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

    //选择：任意同场景可以pk玩家-假设可随意使用技能攻击 todo 可选，仅限竞技场pk玩家
    public String pkPlayer (int skillId,int TargetRoleId, Role role){
        SkillService skillService = new SkillService();
        String result = skillService.skillCommon(skillId,role);
        if(!Const.Fight.SUCCESS.equals(result)){
            return result;
        }
        //到此没有返回，说明技能已经冷却，可以调用该方法
        //int key1 = AssistService.checkSkillId(skillName);
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
            //role.setMoney(role.getMoney()+Const.PK_GET_LOST);
            PackageService.addMoney(Const.PK_GET_LOST,role);
            enemy.setMoney(enemy.getMoney()-Const.PK_GET_LOST);
            //task
            AchievementService.ifFirstPkSuccess(role);
            return result;
        }
        enemy.setHp(hp);
        return Const.Fight.TARGET_HP+hp;
    }

    //输入坐标，走动到目标位置；
    public int[] walkTo(int x,int y,Role role){
        int oldX = role.getPosition()[0];
        int oldY = role.getPosition()[1];
        //一次只能移动一个格子，左右距离差不超过16，上下距离差不超过8；只能改变其中一个坐标的位置；
        if(Math.abs(oldX-x)>=Const.GRID_LENGTH
                || Math.abs(oldY-y)>=Const.GRID_WIDTH
                || (oldX-x!=0 && oldY-y!=0)){
            return role.getPosition();
        }
        refleshGrid(x,y,role);
        role.getPosition()[0]=x;
        role.getPosition()[1]=y;
        return role.getPosition();
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
/*        Role role = GlobalResource.getRoleHashMap().get(roleId);
        System.out.println(role.getUnion());
        System.out.println(UnionService.unionHashMap.get(1).getMoney());*/

        return AchievementService.getAchievmentList(role);
    }

    //task
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
        //task
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
}


