package com.game.service;

import com.game.common.Const;
import com.game.common.ExcelToJson;
import com.game.common.InitStaticResource;
import com.game.controller.FunctionService;
import com.game.dao.ConnectSql;
import com.game.entity.Equipment;
import com.game.entity.Scene;
import com.game.entity.excel.EquipmentStatic;
import com.game.entity.excel.PotionStatic;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 角色一些功能的方法实现
 * @Author andy
 * @create 2020/5/13 18:18
 */

public class RoleService {
    ConnectSql connectSql = new ConnectSql();
    public boolean result;
    //扩展方法-中间变量
    static int time = 1;
    static int t = 0;
    static int monsterHp;

    //角色移动&场景切换提示信息
    public String move(String moveTarget){
        if(moveTo(moveTarget)){
            return "移动成功";
        }else {
            return "不可以从这里去这个地方";
        }
    }

    //角色移动&场景切换
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

    //获取当前场景信息
    public String aoi(){
        String scenesName = InitStaticResource.scenes.get(FunctionService.role.getNowScenesId()).getSceneStatic().getName();
        return placeDetail(scenesName);
    }

    //查找任意场景信息
    public String checkPlace(String scenesName){
        return placeDetail(scenesName);
    }

    //获得场景信息
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

    //与NPC对话
    public String getNpcReply(String NpcName){
        String replyWords = "该场景没有这个npc";
        for (Integer key : InitStaticResource.npcsStatics.keySet()) {
            if(InitStaticResource.npcsStatics.get(key).getName().equals(NpcName) &&
                    FunctionService.role.getNowScenesId()==InitStaticResource.npcsStatics.get(key).getSceneId()){
                replyWords = InitStaticResource.npcsStatics.get(key).getWords();
            }
        }
        return replyWords;
    }

    //修理装备
    public String repairEquipment(String equipmentName){
        String result = "没有该武器";
        for (Integer key : InitStaticResource.equipmentStaticHashMap.keySet()) {
            if(equipmentName.equals(InitStaticResource.equipmentStaticHashMap.get(key).getName())){
                int dura = InitStaticResource.equipmentStaticHashMap.get(key).getDurability();
                FunctionService.role.getEquipmentHashMap().get(key).getEquipmentStatic().setDurability(dura);
                result = "修理成功！当前武器耐久为："+FunctionService.role.getEquipmentHashMap().get(key).getEquipmentStatic().getDurability();
            }
        }
        return result;
    }

