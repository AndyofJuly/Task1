package com.game.service;

import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.dao.RoleMapper;
import com.game.entity.*;
import com.game.entity.excel.MonsterStatic;
import com.game.entity.store.*;
import com.game.entity.vo.GridVo;
import com.game.entity.vo.SceneDetailVo;
import com.game.service.assis.*;

import java.time.Instant;
import java.util.HashMap;


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
        String nowPlace = SceneResource.getScenesStatics().get(GlobalResource.getScenes().get(role.getNowScenesId()).getSceneId()).getName();
        //将当前场景坐标与要移动的场景的坐标进行对比，此处arr为位置关系数组
        String[] arr = SceneResource.getPlaces().get(AssistService.checkSceneId(nowPlace));
        boolean result = false;
        for (String value : arr) {
            int tempId = AssistService.checkDynSceneId(moveTarget);
            String innTarget = SceneResource.getScenesStatics().get(GlobalResource.getScenes().get(tempId).getSceneId()).getName();
            if (innTarget.equals(SceneResource.getScenesStatics().get(Integer.valueOf(value)).getName())){
                result = true;
                break;
            }
        }
        //如果可以移动成功，当前场景剔除该角色，目标场景加入该角色
        if(result){
            Integer lastSceneId = AssistService.checkDynSceneId(moveTarget);

            //视野扩展更新代码两条，先原地址移入，再新地址加入，加入后暂时保持网格id不变
            GlobalResource.getScenes().get(role.getNowScenesId()).getGridHashMap().get(role.getCurGridId()).getGridRoleMap().remove(role.getId());
            GlobalResource.getScenes().get(lastSceneId).getGridHashMap().get(role.getCurGridId()).getGridRoleMap().put(role.getId(),role);

            GlobalResource.getScenes().get(role.getNowScenesId()).getRoleAll().remove(role);
            GlobalResource.getScenes().get(lastSceneId).getRoleAll().add(role);
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
        Scene o = GlobalResource.getScenes().get(j);
        sceneDetailVo.setSceneName(scenesName);
        sceneDetailVo.setRoleArrayList(o.getRoleAll());
        sceneDetailVo.setMonsterHashMap(GlobalResource.getScenes().get(j).getMonsterHashMap());
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

    //输入坐标，走动到目标位置；
    public int[] walkTo(int x,int y,int roleId){
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        int oldX = role.getPosition()[0];
        int oldY = role.getPosition()[1];
        //一次只能移动一个格子，左右距离差不超过16，上下距离差不超过8；只能改变其中一个坐标的位置；
        if(Math.abs(oldX-x)>=Const.GRID_LENGTH || Math.abs(oldY-y)>=Const.GRID_WIDTH || (oldX-x!=0 && oldY-y!=0)){
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
        HashMap<Integer, Grid> gridHashMap = GlobalResource.getScenes().get(role.getNowScenesId()).getGridHashMap();
        if(oldGridId==newGridId){return;}
        gridHashMap.get(oldGridId).getGridRoleMap().remove(role.getId());
        gridHashMap.get(newGridId).getGridRoleMap().put(role.getId(),role);
        role.setCurGridId(newGridId);
    }

    public static int getGridId(int x,int y){
        return x/Const.GRID_LENGTH+1+y/Const.GRID_WIDTH*Const.GRID_WIDTH;
    }

    //封装成九宫格
    public GridVo getMyView(int roleId){
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        int curGridId = role.getCurGridId();
        HashMap<Integer,Grid> viewGridHashMap = new HashMap<>();
        role.getGridVoHashMap().get(roleId).getGridRoleMap().clear();//清理上一次留存下来的
        role.getGridVoHashMap().get(roleId).getGridMonsterMap().clear();
        for(int k=0;k<2;k++){
            for(int i=curGridId-Const.GRID_WIDTH-1;i<=curGridId-Const.GRID_WIDTH+1;i++){
                viewGridHashMap.put(i,GlobalResource.getScenes().get(role.getNowScenesId()).getGridHashMap().get(i));
                for(Integer key : viewGridHashMap.get(i).getGridRoleMap().keySet()){
                    role.getGridVoHashMap().get(roleId).getGridRoleMap().put(key,GlobalResource.getRoleHashMap().get(key));
                }
                for(String key : viewGridHashMap.get(i).getGridMonsterMap().keySet()){
                    System.out.println("怪物有:"+viewGridHashMap.get(i).getGridMonsterMap().size());
                    role.getGridVoHashMap().get(roleId).getGridMonsterMap().put(key,GlobalResource.getScenes().get(role.getNowScenesId()).getMonsterHashMap().get(key));
                }
            }
            curGridId+=Const.GRID_WIDTH;
        }
        return role.getGridVoHashMap().get(roleId);
    }

    //测试用命令
    public String testCode(int roleId){
        getMyView(roleId);
        return "";
    }
}


