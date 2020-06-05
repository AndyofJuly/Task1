package com.game.service;

import com.game.common.Const;
import com.game.controller.FunctionService;
import com.game.dao.ConnectSql;
import com.game.entity.Equipment;
import com.game.entity.Scene;
import com.game.entity.store.*;

import java.time.Duration;
import java.time.Instant;


/**
 * 角色一些功能的方法实现
 * @Author andy
 * @create 2020/5/13 18:18
 */

public class RoleService {
    //ConnectSql connectSql = new ConnectSql();
    public boolean result;
    //扩展方法-中间变量
    static int time = 1;
    static int t = 0;
    static int monsterHp;

    //角色移动&场景切换提示信息
    public String move(String moveTarget,int roleId){
        if(moveTo(moveTarget,roleId)){
            return "移动成功";
        }else {
            return "不可以从这里去这个地方";
        }
    }

    //角色移动&场景切换
    public boolean moveTo(String moveTarget,int roleId){
        //获得角色当前所在场景的坐标-通过角色的nowSceneId属性获取，在登录注册时已经获得
        String nowPlace = SceneResource.scenesStatics.get(FunctionService.roleHashMap.get(roleId).getNowScenesId()).getName();
        //将当前场景坐标与要移动的场景的坐标进行对比
        String[] arr;
        arr = SceneResource.places.get(nowPlace);
        result = false;

        String temp = null;
        for (String value : arr) {
            if (moveTarget.equals(SceneResource.scenesStatics.get(Integer.valueOf(value)).getName())) {
                result = true;
                temp = value;
                break;
            }
        }
        //如果移动成功，当前场景剔除该角色，目标场景加入该角色
        if(result){
            SceneResource.scenes.get(FunctionService.roleHashMap.get(roleId).getNowScenesId()).getRoleAll().remove(FunctionService.roleHashMap.get(roleId));
            SceneResource.scenes.get(Integer.valueOf(temp)).getRoleAll().add(FunctionService.roleHashMap.get(roleId));
            //移动后角色属性的场景id改变
            FunctionService.roleHashMap.get(roleId).setNowScenesId(Integer.valueOf(temp));
            //数据库相关操作可以留在用户退出时再调用
            ConnectSql.sql.insertRoleScenes(FunctionService.roleHashMap.get(roleId).getNowScenesId(),roleId);
        }
        return result;
    }

    //获取当前场景信息-可以修改为直接传id查，此处修改时需要的是动态结果，场景中应该生成一些实体怪物，怪物打死后aoi不显示，用alive字段来判断
    public String aoi(int roleId){
        String scenesName = SceneResource.scenesStatics.get(FunctionService.roleHashMap.get(roleId).getNowScenesId()).getName();
        return placeDetail(scenesName);
    }

    //查找任意场景信息
    public String checkPlace(String scenesName){
        return placeDetail(scenesName);
    }

    //获得场景信息
    public String placeDetail(String scenesName){
        StringBuilder stringBuilder = new StringBuilder("要查看的场景为："+scenesName+"；");
        for(int j = SceneResource.initSceneId; j< SceneResource.initSceneId+SceneResource.scenes.size(); j++){
            if(SceneResource.scenesStatics.get(j).getName().equals(scenesName)){
                Scene o = SceneResource.scenes.get(j);
                stringBuilder.append("角色：");
                for(int i=0;i<o.getRoleAll().size();i++) {
                    stringBuilder.append(o.getRoleAll().get(i).getName()).append(" ");
                }
                stringBuilder.append("。 ");
                stringBuilder.append("NPC：");
                for(int i=0;i<SceneResource.scenesStatics.get(o.getSceneId()).getNpcId().length;i++) {
                    //该场景下的所有npc的名字：InitStaticResource.npcstatic.get(这个场景下的npc的key).
                    stringBuilder.append(NpcResource.npcsStatics.get(Integer.valueOf(SceneResource.scenesStatics.get(o.getSceneId()).getNpcId()[i])).getName()).append(" ");
                }
                stringBuilder.append("。 ");
                stringBuilder.append("怪物：");
                //此处改为使用动态的怪物对象
                //for(int i=0;i<SceneResource.scenesStatics.get(o.getSceneId()).getMonsterId().length;i++) {
                for(Integer key:SceneResource.scenes.get(j).getMonsterHashMap().keySet()){
                    if(MonsterResource.monsters.get(key).getAlive()==1){
                        stringBuilder.append(MonsterResource.monstersStatics.get(key).getName()).append(" ");
                    }
                }
                stringBuilder.append("。 ");
            }
        }
        return stringBuilder.toString();
    }

    //与NPC对话
    public String getNpcReply(String npcName,int roleId){
        String replyWords = "该场景没有这个npc";
        int key = CheckIdByName.checkNpcId(npcName,roleId);
        replyWords = NpcResource.npcsStatics.get(key).getWords();
        return replyWords;
    }

