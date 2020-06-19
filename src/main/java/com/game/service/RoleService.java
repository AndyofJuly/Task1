package com.game.service;

import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.dao.RoleMapper;
import com.game.entity.*;
import com.game.entity.excel.MonsterStatic;
import com.game.entity.store.*;
import com.game.entity.vo.SceneDetailVo;
import com.game.service.assis.*;

import java.time.Instant;


/**
 * 角色一些功能的方法实现
 * @Author andy
 * @create 2020/5/13 18:18
 */

public class RoleService {

    //角色移动&场景切换
    public boolean moveTo(String moveTarget,int roleId){
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        //名字还是用静态的名字，传送时用的是外部的
        String nowPlace = SceneResource.getScenesStatics().get(InitGame.scenes.get(role.getNowScenesId()).getSceneId()).getName();
        //将当前场景坐标与要移动的场景的坐标进行对比，此处arr为位置关系数组
        String[] arr = SceneResource.getPlaces().get(AssistService.checkSceneId(nowPlace));
        boolean result = false;
        for (String value : arr) {
            int tempId = AssistService.checkDynSceneId(moveTarget);
            String innTarget = SceneResource.getScenesStatics().get(InitGame.scenes.get(tempId).getSceneId()).getName();
            if (innTarget.equals(SceneResource.getScenesStatics().get(Integer.valueOf(value)).getName())){
                result = true;
                break;
            }
        }
        //如果可以移动成功，当前场景剔除该角色，目标场景加入该角色
        if(result){
            InitGame.scenes.get(role.getNowScenesId()).getRoleAll().remove(role);
            Integer lastSceneId = AssistService.checkDynSceneId(moveTarget);
            InitGame.scenes.get(lastSceneId).getRoleAll().add(role);
            role.setNowScenesId(lastSceneId);
            //数据库相关操作可以留在用户退出时再调用
            RoleMapper roleMapper = new RoleMapper();
            roleMapper.insertRoleScenes(role.getNowScenesId(),roleId);

            //宝宝进行跟随
            if(role.getBaby()!=null){
                role.getBaby().setScenneId(lastSceneId);
            }
        }
        return result;
    }

    //获得场景信息
    public SceneDetailVo placeDetail(String scenesName){//printPlaceDetail
        SceneDetailVo sceneDetailVo = new SceneDetailVo();
        int j = AssistService.checkDynSceneId(scenesName);
        Scene o = InitGame.scenes.get(j);
        sceneDetailVo.setSceneName(scenesName);
        sceneDetailVo.setRoleArrayList(o.getRoleAll());
        sceneDetailVo.setMonsterHashMap(InitGame.scenes.get(j).getMonsterHashMap());
        sceneDetailVo.setNpcIdArray(SceneResource.getScenesStatics().get(o.getSceneId()).getNpcId());
        return sceneDetailVo;
    }

    //修理装备
    public Equipment repairEquipment(String equipmentName,int roleId){
        int key = AssistService.checkEquipmentId(equipmentName);
        Equipment equipment = GlobalResource.getRoleHashMap().get(roleId).getEquipmentHashMap().get(key);
        equipment.setDura(EquipmentResource.getEquipmentStaticHashMap().get(key).getDurability());
        return equipment;
    }

    //穿戴装备-从背包里拿出来穿戴
    public void putOnEquipment(String equipment,int roleId){
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        int atk = role.getAtk();
        int key = AssistService.checkEquipmentId(equipment);
        atk = atk + EquipmentResource.getEquipmentStaticHashMap().get(key).getAtk();
        role.setAtk(atk);
        Equipment equipment1 = new Equipment(key,EquipmentResource.getEquipmentStaticHashMap().get(key).getDurability());
        role.getEquipmentHashMap().put(key,equipment1);
        role.getMyPackage().getGoodsHashMap().remove(key);
    }

    //卸下装备
    public void takeOffEquipment(String equipment,int roleId){
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        int atk = role.getAtk();
        int key = AssistService.checkEquipmentId(equipment);
        atk = atk - EquipmentResource.getEquipmentStaticHashMap().get(key).getAtk();
        role.setAtk(atk);
        role.getEquipmentHashMap().remove(key);
        Equipment equipment1 = new Equipment(key,EquipmentResource.getEquipmentStaticHashMap().get(key).getDurability());
        role.getMyPackage().getGoodsHashMap().put(key,1);
    }

