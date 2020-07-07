package com.game.entity;

import com.game.common.Const;
import com.game.entity.vo.*;

import java.util.HashMap;

/**
 * 角色类
 * @Author andy
 * @create 2020/5/11 14:58
 */
public class Role {
    // 角色id
    private int id;
    // 角色名
    private String name;
    // 角色所在场景
    private int nowScenesId;
    // 角色存活状态
    private int alive;
    // 角色包裹
    private MyPackage myPackage;
    // 角色职业id
    private int careerId;
    //角色血量
    private int hp;
    //角色蓝量
    private int mp;
    //角色攻击力
    private int atk;
    //角色的金钱，初始送100
    private int money;
    //角色防御力
    private int def;
    //是否使用嘲讽技能
    private boolean useTaunt = false;
    //召唤师特有属性-宝宝-怪物类
    private Baby baby;
    //横纵坐标，数组下标0为横坐标，下标1为纵坐标
    private int[] position = {50,20};//new int[2]计算出的网格id为20
    //角色所在的网格id
    private int curGridId = 20;
    //角色面对面交易单
    private DealVo dealVo;
    //角色交易行
    private PlayerSaleVo playerSaleVo;
    //角色视野实体集合，角色id和GridVo
//    private HashMap<Integer, GridVo> gridVoHashMap = new HashMap<>();
    private GridVo gridVo;
    //角色成就-任务系统
    private AchievementVo achievementVo = new AchievementVo();
    //角色的公会
    private int unionId;
    //朋友
    private FriendVo friendVo = new FriendVo();
    //等级
    private int level;

    //穿戴装备
    //private WearEquipment wearEquipment;

    //角色学会的技能
    private HashMap<Integer,Skill> skillHashMap = new HashMap<Integer,Skill>();
    //如果后续装备类型变多了，还是需要集合
    //角色装备栏
    private HashMap<Integer,Equipment> equipmentHashMap = new HashMap<Integer,Equipment>();
    //private BodyEquipVo bodyEquipVo;
    //药品应该可以不需要集合，直接从背包里拿，然后使用

/*    public Role(int id, String name, int nowScenesId,int hp,int mp,int atk,int money,int def,int level) {
        this.id = id;
        this.name = name;
        this.nowScenesId = nowScenesId;
        this.alive = 1;
    }*/

    public Role(int id) {
        this.id = id;
        this.alive = 1;
        this.level = 1;
        this.nowScenesId = Const.INIT_SCENE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNowScenesId() {
        return nowScenesId;
    }

    public void setNowScenesId(int nowScenesId) {
        this.nowScenesId = nowScenesId;
    }

    public int getAlive() {
        return alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public HashMap<Integer, Skill> getSkillHashMap() {
        return skillHashMap;
    }

    public void setSkillHashMap(HashMap<Integer, Skill> skillHashMap) {
        this.skillHashMap = skillHashMap;
    }

    public HashMap<Integer, Equipment> getEquipmentHashMap() {
        return equipmentHashMap;
    }

    public void setEquipmentHashMap(HashMap<Integer, Equipment> equipmentHashMap) {
        this.equipmentHashMap = equipmentHashMap;
    }

    public MyPackage getMyPackage() {
        return myPackage;
    }

    public void setMyPackage(MyPackage myPackage) {
        this.myPackage = myPackage;
    }

    public int getCareerId() {
        return careerId;
    }

    public void setCareerId(int careerId) {
        this.careerId = careerId;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isUseTaunt() {
        return useTaunt;
    }

    public void setUseTaunt(boolean useTaunt) {
        this.useTaunt = useTaunt;
    }

    public Baby getBaby() {
        return baby;
    }

    public void setBaby(Baby baby) {
        this.baby = baby;
    }

    public int getCurGridId() {
        return curGridId;
    }

    public void setCurGridId(int curGridId) {
        this.curGridId = curGridId;
    }

/*    public HashMap<Integer, GridVo> getGridVoHashMap() {
        return gridVoHashMap;
    }

    public void setGridVoHashMap(HashMap<Integer, GridVo> gridVoHashMap) {
        this.gridVoHashMap = gridVoHashMap;
    }*/

    public GridVo getGridVo() {
        return gridVo;
    }

    public void setGridVo(GridVo gridVo) {
        this.gridVo = gridVo;
    }

    public DealVo getDealVo() {
        return dealVo;
    }

    public void setDealVo(DealVo dealVo) {
        this.dealVo = dealVo;
    }

    public PlayerSaleVo getPlayerSaleVo() {
        return playerSaleVo;
    }

    public void setPlayerSaleVo(PlayerSaleVo playerSaleVo) {
        this.playerSaleVo = playerSaleVo;
    }

    public AchievementVo getAchievementVo() {
        return achievementVo;
    }

    public void setAchievementVo(AchievementVo achievementVo) {
        this.achievementVo = achievementVo;
    }

    public int getUnionId() {
        return unionId;
    }

    public void setUnionId(int unionId) {
        this.unionId = unionId;
    }

    public FriendVo getFriendVo() {
        return friendVo;
    }

    public void setFriendVo(FriendVo friendVo) {
        this.friendVo = friendVo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

/*    public WearEquipment getWearEquipment() {
        return wearEquipment;
    }

    public void setWearEquipment(WearEquipment wearEquipment) {
        this.wearEquipment = wearEquipment;
    }*/

/*    public BodyEquipVo getBodyEquipVo() {
        return bodyEquipVo;
    }

    public void setBodyEquipVo(BodyEquipVo bodyEquipVo) {
        this.bodyEquipVo = bodyEquipVo;
    }*/
}
