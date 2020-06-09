package com.game.service;

import com.game.common.Const;
import com.game.controller.FunctionService;
import com.game.dao.ConnectSql;
import com.game.entity.*;
import com.game.entity.store.*;
import com.game.service.assis.*;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;


/**
 * 角色一些功能的方法实现
 * @Author andy
 * @create 2020/5/13 18:18
 */

public class RoleService {
    public boolean result;
    //扩展方法-中间变量
    static int time = 1;
    static int t = 0;
    static int monsterHp;
    Role role;
    Equipment equipment;

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
        role = FunctionService.roleHashMap.get(roleId);
        //获得角色当前所在场景的坐标-通过角色的nowSceneId属性获取，在登录注册时已经获得
        String nowPlace = SceneResource.scenesStatics.get(role.getNowScenesId()).getName();
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
            InitGame.scenes.get(role.getNowScenesId()).getRoleAll().remove(role);
            InitGame.scenes.get(Integer.valueOf(temp)).getRoleAll().add(role);
            //移动后角色属性的场景id改变
            role.setNowScenesId(Integer.parseInt(temp));
            //数据库相关操作可以留在用户退出时再调用
            ConnectSql.sql.insertRoleScenes(role.getNowScenesId(),roleId);
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
        for(int j = SceneResource.initSceneId; j< SceneResource.initSceneId+SceneResource.scenesStatics.size(); j++){
            if(SceneResource.scenesStatics.get(j).getName().equals(scenesName)){
                Scene o = InitGame.scenes.get(j);
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
                for(String key: InitGame.scenes.get(j).getMonsterHashMap().keySet()){
                    //if(MonsterResource.monsters.get(key).getAlive()==1){ todo
                    if(InitGame.scenes.get(AssistService.checkSceneId(scenesName)).getMonsterHashMap().get(key).getAlive()==1){
                        stringBuilder.append(MonsterResource.monstersStatics.get(InitGame.scenes.get(AssistService.checkSceneId(scenesName)).getMonsterHashMap().get(key).getMonsterId()).getName()).append(" ");
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
        int key = AssistService.checkNpcId(npcName,roleId);
        replyWords = NpcResource.npcsStatics.get(key).getWords();
        return replyWords;
    }

    //修理装备
    public String repairEquipment(String equipmentName,int roleId){
        String result = "没有该武器";
        int key = AssistService.checkEquipmentId(equipmentName);
        equipment = FunctionService.roleHashMap.get(roleId).getEquipmentHashMap().get(key);
        equipment.setDura(EquipmentResource.equipmentStaticHashMap.get(key).getDurability());
        result = "修理成功！当前武器耐久为："+equipment.getDura();
        return result;
    }

    //穿戴装备-从背包里拿出来穿戴
    public String putOnEquipment(String equipment,int roleId){
        role = FunctionService.roleHashMap.get(roleId);
        int atk = role.getAtk();
        int key = AssistService.checkEquipmentId(equipment);
        atk = atk + EquipmentResource.equipmentStaticHashMap.get(key).getAtk();
        role.setAtk(atk);
        Equipment equipment1 = new Equipment(key,EquipmentResource.equipmentStaticHashMap.get(key).getDurability());
        role.getEquipmentHashMap().put(key,equipment1);
        role.getMyPackage().getGoodsHashMap().remove(key);
        System.out.println("装备了");
        return "你已成功装备该武器，目前攻击力为："+role.getAtk();
    }

    //卸下装备
    public String takeOffEquipment(String equipment,int roleId){
        role = FunctionService.roleHashMap.get(roleId);
        int atk = role.getAtk();
        int key = AssistService.checkEquipmentId(equipment);
        atk = atk - EquipmentResource.equipmentStaticHashMap.get(key).getAtk();
        role.setAtk(atk);
        role.getEquipmentHashMap().remove(key);
        Equipment equipment1 = new Equipment(key,EquipmentResource.equipmentStaticHashMap.get(key).getDurability());
        role.getMyPackage().getGoodsHashMap().put(key,1);
        return "你已成功卸下该武器，目前攻击力为："+role.getAtk();
    }

    //使用药品
    public String useDrug(String drugName,int roleId){
        role = FunctionService.roleHashMap.get(roleId);
        int hp = role.getHp();
        int mp = role.getMp();
        int key = AssistService.checkPotionId(drugName);
        System.out.println("使用药品前，背包中还有此药品数量为："+role.getMyPackage().getGoodsHashMap().get(key));
        if(role.getMyPackage().getGoodsHashMap().get(key)<=0){
            return "此药品已经没有了！";
        }else {
            role.getMyPackage().getGoodsHashMap().put(key,role.getMyPackage().getGoodsHashMap().get(key)-1);
        }
        System.out.println("使用药品后，背包中还有此药品数量为："+role.getMyPackage().getGoodsHashMap().get(key));
        hp = hp + PotionResource.potionStaticHashMap.get(key).getAddHp();
        mp = mp + PotionResource.potionStaticHashMap.get(key).getAddMp();
        role.setHp(hp);
        role.setMp(mp);
        if(role.getHp()>=CareerResource.careerStaticHashMap.get(Const.CAREER_ID).getHp()){
                //RoleResource.roleStaticHashMap.get(Const.TYPE_ID).getLevelHp()){
            role.setHp(CareerResource.careerStaticHashMap.get(Const.CAREER_ID).getHp());
        }
        if(role.getMp()>=CareerResource.careerStaticHashMap.get(Const.CAREER_ID).getMp()){
            role.setMp(CareerResource.careerStaticHashMap.get(Const.CAREER_ID).getMp());
        }
        return "使用药品成功，你的当前血量为："+role.getHp()+"， 当前的蓝量为："+role.getMp();
    }

    //技能攻击
    public String useSkillAttack(String skillName,String monsterId,int roleId){
        role = FunctionService.roleHashMap.get(roleId);
        String string = null;
        int dura;
        int weaponId =0;
        for (Integer temp : role.getEquipmentHashMap().keySet()) {
            weaponId = temp;
        }
        int key1 = AssistService.checkSkillId(skillName);
        //使用该技能，记录当前时间，set方法传给角色的集合的技能对象的属性，同时判断时间是否合理满足CD
        Instant nowDate = Instant.now();
        Duration between = Duration.between(FunctionService.roleHashMap.get(roleId).getSkillHashMap().get(key1).getStart(), nowDate);
        long l = between.toMillis();
        if(l>SkillResource.skillStaticHashMap.get(key1).getCd()*Const.GAP_TIME_SKILL) {
            role.getSkillHashMap().get(key1).setStart(nowDate);
            //说明技能已经冷却，可以调用该方法，怪物扣血
            Monster nowMonster = InitGame.scenes.get(role.getNowScenesId()).getMonsterHashMap().get(monsterId);
            int hp = nowMonster.getMonsterHp();
            int mp=role.getMp();
            dura=role.getEquipmentHashMap().get(weaponId).getDura();
            //耐久小于等于0或者蓝量不够，退出场景
            if(dura<=0){
                return "武器耐久不够，请先修理再战斗";
            }
            if(mp<SkillResource.skillStaticHashMap.get(key1).getUseMp()){
                role.setMp(mp);
                return "角色蓝量不够，请先恢复再战斗";
            }
            hp=hp-role.getAtk()-SkillResource.skillStaticHashMap.get(key1).getAtk()-
                    Const.WEAPON_BUFF;
            mp=mp-SkillResource.skillStaticHashMap.get(key1).getUseMp();
            role.setMp(mp);
            role.getEquipmentHashMap().get(weaponId).setDura(dura-
                    Const.DURA_MINUS);
            if(hp<=0){
                hp=0;
                string = "怪物血量为0，你已经打败该怪物！恭喜获得5000银和一个高级宝箱!获得2点攻击力加成!";
                Listen.monsterIsDead=true;  //对全部客户端进行通知
                nowMonster.setMonsterHp(hp);
                //同时，这里设置怪物的生存状态为0，表示已被消灭
                nowMonster.setAlive(0);
                role.setAtk(role.getAtk()+Const.ABTAIN_ATK);
                return string;
            }
            System.out.println("玩家使用了"+skillName+"技能，怪物的血量还有"+hp);
            nowMonster.setMonsterHp(hp);
        }else {
            string = "该技能冷却中";
        }
        return string;
    }

    //选择：任意同场景可以pk玩家 todo 可选，仅限竞技场pk玩家
    //假设可随意使用技能攻击String skillName,String monsterId,int roleId
    public String pkPlayer (String skillName,int TargetRoleId, int roleId){
        role = FunctionService.roleHashMap.get(roleId);
        String string = "你对玩家发起了pk，对其进行攻击";
        int dura;
        int weaponId =0;
        for (Integer temp : role.getEquipmentHashMap().keySet()) {
            weaponId = temp;
        }
        int key1 = AssistService.checkSkillId(skillName);
        //使用该技能，记录当前时间，set方法传给角色的集合的技能对象的属性，同时判断时间是否合理满足CD
        Instant nowDate = Instant.now();
        Duration between = Duration.between(FunctionService.roleHashMap.get(roleId).getSkillHashMap().get(key1).getStart(), nowDate);
        long l = between.toMillis();
        if(l>SkillResource.skillStaticHashMap.get(key1).getCd()*Const.GAP_TIME_SKILL) {
            role.getSkillHashMap().get(key1).setStart(nowDate);
            //说明技能已经冷却，可以调用该方法，对方扣血
            Role enemy = FunctionService.roleHashMap.get(TargetRoleId);
                    //InitGame.scenes.get(role.getNowScenesId()).getRoleAll().get(TargetRoleId);
            int hp = enemy.getHp();
            int mp=role.getMp();
            dura=role.getEquipmentHashMap().get(weaponId).getDura();
            //耐久小于等于0或者蓝量不够，退出场景
            if(dura<=0){
                return "武器耐久不够，请先修理再战斗";
            }
            if(mp<SkillResource.skillStaticHashMap.get(key1).getUseMp()){
                role.setMp(mp);
                return "角色蓝量不够，请先恢复再战斗";
            }
            hp=hp-role.getAtk()-SkillResource.skillStaticHashMap.get(key1).getAtk()-
                    Const.WEAPON_BUFF;
            mp=mp-SkillResource.skillStaticHashMap.get(key1).getUseMp();
            role.setMp(mp);
            role.getEquipmentHashMap().get(weaponId).setDura(dura-
                    Const.DURA_MINUS);
            if(hp<=0){
                hp=0;
                string = "对方血量为0，你已经pk掉对方！恭喜获得对方50银，获得2点攻击力加成!";
                //Listen.monsterIsDead=true;  //对全部客户端进行通知
                enemy.setHp(hp);
                //同时，这里设置怪物的生存状态为0，表示已被消灭
                //enemy.setAlive(0);
                role.setAtk(role.getAtk()+Const.ABTAIN_ATK);
                return string;
            }
            System.out.println("玩家使用了"+skillName+"技能，对方的血量还有"+hp);
            enemy.setHp(hp);
        }else {
            string = "该技能冷却中";
        }
        return string;
    }

    //返回角色的hp，mp，武器耐久，当前攻击力
    public String getRoleInfo(int roleId){
        role = FunctionService.roleHashMap.get(roleId);
        int weaponId =0;
        for (Integer key : role.getEquipmentHashMap().keySet()) {
            weaponId = key;
            System.out.println("come here"+weaponId);
        }
        return "当前角色的hp："+role.getHp()+"。 mp："+role.getMp()+"。 武器耐久："+
                role.getEquipmentHashMap().get(weaponId).getDura()+"。 攻击力："+role.getAtk()+"。 金钱："+role.getMoney();
    }

    //返回怪物当前状态-存活，非存活
    public String getMonsterInfo(String monsterName,int roleId){
        String key = AssistService.checkMonsterId(monsterName,roleId);// todo
        Monster nowMonster = InitGame.scenes.get(FunctionService.roleHashMap.get(roleId).getNowScenesId()).getMonsterHashMap().get(key);
        return "你查询的怪物的hp：" + nowMonster.getMonsterHp() + "，存活状态："+ nowMonster.getAlive();
    }

    //获得一些物品 todo 优化条件语句；建议药品查询与装备查询放在一起
    //购买物品，计算金钱，提示金钱不够or购买成功；角色金钱>物品价格*数量
    public String buyGoods(String goods,String amount,int roleId){
        role = FunctionService.roleHashMap.get(roleId);
        //根据物品找到物品id，根据id放入背包，key对应的num加上amount即可；如果是装备，则不增加
        int number = Integer.parseInt(amount);
        //该商品可能是药品，也可能是装备
        int key = AssistService.checkGoodsId(goods);
        if(String.valueOf(key).startsWith("2")){  //药品的情况
            //先计算是否买得起，买不起直接提示
            int cost = PotionResource.potionStaticHashMap.get(key).getPrice()*number;
            if(role.getMoney()<cost){
                return "您所携带的银两不够，无法够买";
            }else {
                //够买成功，角色金钱变少
                role.setMoney(role.getMoney()-cost);
            }
            //如果背包中有该key，则数量叠加，如果getkey==null则新增该key，并设置数量
            if(role.getMyPackage().getGoodsHashMap().get(key)!=null){
                int allAmount = role.getMyPackage().getGoodsHashMap().get(key)+number;
                if(allAmount>=99){  //药品类叠加超过上限，提示最多存99，并设置数量为99
                    role.getMyPackage().getGoodsHashMap().put(key,99);
                    System.out.println("你的背包最多只能存放该药物"+99+"瓶");
                }else{
                    role.getMyPackage().getGoodsHashMap().put(key,allAmount);
                    System.out.println("获得药品！");
                }
            }else {  //没有该物品，直接放入背包
                role.getMyPackage().getGoodsHashMap().put(key,number);
            }
            return "够买成功，目前该药品在背包中的数量为"+ role.getMyPackage().getGoodsHashMap().get(key);
        }else {  //装备的情况
            //先计算是否买得起，买不起直接提示
            int cost = EquipmentResource.equipmentStaticHashMap.get(key).getPrice()*number;
            if(role.getMoney()<cost){
                return "您所携带的银两不够，无法够买";
            }else {
                //够买成功，角色金钱变少
                role.setMoney(role.getMoney()-cost);
            }
            //装备类，如果有相同的装备，不操作，如果装备不同，数量+1
            int keyEquipment = AssistService.checkEquipmentId(goods);
            if(role.getMyPackage().getGoodsHashMap().get(key)==null){
                role.getMyPackage().getGoodsHashMap().put(keyEquipment,1);
                System.out.println("获得装备！");
            }
            return "够买成功，目前该装备在背包中的数量为"+ role.getMyPackage().getGoodsHashMap().get(keyEquipment);
        }
    }

    //获得所有商品列表，并附上价格，这里遍历药品和装备的静态列表即可，商店物品可用字符串存起来
    public String getGoodsList(){
        return InitGame.goodsList;
    }

    //创建队伍，返回一个队伍id，并将该id加入到全局的teamList中
    public String createTeam(int dungeonesId, int roleId){
        String teamId = "0000"+roleId;
        Team team = new Team(teamId,dungeonesId);
        DynamicResource.teamList.put(teamId,team);
        //房主自动加入队伍中
        DynamicResource.teamList.get(teamId).getRoleList().add(roleId);
        return "你已经创建了队伍，队伍id为："+teamId+"，快告诉你的小伙伴加入吧";
    }

    //加入队伍，角色放入队伍集合中，返回队伍中的角色列表 todo 待扩展，测试队员掉线时的变化，分为房主掉线和成员掉线
    public String joinTeam(String teamId, int roleId){
        DynamicResource.teamList.get(teamId).getRoleList().add(roleId);
        return "你已经加入了队伍，当前队伍列表中角色有："+AssistService.getRoleList(teamId);
    }

    //难度最大的方法，其中还涉及了其他比较多的方法，这些方法需要提取到外部，统一化管理
    //开始副本，玩家一起攻打BOSS  todo 待扩展，测试队员掉线时的变化，分为房主掉线和成员掉线；掉线时队伍的状态管理和副本的状态管理；是否要考虑重连的情况？
    // 是否要考虑多线程？不同角色（线程）如何对同一个值进行修改操作
    public String startDungeons (String teamId, int roleId){//这里直接放入团队的id？或者不放
        if(!teamId.equals("0000"+roleId)){return "只有房主才有开启副本的权限";}
        //创建场景，角色加入该场景，怪物加入该场景
        //角色调用move方法，传送到副本场景中
        Team team = DynamicResource.teamList.get(teamId);
        for(int i=0;i<team.getRoleList().size();i++){
            //move(MonsterResource.monstersStatics.get(DungeonsResource.dungeonsStaticHashMap.get(team.getDungeonsId()).getBossId()).getName(),team.getRoleList().get(i));
            move(SceneResource.scenesStatics.get(DungeonsResource.dungeonsStaticHashMap.get(team.getDungeonsId()).getSceneId()).getName(),team.getRoleList().get(i));
        }

        //调用boss定时攻击角色的方法
        bossAttackRole();

        //期间角色可以对boss进行攻击

        //发现成员掉线了，分为队长和普通成员两种情况。成员掉线，场景队长切换为队员，同时队伍的角色列表中去除名字；队员掉线直接在列表中去除名字，


        //角色可以使用自己的技能攻击BOSS，skill命令，todo 该命令需要重构并扩展
        //调用skill方法，传入参数包含roleId，通过循环遍历队伍中的角色获取


        //BOSS自动攻击角色，选取目标，优先选择宝宝，其次选择战士，其他随机；BOSS被嘲讽技能命中时，优先选择释放命令的角色
        String careerName = "";
        //伪代码-后续扩展可使用状态设计模式
/*        if(careerName.equals("被嘲讽")){

        }else if(careerName.equals("宝宝")){

        }else if(careerName.equals("战士")){

        }else {
            //剩余角色中随机选取
        }*/
        //角色被击杀，或规定时间内未完成任务

        //BOSS被打败，团队每人都获得1-2件装备，500-800银两，每个人一样多

        //退出时，回收该地图场景

        return "副本已开启，尽情攻打BOSS，获得更多奖励吧";
    }

    //全服聊天
    public String sayToAllPlayer (String words, int roleId){
        //当前全局通信的基础上进行修改

        return "你已发送消息xx给大家";
    }

    //私人聊天
    public String sayToOnePlayer (int TargetRoleId, String words, int roleId){

        return "你已发送消息xx给对方";
    }

    //发送邮件
    public String emailToPlayer (int TargetRoleId, String words, String goods, int roleId){

        //邮寄的话，同上

        //邮寄的物品，对方背包中追加该物品，自己背包中减少该物品

        return "你已发送邮件xx给对方";
    }

    //选择职业，放到前面注册角色后
    public String chooseCareer (String careerName, int roleId){

        return "你选择了xx职业";
    }

    //测试用命令
    public String testCode(){
        return AssistService.getTeamIdList()+InitGame.dungeonsList;
    }

    //盾类和蓝药缓慢恢复技能6.9版本

    //boss定时使用技能攻击角色方法
    public static void bossAttackRole(){
        Timer timer = new Timer();
        BossAttack bossAttack = new BossAttack(timer);
        timer.schedule(bossAttack, Const.DELAY_TIME, Const.GAP_TIME_POTION);

    }
}