    //穿戴装备
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
                FunctionService.role.getMyPackage().getPackageEquipmentHashMap().remove(key);
            }
        }
        return "你已成功装备该武器，目前攻击力为："+FunctionService.role.getAtk();
    }

    //卸下装备
    public String takeOffEquipment(String equipment){
        int atk = FunctionService.role.getAtk();
        for (Integer key : InitStaticResource.equipmentStaticHashMap.keySet()) {
            if(equipment.equals(InitStaticResource.equipmentStaticHashMap.get(key).getName())){
                atk = atk - InitStaticResource.equipmentStaticHashMap.get(key).getAtk();
                FunctionService.role.setAtk(atk);
                FunctionService.role.getEquipmentHashMap().remove(key);
                Equipment equipment1 = new Equipment(new EquipmentStatic(InitStaticResource.equipmentStaticHashMap.get(key).getId(),
                        InitStaticResource.equipmentStaticHashMap.get(key).getName(),InitStaticResource.equipmentStaticHashMap.get(key).getAtk(),
                        InitStaticResource.equipmentStaticHashMap.get(key).getDurability()));
                FunctionService.role.getMyPackage().getPackageEquipmentHashMap().put(key,equipment1);
            }
        }
        return "你已成功卸下该武器，目前攻击力为："+FunctionService.role.getAtk();
    }

    //使用药品
    public String useDrug(String drugName){
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
                if(FunctionService.role.getHp()>=InitStaticResource.roleStaticHashMap.get(Const.TYPE_ID).getLevelHp()){
                    FunctionService.role.setHp(InitStaticResource.roleStaticHashMap.get(Const.TYPE_ID).getLevelHp());
                }
                if(FunctionService.role.getMp()>=InitStaticResource.roleStaticHashMap.get(Const.TYPE_ID).getLevelMp()){
                    FunctionService.role.setMp(InitStaticResource.roleStaticHashMap.get(Const.TYPE_ID).getLevelMp());
                }
            }
        }
        return "使用药品成功，你的当前血量为："+FunctionService.role.getHp()+"， 当前的蓝量为："+FunctionService.role.getMp();
    }

    //技能攻击
    public String useSkillAttack(String skillName,String Target){
        String string = null;
        int dura;
        int weaponId =0;
        for (Integer key : FunctionService.role.getEquipmentHashMap().keySet()) {
            weaponId = key;
        }
        for (Integer key1 : InitStaticResource.skillStaticHashMap.keySet()) {
            if (skillName.equals(InitStaticResource.skillStaticHashMap.get(key1).getName())) {
                //使用该技能，记录当前时间，set方法传给角色的集合的技能对象的属性，同时判断时间是否合理满足CD
                Instant nowDate = Instant.now();
                Duration between = Duration.between(FunctionService.role.getSkillHashMap().get(key1).getStart(), nowDate);
                long l = between.toMillis();
                if(l>FunctionService.role.getSkillHashMap().get(key1).getSkillStatic().getCd()*Const.GAP_TIME_SKILL){
                    FunctionService.role.getSkillHashMap().get(key1).setStart(nowDate);
                    //说明技能已经冷却，可以调用该方法，怪物扣血
                    for (Integer key : InitStaticResource.monstersStatics.keySet()) {
                        if(InitStaticResource.monstersStatics.get(key).getName().equals(Target) &&
                                FunctionService.role.getNowScenesId()==InitStaticResource.monstersStatics.get(key).getSceneId()){
                            int hp=InitStaticResource.monstersStatics.get(key).getHp();
                            int mp=FunctionService.role.getMp();
                            dura=FunctionService.role.getEquipmentHashMap().get(weaponId).getEquipmentStatic().getDurability();
                            //耐久小于等于0或者蓝量不够，退出场景
                            if(dura<=0){
                                return "武器耐久不够，请先修理再战斗";
                            }
                            if(mp<InitStaticResource.skillStaticHashMap.get(key1).getUseMp()){
                                FunctionService.role.setMp(mp);
                                return "角色蓝量不够，请先恢复再战斗";
                            }
                            hp=hp-FunctionService.role.getAtk()-InitStaticResource.skillStaticHashMap.get(key1).getAtk()-
                                    Const.WEAPON_BUFF;
                            mp=mp-InitStaticResource.skillStaticHashMap.get(key1).getUseMp();
                            FunctionService.role.setMp(mp);
                            FunctionService.role.getEquipmentHashMap().get(weaponId).getEquipmentStatic().setDurability(dura-
                                    Const.DURA_MINUS);
                            if(hp<=0){
                                hp=0;
                                string = "怪物血量为0，你已经打败该怪物！恭喜获得5000银和一个高级宝箱!";
                                Listen.monsterIsDead=true;
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



    //返回角色的hp，mp，武器耐久，当前攻击力
    public String getRoleInfo(){
        int weaponId =0;
        for (Integer key : FunctionService.role.getEquipmentHashMap().keySet()) {
            weaponId = key;
        }
        return "当前角色的hp："+FunctionService.role.getHp()+"。 mp："+FunctionService.role.getMp()+"。 武器耐久："+
                FunctionService.role.getEquipmentHashMap().get(weaponId).getEquipmentStatic().getDurability()+"。 攻击力："+FunctionService.role.getAtk();
    }

    //返回怪物当前状态
    public String getMonsterInfo(String monsterName){
        for (Integer key : InitStaticResource.monstersStatics.keySet()) {
            if (InitStaticResource.monstersStatics.get(key).getName().equals(monsterName)) {
                return "你查询的怪物的hp：" + InitStaticResource.monstersStatics.get(key).getHp();
            }
        }
        return "";
    }

    //扩展，毒素和护盾的技能demo，约定技能表中typeId为1时表示毒素类技能，为2时表示护盾类技能；后续可以与前面技能合并；血量不同步是因为用了临时变量
    public String useSkill(String skillName,String Target){
        String string = null;
        for (Integer key1 : InitStaticResource.skillStaticHashMap.keySet()) {
            if (skillName.equals(InitStaticResource.skillStaticHashMap.get(key1).getName())) {
                Instant nowDate = Instant.now();
                Duration between = Duration.between(FunctionService.role.getSkillHashMap().get(key1).getStart(), nowDate);
                long l = between.toMillis();
                if(l>FunctionService.role.getSkillHashMap().get(key1).getSkillStatic().getCd()*Const.TO_MS){
                    FunctionService.role.getSkillHashMap().get(key1).setStart(nowDate);
                    //如果是中毒类技能，初步思路如下
                    if(InitStaticResource.skillStaticHashMap.get(key1).getTypeId()==Const.POISON_TYPE){
                        //每隔1s怪物扣血，扣的血量为技能基础攻击力
                        for (Integer key : InitStaticResource.monstersStatics.keySet()) {
                            if (InitStaticResource.monstersStatics.get(key).getName().equals(Target) &&
                                    FunctionService.role.getNowScenesId() == InitStaticResource.monstersStatics.get(key).getSceneId()) {
                                monsterHp = InitStaticResource.monstersStatics.get(key).getHp();
                                int mp=FunctionService.role.getMp();
                                //耐久小于等于0或者蓝量不够，退出场景
                                if(mp<InitStaticResource.skillStaticHashMap.get(key1).getUseMp()){
                                    FunctionService.role.setMp(mp);
                                    return "角色蓝量不够，请先恢复再战斗";
                                }
                                Timer timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        //延时time秒后，开始执行
                                        time++;
                                        if(time > InitStaticResource.skillStaticHashMap.get(key1).getDuration()){
                                            time=0;
                                            timer.cancel();
                                        }
                                        //怪物持续扣血
                                        monsterHp=monsterHp-InitStaticResource.skillStaticHashMap.get(key1).getAtk();
                                        InitStaticResource.monstersStatics.get(key).setHp(monsterHp);
                                        System.out.println("正在使用"+skillName+"技能，怪物的血量还有"+monsterHp);
                                    }
                                }, Const.DELAY_TIME, Const.GAP_TIME_SKILL);
                                mp=mp-InitStaticResource.skillStaticHashMap.get(key1).getUseMp();
                                FunctionService.role.setMp(mp);
                                if(monsterHp<=0){
                                    monsterHp=0;
                                    string = "怪物血量为0，你已经打败该怪物！恭喜获得5000银和一个高级宝箱!";
                                    Listen.monsterIsDead=true;
                                    return string;
                                }
                            }
                        }
                    }
                    //如果是护盾类技能，初步思路如下
                    else if(InitStaticResource.skillStaticHashMap.get(key1).getTypeId()==Const.SHIELD_TYPE){
                        //如果护盾为承伤护盾，角色增加对应血量，持续10s，要判断护盾能承受的伤害与怪物对角色的攻击伤害，如果能承受住，最后恢复原血量，如果不能承受住，最后血量为原血量+护盾血量-怪物伤害
                        //此处假设每次使用完毕，角色恢复原血量
                        int hp = FunctionService.role.getHp();
                        FunctionService.role.setHp(InitStaticResource.skillStaticHashMap.get(key1).getAddHp());
                        //此处，角色与怪物之间的攻击过程，待做
                        System.out.println("互相攻击");
                        //结束后，恢复为初始血量
                        FunctionService.role.setHp(hp);
                    }
                }else {
                    string = "该技能冷却中";
                }
            }
        }
        return string;
    }

    //蓝药缓慢恢复demo
    public String slowlyRecoverd(){
        //使用Timer定时器，每隔1s恢复1点mp，药品恢复10点mp后，使用cancel取消定时任务
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                t++;
                if(t> InitStaticResource.potionStaticHashMap.get(Const.MP_ID).getAddMp()){
                    t=0;
                    timer.cancel();
                }
                //增加蓝量，但不会超过等级的蓝量上限
                if(FunctionService.role.getMp()<InitStaticResource.roleStaticHashMap.get(Const.TYPE_ID).getLevelMp()){
                    FunctionService.role.setMp(FunctionService.role.getMp()+Const.RECOVER_MP);
                }
                System.out.println(FunctionService.role.getMp());
            }
        }, Const.DELAY_TIME, Const.GAP_TIME_POTION);
        return "使用药品成功，缓慢恢复中..";
    }
}


