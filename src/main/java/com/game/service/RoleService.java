package com.game.service;

import com.game.common.InitStaticResource;
import com.game.controller.FunctionService;
import com.game.dao.ConnectSql;
import com.game.entity.Monster;
import com.game.entity.Scene;
import com.game.entity.excel.EquipmentStatic;
import com.game.entity.excel.MonsterStatic;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 角色一些功能的方法实现
 * @Author andy
 * @create 2020/5/13 18:18
 */

public class RoleService {
    ConnectSql connectSql = new ConnectSql();
    public boolean result;

    public String move(String moveTarget){
        if(moveTo(moveTarget)){
            return "移动成功";
        }else {
            return "不可以从这里去这个地方";
        }
    }

    public String aoi(){
        String scenesName = InitStaticResource.scenes.get(FunctionService.role.getNowScenesId()).getSceneStatic().getName();
        return placeDetail(scenesName);
    }

    public String checkPlace(String scenesName){
        return placeDetail(scenesName);
    }

    public String placeDetail(String scenesName){
        StringBuilder stringBuilder = new StringBuilder("要查看的场景为："+scenesName+"；");
        for(int j = InitStaticResource.initSceneId; j< InitStaticResource.initSceneId+InitStaticResource.scenes.size(); j++){
            if(InitStaticResource.scenes.get(j).getSceneStatic().getName().equals(scenesName)){
                Scene o = InitStaticResource.scenes.get(j);
                stringBuilder.append("角色：");
                for(int i=0;i<o.getRoleAll().size();i++) {
                    stringBuilder.append(o.getRoleAll().get(i).getName()).append(" ");
                }
                stringBuilder.append("。 ");
                stringBuilder.append("NPC：");
                for(int i=0;i<o.getSceneStatic().getNpcId().length;i++) {
                    stringBuilder.append(InitStaticResource.npcs.get(Integer.valueOf(o.getSceneStatic().getNpcId()[i])).getNpcStatic().getName()).append(" ");
                }
                stringBuilder.append("。 ");
                stringBuilder.append("怪物：");
                for(int i=0;i<o.getSceneStatic().getMonsterId().length;i++) {
                    stringBuilder.append(InitStaticResource.monsters.get(Integer.valueOf(o.getSceneStatic().getMonsterId()[i])).getMonsterStatic().getName()).append(" ");
                }
                stringBuilder.append("。 ");
            }
        }
        return stringBuilder.toString();
    }

    public boolean moveTo(String moveTarget){
        //获得角色当前所在场景的坐标-通过角色的nowSceneId属性获取，在登录注册时已经获得
        String nowPlace = InitStaticResource.scenes.get(FunctionService.role.getNowScenesId()).getSceneStatic().getName();
        //将当前场景坐标与要移动的场景的坐标进行对比
        String[] arr;
        arr = InitStaticResource.places.get(nowPlace);
        result = false;

        String temp = null;
        for (String value : arr) {
            if (moveTarget.equals(InitStaticResource.scenes.get(Integer.valueOf(value)).getSceneStatic().getName())) {
                result = true;
                temp = value;
                break;
            }
        }
        //如果移动成功，当前场景剔除该角色，目标场景加入该角色
        if(result){
            InitStaticResource.scenes.get(FunctionService.role.getNowScenesId()).getRoleAll().remove(FunctionService.role);
            InitStaticResource.scenes.get(Integer.valueOf(temp)).getRoleAll().add(FunctionService.role);
            //移动后角色属性的场景id改变
            FunctionService.role.setNowScenesId(Integer.valueOf(temp));
            connectSql.insertRoleScenes(FunctionService.role.getNowScenesId());
        }
        return result;
    }

    public String getNpcReply(String NpcName){
        //根据名字从集合npcs中获取到需要的Npc的id
        //遍历，如果该id对应的名字与传参相同，则找出对应id的words返回
        //同时需要保证，玩家只能与当前场景内的npc对话，不可以与其他场景的npc对话
        String replyWords = "该场景没有这个npc";
        for (Integer key : InitStaticResource.npcsStatics.keySet()) {
            if(InitStaticResource.npcsStatics.get(key).getName().equals(NpcName) && FunctionService.role.getNowScenesId()==InitStaticResource.npcsStatics.get(key).getSceneId()){
                replyWords = InitStaticResource.npcsStatics.get(key).getWords();
            }
        }
        //返回words字段
        return replyWords;
    }