    //修理装备
    public String repairEquipment(String equipmentName,int roleId){
        String result = "没有该武器";
        int key = CheckIdByName.checkEquipmentId(equipmentName);
        FunctionService.roleHashMap.get(roleId).getEquipmentHashMap().get(key).setDura(EquipmentResource.equipmentStaticHashMap.get(key).getDurability());
        result = "修理成功！当前武器耐久为："+FunctionService.roleHashMap.get(roleId).getEquipmentHashMap().get(key).getDura();
        return result;
    }

    //穿戴装备-从背包里拿出来穿戴
    public String putOnEquipment(String equipment,int roleId){
        int atk = FunctionService.roleHashMap.get(roleId).getAtk();
        int key = CheckIdByName.checkEquipmentId(equipment);
        atk = atk + EquipmentResource.equipmentStaticHashMap.get(key).getAtk();
        FunctionService.roleHashMap.get(roleId).setAtk(atk);
        Equipment equipment1 = new Equipment(key,EquipmentResource.equipmentStaticHashMap.get(key).getDurability());
        FunctionService.roleHashMap.get(roleId).getEquipmentHashMap().put(key,equipment1);
        FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().remove(key);
        System.out.println("装备了");
        return "你已成功装备该武器，目前攻击力为："+FunctionService.roleHashMap.get(roleId).getAtk();
    }

    //卸下装备
    public String takeOffEquipment(String equipment,int roleId){
        int atk = FunctionService.roleHashMap.get(roleId).getAtk();
        int key = CheckIdByName.checkEquipmentId(equipment);
        atk = atk - EquipmentResource.equipmentStaticHashMap.get(key).getAtk();
        FunctionService.roleHashMap.get(roleId).setAtk(atk);
        FunctionService.roleHashMap.get(roleId).getEquipmentHashMap().remove(key);
        Equipment equipment1 = new Equipment(key,EquipmentResource.equipmentStaticHashMap.get(key).getDurability());
        FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().put(key,1);
        return "你已成功卸下该武器，目前攻击力为："+FunctionService.roleHashMap.get(roleId).getAtk();
    }

    //使用药品
    public String useDrug(String drugName,int roleId){
        int hp = FunctionService.roleHashMap.get(roleId).getHp();
        int mp = FunctionService.roleHashMap.get(roleId).getMp();
        int key = CheckIdByName.checkPotionId(drugName);
        System.out.println("使用药品前，背包中还有此药品数量为："+FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().get(key));
        if(FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().get(key)<=0){
            return "此药品已经没有了！";
        }else {
            FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().put(key,FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().get(key)-1);
        }
        System.out.println("使用药品后，背包中还有此药品数量为："+FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().get(key));
        hp = hp + PotionResource.potionStaticHashMap.get(key).getAddHp();
        mp = mp + PotionResource.potionStaticHashMap.get(key).getAddMp();
        FunctionService.roleHashMap.get(roleId).setHp(hp);
        FunctionService.roleHashMap.get(roleId).setMp(mp);
        if(FunctionService.roleHashMap.get(roleId).getHp()>=RoleResource.roleStaticHashMap.get(Const.TYPE_ID).getLevelHp()){
            FunctionService.roleHashMap.get(roleId).setHp(RoleResource.roleStaticHashMap.get(Const.TYPE_ID).getLevelHp());
        }
        if(FunctionService.roleHashMap.get(roleId).getMp()>=RoleResource.roleStaticHashMap.get(Const.TYPE_ID).getLevelMp()){
            FunctionService.roleHashMap.get(roleId).setMp(RoleResource.roleStaticHashMap.get(Const.TYPE_ID).getLevelMp());
        }
        return "使用药品成功，你的当前血量为："+FunctionService.roleHashMap.get(roleId).getHp()+"， 当前的蓝量为："+FunctionService.roleHashMap.get(roleId).getMp();
    }