    //使用药品
    public boolean useDrug(String drugName,int roleId){
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        int hp = role.getHp();
        int mp = role.getMp();
        int key = AssistService.checkPotionId(drugName);
        if(role.getMyPackage().getGoodsHashMap().get(key)<=0){
            return false;
        }else {
            role.getMyPackage().getGoodsHashMap().put(key,role.getMyPackage().getGoodsHashMap().get(key)-1);
        }
        hp = hp + PotionResource.getPotionStaticHashMap().get(key).getAddHp();
        mp = mp + PotionResource.getPotionStaticHashMap().get(key).getAddMp();
        role.setHp(hp);
        role.setMp(mp);
        if(role.getHp()>=CareerResource.getCareerStaticHashMap().get(Const.CAREER_ID).getHp()){
                //RoleResource.roleStaticHashMap.get(Const.TYPE_ID).getLevelHp()){
            role.setHp(CareerResource.getCareerStaticHashMap().get(Const.CAREER_ID).getHp());
        }
        if(role.getMp()>=CareerResource.getCareerStaticHashMap().get(Const.CAREER_ID).getMp()){
            role.setMp(CareerResource.getCareerStaticHashMap().get(Const.CAREER_ID).getMp());
        }
        return true;
    }

    //选择：任意同场景可以pk玩家-假设可随意使用技能攻击 todo 可选，仅限竞技场pk玩家
    public String pkPlayer (String skillName,int TargetRoleId, int roleId){
        String result = SkillService.skillCommon(skillName,roleId);
        if(!Const.Fight.SUCCESS.equals(result)){
            return result;
        }
        //到此没有返回，说明技能已经冷却，可以调用该方法
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        int key1 = AssistService.checkSkillId(skillName);
        Role enemy = GlobalResource.getRoleHashMap().get(TargetRoleId);
        int hp = enemy.getHp();
        hp=hp-role.getAtk()-SkillResource.getSkillStaticHashMap().get(key1).getAtk()-
                Const.WEAPON_BUFF;
        if(hp<=0){
            result = Const.Fight.PK_SUCCESS;
            //Listen.monsterIsDead=true;  //对全部客户端进行通知-可改为pk玩家的设置
            enemy.setHp(Const.ZERO);
            //enemy.setAlive(0);
            role.setAtk(role.getAtk()+Const.ABTAIN_ATK);
            role.setMoney(role.getMoney()+Const.PK_GET_LOST);
            enemy.setMoney(enemy.getMoney()-Const.PK_GET_LOST);
            return result;
        }
        enemy.setHp(hp);
        return Const.Fight.TARGET_HP+hp;
    }

    //查看角色视野中的其他实体-其他角色
    public String getView(int roleId){
        StringBuilder stringBuilder = new StringBuilder("");
        //当前场景下的所有角色
        Role self = GlobalResource.getRoleHashMap().get(roleId);
        // todo 想办法去掉for循环
        for(Role role : InitGame.scenes.get(self.getNowScenesId()).getRoleAll()){
            if(ifCanSee(self,role)){
                stringBuilder.append(role.getName()+" ");
            }
        }
        return stringBuilder.toString();
    }

    //角色视野是否可见其他角色
    public boolean ifCanSee(Role self,Role role){
        boolean distanceX = (role.getPosition()[0]-self.getPosition()[0]<Const.VIEW_X
                && role.getPosition()[0]-self.getPosition()[0]>-Const.VIEW_X);
        boolean distanceY = (role.getPosition()[1]-self.getPosition()[1]<Const.VIEW_Y
                && role.getPosition()[1]-self.getPosition()[1]>-Const.VIEW_Y);
        return distanceX && distanceY;
    }

    //输入坐标，走动到目标位置
    public int[] walkTo(int x,int y,int roleId){
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        role.getPosition()[0]=x;
        role.getPosition()[1]=y;
        return role.getPosition();
    }


    //测试用命令
    public String testCode(int monsterId,int roleId){
        MonsterStatic monster = MonsterResource.getMonstersStatics().get(monsterId);
        return "["+monster.getPosition()[0]+","+monster.getPosition()[1]+"]";
    }
}