    public String repairEquipment(String equipmentName){
        String result = "没有该武器";
        for (Integer key : InitStaticResource.equipmentStaticHashMap.keySet()) {
            if(equipmentName.equals(InitStaticResource.equipmentStaticHashMap.get(key).getName())){
                System.out.println("当前武器的耐久为"+FunctionService.role.getEquipmentStaticHashMap().get(key).getDurability()+"。正在修理...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FunctionService.role.getEquipmentStaticHashMap().get(key).setDurability(InitStaticResource.equipmentStaticHashMap.get(key).getDurability());
                result = "修理成功！当前武器耐久为："+FunctionService.role.getEquipmentStaticHashMap().get(key).getDurability();
            }
        }
        return result;
    }

    public String putOnEquipment(String equipment){

        // TODO: 2020/5/26 换武器的时候，新武器来自背包，旧武器放回背包；

        int atk = FunctionService.role.getAtk();
        for (Integer key : InitStaticResource.equipmentStaticHashMap.keySet()) {
            if(equipment.equals(InitStaticResource.equipmentStaticHashMap.get(key).getName())){
                atk = atk + InitStaticResource.equipmentStaticHashMap.get(key).getAtk();
                FunctionService.role.setAtk(atk);
                FunctionService.role.getEquipmentStaticHashMap().put(key,InitStaticResource.equipmentStaticHashMap.get(key));
            }
        }
        return "你已成功装备该武器，目前攻击力为："+atk;
    }

    public String takeOffEquipment(String equipment){

        // TODO: 2020/5/26  与背包交互；

        int atk = FunctionService.role.getAtk();
        for (Integer key : InitStaticResource.equipmentStaticHashMap.keySet()) {
            if(equipment.equals(InitStaticResource.equipmentStaticHashMap.get(key).getName())){
                atk = atk - InitStaticResource.equipmentStaticHashMap.get(key).getAtk();
                //记得更新缓存
                FunctionService.role.setAtk(atk);
                //角色把武器放回了背包
                FunctionService.role.getEquipmentStaticHashMap().clear();
            }
        }
        return "你已成功卸下该武器，目前攻击力为："+atk;
    }

    public String useDrug(String drugName){

        // TODO: 2020/5/26  与背包交互；

        //根据名字查找id，获取该药品的hp和mp加成
        int hp = FunctionService.role.getHp();
        int mp = FunctionService.role.getMp();
        for (Integer key : InitStaticResource.potionStaticHashMap.keySet()) {
            if(drugName.equals(InitStaticResource.potionStaticHashMap.get(key).getName())){
                System.out.println(key);
                hp = hp + InitStaticResource.potionStaticHashMap.get(key).getAddHp();
                mp = mp + InitStaticResource.potionStaticHashMap.get(key).getAddMp();
                if(hp>=InitStaticResource.roleStaticHashMap.get(101).getLevelHp()){
                    FunctionService.role.setHp(InitStaticResource.roleStaticHashMap.get(101).getLevelHp());
                    return "血已满";
                }else if (mp>=InitStaticResource.roleStaticHashMap.get(101).getLevelMp()){
                    FunctionService.role.setMp(InitStaticResource.roleStaticHashMap.get(101).getLevelMp());
                    return "蓝已满";
                }
                FunctionService.role.setHp(hp);
                FunctionService.role.setMp(mp);
            }
        }
        return "使用药品成功，你的当前血量为："+hp+"， 当前的蓝量为："+mp;
    }

    public String getRoleInfo(){
        return "当前角色的hp："+FunctionService.role.getHp()+"。 mp："+FunctionService.role.getMp()+"。 武器耐久："+FunctionService.role.getEquipmentStaticHashMap().get(3001).getDurability()+"。 攻击力："+FunctionService.role.getAtk();
    }
}


