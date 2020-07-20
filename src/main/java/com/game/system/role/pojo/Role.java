package com.game.system.role.pojo;

import com.game.common.Const;
import com.game.system.bag.pojo.Equipment;
import com.game.system.scene.pojo.ViewGridBo;
import com.game.system.shop.pojo.DealBo;
import com.game.system.shop.pojo.AuctionBo;
import com.game.system.skill.pojo.Skill;
import com.game.system.achievement.pojo.AchievementBo;
import com.game.system.bag.pojo.MyPackageBo;

import java.util.HashMap;

/**
 * 角色类
 * @Author andy
 * @create 2020/5/11 14:58
 */
public class Role {
    /** 角色id */
    private Integer id;
    /** 角色名 */
    private String name;
    /** 角色所在场景 */
    private Integer nowScenesId;
    /** 角色存活状态 */
    private Integer alive;
    /** 角色包裹 */
    private MyPackageBo myPackageBo;
    /** 角色职业id */
    private Integer careerId;
    /** 角色血量 */
    private Integer hp;
    /** 角色蓝量 */
    private Integer mp;
    /** 角色攻击力 */
    private Integer atk;
    /** 角色的金钱 */
    private Integer money;
    /** 角色防御力 */
    private Integer def;
    /** 是否使用嘲讽技能 */
    private boolean useTaunt = false;
    /** 召唤师特有属性-宝宝-怪物类 */
    private Baby baby;
    /** 横纵坐标，数组下标0为横坐标，下标1为纵坐标 */
    private Integer[] position;
    /** 角色所在的网格id */
    private Integer curGridId;
    /** 角色面对面交易单 */
    private DealBo dealBo;
    /** 角色交易行 */
    private AuctionBo auctionBo;
    /** 角色视野实体集合 */
    private ViewGridBo viewGridBo;
    /** 角色成就-任务系统 */
    private final AchievementBo achievementBo = new AchievementBo();
    /** 角色的公会 */
    private Integer unionId;
    /** 朋友 */
    private final FriendBo friendBo = new FriendBo();
    /** 等级 */
    private Integer level;
    /** 角色学会的技能 */
    private final HashMap<Integer, Skill> skillHashMap = new HashMap<>();
    /** 角色装备栏-槽位0至五，对应value为武器id */
    private final HashMap<Integer, Integer> equipmentHashMap = new HashMap<>();//id可以改为0-5，对应装备槽
    /** 物品锁定-交易时 */
    private HashMap<Integer,Integer> goodsLockMap = new HashMap<>();
    /** 当前最大血量 */
    private Integer maxHp;
    /** 当前最大蓝量 */
    private Integer maxMp;
    /** 所在队伍id */
    private String teamId;

    public Role(Integer id) {
        this.id = id;
        this.alive = 1;
        this.level = 1;
        this.position= new Integer[]{50, 20};
        this.curGridId = 20;
        this.nowScenesId = Const.INIT_SCENE;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNowScenesId() {
        return nowScenesId;
    }

    public void setNowScenesId(Integer nowScenesId) {
        this.nowScenesId = nowScenesId;
    }

    public Integer getAlive() {
        return alive;
    }

    public void setAlive(Integer alive) {
        this.alive = alive;
    }

    public MyPackageBo getMyPackageBo() {
        return myPackageBo;
    }

    public void setMyPackageBo(MyPackageBo myPackageBo) {
        this.myPackageBo = myPackageBo;
    }

    public Integer getCareerId() {
        return careerId;
    }

    public void setCareerId(Integer careerId) {
        this.careerId = careerId;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getMp() {
        return mp;
    }

    public void setMp(Integer mp) {
        this.mp = mp;
    }

    public Integer getAtk() {
        return atk;
    }

    public void setAtk(Integer atk) {
        this.atk = atk;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getDef() {
        return def;
    }

    public void setDef(Integer def) {
        this.def = def;
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

    public Integer[] getPosition() {
        return position;
    }

    public void setPosition(Integer[] position) {
        this.position = position;
    }

    public Integer getCurGridId() {
        return curGridId;
    }

    public void setCurGridId(Integer curGridId) {
        this.curGridId = curGridId;
    }

    public DealBo getDealBo() {
        return dealBo;
    }

    public void setDealBo(DealBo dealBo) {
        this.dealBo = dealBo;
    }

    public AuctionBo getAuctionBo() {
        return auctionBo;
    }

    public void setAuctionBo(AuctionBo auctionBo) {
        this.auctionBo = auctionBo;
    }

    public ViewGridBo getViewGridBo() {
        return viewGridBo;
    }

    public void setViewGridBo(ViewGridBo viewGridBo) {
        this.viewGridBo = viewGridBo;
    }

    public AchievementBo getAchievementBo() {
        return achievementBo;
    }

    public Integer getUnionId() {
        return unionId;
    }

    public void setUnionId(Integer unionId) {
        this.unionId = unionId;
    }

    public FriendBo getFriendBo() {
        return friendBo;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public HashMap<Integer, Skill> getSkillHashMap() {
        return skillHashMap;
    }

/*    public HashMap<Integer, Equipment> getEquipmentHashMap() {//建议枚举，描述与槽位相对应
        return equipmentHashMap;
    }*/

    public HashMap<Integer, Integer> getEquipmentHashMap() {
        return equipmentHashMap;
    }

    public HashMap<Integer, Integer> getGoodsLockMap() {
        return goodsLockMap;
    }

    public void setGoodsLockMap(HashMap<Integer, Integer> goodsLockMap) {
        this.goodsLockMap = goodsLockMap;
    }

    public Integer getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(Integer maxHp) {
        this.maxHp = maxHp;
    }

    public Integer getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(Integer maxMp) {
        this.maxMp = maxMp;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