    //技能攻击
    public String useSkillAttack(String skillName,String Target,int roleId){
        String string = null;
        int dura;
        int weaponId =0;
        for (Integer key : FunctionService.roleHashMap.get(roleId).getEquipmentHashMap().keySet()) {
            weaponId = key;
        }
        int key1 = CheckIdByName.checkSkillId(skillName);
        //使用该技能，记录当前时间，set方法传给角色的集合的技能对象的属性，同时判断时间是否合理满足CD
        Instant nowDate = Instant.now();
        Duration between = Duration.between(FunctionService.roleHashMap.get(roleId).getSkillHashMap().get(key1).getStart(), nowDate);
        long l = between.toMillis();
        //FunctionService.role.getSkillHashMap().get(key1)
        if(l>SkillResource.skillStaticHashMap.get(key1).getCd()*Const.GAP_TIME_SKILL) {
            FunctionService.roleHashMap.get(roleId).getSkillHashMap().get(key1).setStart(nowDate);
            //说明技能已经冷却，可以调用该方法，怪物扣血
            int key = CheckIdByName.checkMonsterId(Target,roleId);

            int hp = MonsterResource.monsters.get(key).getMonsterHp();
            int mp=FunctionService.roleHashMap.get(roleId).getMp();
            dura=FunctionService.roleHashMap.get(roleId).getEquipmentHashMap().get(weaponId).getDura();
            System.out.println("武器当前耐久为："+dura);
            //耐久小于等于0或者蓝量不够，退出场景
            if(dura<=0){
                return "武器耐久不够，请先修理再战斗";
            }
            if(mp<SkillResource.skillStaticHashMap.get(key1).getUseMp()){
                FunctionService.roleHashMap.get(roleId).setMp(mp);
                return "角色蓝量不够，请先恢复再战斗";
            }
            hp=hp-FunctionService.roleHashMap.get(roleId).getAtk()-SkillResource.skillStaticHashMap.get(key1).getAtk()-
                    Const.WEAPON_BUFF;
            mp=mp-SkillResource.skillStaticHashMap.get(key1).getUseMp();
            FunctionService.roleHashMap.get(roleId).setMp(mp);
            FunctionService.roleHashMap.get(roleId).getEquipmentHashMap().get(weaponId).setDura(dura-
                    Const.DURA_MINUS);
            if(hp<=0){
                hp=0;
                string = "怪物血量为0，你已经打败该怪物！恭喜获得5000银和一个高级宝箱!获得2点攻击力加成!";
                Listen.monsterIsDead=true;  //对全部客户端进行通知
                MonsterResource.monsters.get(key).setMonsterHp(hp);
                //同时，这里设置怪物的生存状态为0，表示已被消灭
                MonsterResource.monsters.get(key).setAlive(0);
                FunctionService.roleHashMap.get(roleId).setAtk(FunctionService.roleHashMap.get(roleId).getAtk()+Const.ABTAIN_ATK);
                return string;
            }
            System.out.println("玩家使用了"+skillName+"技能，怪物的血量还有"+hp);
            MonsterResource.monsters.get(key).setMonsterHp(hp);
        }else {
            string = "该技能冷却中";
        }
        return string;
    }

    //返回角色的hp，mp，武器耐久，当前攻击力
    public String getRoleInfo(int roleId){
        int weaponId =0;
        for (Integer key : FunctionService.roleHashMap.get(roleId).getEquipmentHashMap().keySet()) {
            weaponId = key;
            System.out.println("come here"+weaponId);
        }
        return "当前角色的hp："+FunctionService.roleHashMap.get(roleId).getHp()+"。 mp："+FunctionService.roleHashMap.get(roleId).getMp()+"。 武器耐久："+
                FunctionService.roleHashMap.get(roleId).getEquipmentHashMap().get(weaponId).getDura()+"。 攻击力："+FunctionService.roleHashMap.get(roleId).getAtk();
    }

    //返回怪物当前状态-存活，非存活
    public String getMonsterInfo(String monsterName,int roleId){
        int key = CheckIdByName.checkMonsterId(monsterName,roleId);
        return "你查询的怪物的hp：" + MonsterResource.monsters.get(key).getMonsterHp() + "，存活状态："+ MonsterResource.monsters.get(key).getAlive();
    }

    //获得一些物品
    public String getBonus(String goods,String amount,int roleId){
        //根据物品找到物品id，根据id放入背包，key对应的num加上amount即可；如果是装备，则不增加
        int number = Integer.valueOf(amount);
        int key = CheckIdByName.checkPotionId(goods);
        if(key!=0){  //药品的情况
            //如果背包中有该key，则数量叠加，如果getkey==null则新增该key，并设置数量
            if(FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().get(key)!=null){
                int allAmount = FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().get(key)+number;
                if(allAmount>=99){  //药品类叠加超过上限，提示最多存99，并设置数量为99
                    FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().put(key,99);
                    System.out.println("你的背包最多只能存放该药物"+99+"瓶");
                }else{
                    FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().put(key,allAmount);
                    System.out.println("获得药品！");
                }
            }else {
                FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().put(key,number);
            }
            return "目前该物品在背包中的数量为"+ FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().get(key);
        }else {  //装备的情况
            //装备类，如果有相同的装备，不操作，如果装备不同，数量+1
            int keyEquipment = CheckIdByName.checkEquipmentId(goods);
            if(FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().get(key)==null){
                FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().put(keyEquipment,1);
                System.out.println("获得装备！");
            }
            return "目前该物品在背包中的数量为"+ FunctionService.roleHashMap.get(roleId).getMyPackage().getGoodsHashMap().get(keyEquipment);
        }
    }

    public String testCode(){

        return "";
    }

    //扩展，毒素和护盾的技能demo，约定技能表中typeId为1时表示毒素类技能，为2时表示护盾类技能；后续可以与前面技能合并；血量不同步是因为用了临时变量
    /*public String useSkill(String skillName,String Target){
        String string = null;
        for (Integer key1 : InitStaticResource.skillStaticHashMap.keySet()) {
            if (skillName.equals(InitStaticResource.skillStaticHashMap.get(key1).getName())) {
                Instant nowDate = Instant.now();
                Duration between = Duration.between(FunctionService.role.getSkillHashMap().get(key1).getStart(), nowDate);
                long l = between.toMillis();
                if(l>InitStaticResource.skillStaticHashMap.get(FunctionService.role.getSkillHashMap().get(key1)).getCd()*Const.TO_MS){
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
    }*/

}


