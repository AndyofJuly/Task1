package com.game.service;

import com.game.common.ExcelToJson;
import com.game.common.InitStaticResource;
import com.game.controller.FunctionService;
import com.game.dao.ConnectSql;
import com.game.entity.Equipment;
import com.game.entity.Scene;
import com.game.entity.excel.EquipmentStatic;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;


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
                int dura = InitStaticResource.equipmentStaticHashMap.get(key).getDurability();
                System.out.println(dura);
                FunctionService.role.getEquipmentHashMap().get(key).getEquipmentStatic().setDurability(dura);
                result = "修理成功！当前武器耐久为："+FunctionService.role.getEquipmentHashMap().get(key).getEquipmentStatic().getDurability();
            }
        }
        return result;
    }

    public String putOnEquipment(String equipment){

        int atk = FunctionService.role.getAtk();
        for (Integer key : InitStaticResource.equipmentStaticHashMap.keySet()) {
            if(equipment.equals(InitStaticResource.equipmentStaticHashMap.get(key).getName())){
                atk = atk + InitStaticResource.equipmentStaticHashMap.get(key).getAtk();
                FunctionService.role.setAtk(atk);
                Equipment equipment1 = new Equipment(new EquipmentStatic(InitStaticResource.equipmentStaticHashMap.get(key).getId(),
                        InitStaticResource.equipmentStaticHashMap.get(key).getName(),InitStaticResource.equipmentStaticHashMap.get(key).getAtk(),
                        InitStaticResource.equipmentStaticHashMap.get(key).getDurability()));
                FunctionService.role.getEquipmentHashMap().put(key,equipment1);
                //从背包中去除该装备，因为装备已经在身上了
                FunctionService.role.getMyPackage().getPackageEquipmentHashMap().remove(key);
            }
        }
        return "你已成功装备该武器，目前攻击力为："+FunctionService.role.getAtk();
    }

    public String takeOffEquipment(String equipment){

        int atk = FunctionService.role.getAtk();
        for (Integer key : InitStaticResource.equipmentStaticHashMap.keySet()) {
            if(equipment.equals(InitStaticResource.equipmentStaticHashMap.get(key).getName())){
                atk = atk - InitStaticResource.equipmentStaticHashMap.get(key).getAtk();
                //记得更新缓存
                FunctionService.role.setAtk(atk);
                //角色把武器放回了背包，身上装备栏去掉该武器
                FunctionService.role.getEquipmentHashMap().remove(key);
                //背包加入该武器
                Equipment equipment1 = new Equipment(new EquipmentStatic(InitStaticResource.equipmentStaticHashMap.get(key).getId(),
                        InitStaticResource.equipmentStaticHashMap.get(key).getName(),InitStaticResource.equipmentStaticHashMap.get(key).getAtk(),
                        InitStaticResource.equipmentStaticHashMap.get(key).getDurability()));
                FunctionService.role.getMyPackage().getPackageEquipmentHashMap().put(key,equipment1);
            }
        }
        return "你已成功卸下该武器，目前攻击力为："+FunctionService.role.getAtk();
    }

    public String useDrug(String drugName){
        //根据名字查找id，获取该药品的hp和mp加成
        int hp = FunctionService.role.getHp();
        int mp = FunctionService.role.getMp();
        for (Integer key : InitStaticResource.potionStaticHashMap.keySet()) {
            if(drugName.equals(InitStaticResource.potionStaticHashMap.get(key).getName())){
                System.out.println("使用药品前，背包中还有此药品数量为："+FunctionService.role.getMyPackage().getPotionHashMap().get(key).getNumber());
                if(FunctionService.role.getMyPackage().getPotionHashMap().get(key).getNumber()<=0){
                    return "此药品已经没有了！";
                }else {
                    FunctionService.role.getMyPackage().getPotionHashMap().get(key).setNumber(FunctionService.role.getMyPackage().getPotionHashMap().get(key).getNumber()-1);
                }
                System.out.println("使用药品后，背包中还有此药品数量为："+FunctionService.role.getMyPackage().getPotionHashMap().get(key).getNumber());
                hp = hp + InitStaticResource.potionStaticHashMap.get(key).getAddHp();
                mp = mp + InitStaticResource.potionStaticHashMap.get(key).getAddMp();
                FunctionService.role.setHp(hp);
                FunctionService.role.setMp(mp);
                System.out.println(ExcelToJson.getConfigPath("TYPE_ID"));
                if(FunctionService.role.getHp()>=InitStaticResource.roleStaticHashMap.get(Integer.valueOf(ExcelToJson.getConfigPath("TYPE_ID"))).getLevelHp()){
                    FunctionService.role.setHp(InitStaticResource.roleStaticHashMap.get(Integer.valueOf(ExcelToJson.getConfigPath("TYPE_ID"))).getLevelHp());
                }
                if(FunctionService.role.getMp()>=InitStaticResource.roleStaticHashMap.get(Integer.valueOf(ExcelToJson.getConfigPath("TYPE_ID"))).getLevelMp()){
                    FunctionService.role.setMp(InitStaticResource.roleStaticHashMap.get(Integer.valueOf(ExcelToJson.getConfigPath("TYPE_ID"))).getLevelMp());
                }
            }
        }
        return "使用药品成功，你的当前血量为："+FunctionService.role.getHp()+"， 当前的蓝量为："+FunctionService.role.getMp();
    }

    public String useSkillAttack(String skillName,String Target){
        String string = null;
        int dura;
        for (Integer key1 : InitStaticResource.skillStaticHashMap.keySet()) {
            if (skillName.equals(InitStaticResource.skillStaticHashMap.get(key1).getName())) {
                //使用该技能，记录当前时间，set方法传给角色的集合的技能对象的属性，同时判断时间是否合理满足CD
                Instant nowDate = Instant.now();
                Duration between = Duration.between(FunctionService.role.getSkillHashMap().get(key1).getStart(), nowDate);
                long l = between.toMillis();
                if(l>FunctionService.role.getSkillHashMap().get(key1).getSkillStatic().getCd()*1000){
                    FunctionService.role.getSkillHashMap().get(key1).setStart(nowDate);
                    //说明技能已经冷却，可以调用该方法，怪物扣血
                    for (Integer key : InitStaticResource.monstersStatics.keySet()) {
                        if(InitStaticResource.monstersStatics.get(key).getName().equals(Target) && FunctionService.role.getNowScenesId()==InitStaticResource.monstersStatics.get(key).getSceneId()){
                            int hp=InitStaticResource.monstersStatics.get(key).getHp();
                            int mp=FunctionService.role.getMp();
                            System.out.println(InitStaticResource.equipmentStaticHashMap.get(3001).getDurability()+";"+FunctionService.role.getEquipmentHashMap().get(3001).getEquipmentStatic().getDurability());
                            dura=FunctionService.role.getEquipmentHashMap().get(3001).getEquipmentStatic().getDurability();
                            //耐久小于等于0或者蓝量不够，退出场景
                            if(dura<=0){
                                return "武器耐久不够，请先修理再战斗";
                            }
                            if(mp<InitStaticResource.skillStaticHashMap.get(key1).getUseMp()){
                                FunctionService.role.setMp(mp);
                                return "角色蓝量不够，请先恢复再战斗";
                            }
                            hp=hp-FunctionService.role.getAtk()-InitStaticResource.skillStaticHashMap.get(key1).getAtk()-2;//2为持有的武器伤害加成
                            mp=mp-InitStaticResource.skillStaticHashMap.get(key1).getUseMp();
                            FunctionService.role.setMp(mp);
                            FunctionService.role.getEquipmentHashMap().get(3001).getEquipmentStatic().setDurability(dura-1);
                            System.out.println(InitStaticResource.equipmentStaticHashMap.get(3001).getDurability()+";"+FunctionService.role.getEquipmentHashMap().get(3001).getEquipmentStatic().getDurability());
                            if(hp<=0){
                                hp=0;
                                string = "怪物血量为0，你已经打败该怪物！恭喜获得5000银和一个高级宝箱!";
                                return string;
                            }
                            System.out.println("玩家使用了"+skillName+"技能，怪物的血量还有"+hp);
                            InitStaticResource.monstersStatics.get(key).setHp(hp);
                        }
                    }
                }else {
                    string = "该技能冷却中";
                }
            }
        }
        return string;
    }

    public String getRoleInfo(){
        return "当前角色的hp："+FunctionService.role.getHp()+"。 mp："+FunctionService.role.getMp()+"。 武器耐久："+FunctionService.role.getEquipmentHashMap().get(3001).getEquipmentStatic().getDurability()+"。 攻击力："+FunctionService.role.getAtk();
    }


}


