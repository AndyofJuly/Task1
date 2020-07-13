package com.game.system.role.pojo;

import com.game.common.Const;
import com.game.system.bag.pojo.Equipment;
import com.game.system.shop.pojo.DealBo;
import com.game.system.shop.pojo.PlayerSaleBo;
import com.game.system.skill.pojo.Skill;
import com.game.system.achievement.pojo.AchievementBo;
import com.game.system.bag.pojo.MyPackageBo;
import com.game.system.scene.pojo.GridBo;

import java.util.HashMap;

/**
 * 角色类
 * @Author andy
 * @create 2020/5/11 14:58
 */
public class Role {
    /** 角色id */
    private int id;
    /** 角色名 */
    private String name;
    /** 角色所在场景 */
    private int nowScenesId;
    /** 角色存活状态 */
    private int alive;
    /** 角色包裹 */
    private MyPackageBo myPackageBo;
    /** 角色职业id */
    private int careerId;
    /** 角色血量 */
    private int hp;
    /** 角色蓝量 */
    private int mp;
    /** 角色攻击力 */
    private int atk;
    /** 角色的金钱 */
    private int money;
    /** 角色防御力 */
    private int def;
    /** 是否使用嘲讽技能 */
    private boolean useTaunt = false;
    /** 召唤师特有属性-宝宝-怪物类 */
    private Baby baby;
    /** 横纵坐标，数组下标0为横坐标，下标1为纵坐标 */
    private int[] position;
    /** 角色所在的网格id */
    private int curGridId;
    /** 角色面对面交易单 */
    private DealBo dealBo;
    /** 角色交易行 */
    private PlayerSaleBo playerSaleBo;
    /** 角色视野实体集合 */
    private GridBo gridBo;
    /** 角色成就-任务系统 */
    private final AchievementBo achievementBo = new AchievementBo();
    /** 角色的公会 */
    private int unionId;
    /** 朋友 */
    private final FriendBo friendBo = new FriendBo();
    /** 等级 */
    private int level;
    /** 角色学会的技能 */
    private final HashMap<Integer, Skill> skillHashMap = new HashMap<>();
    /** 角色装备栏 */
    private final HashMap<Integer, Equipment> equipmentHashMap = new HashMap<>();

    public Role(int id) {
        this.id = id;
        this.alive = 1;
        this.level = 1;
        this.position= new int[]{50, 20};
        this.curGridId = 20;
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

    public HashMap<Integer, Equipment> getEquipmentHashMap() {
        return equipmentHashMap;
    }

    public MyPackageBo getMyPackageBo() {
        return myPackageBo;
    }

    public void setMyPackageBo(MyPackageBo myPackageBo) {
        this.myPackageBo = myPackageBo;
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

    public GridBo getGridBo() {
        return gridBo;
    }

    public void setGridBo(GridBo gridBo) {
        this.gridBo = gridBo;
    }

    public DealBo getDealBo() {
        return dealBo;
    }

    public void setDealBo(DealBo dealBo) {
        this.dealBo = dealBo;
    }

    public PlayerSaleBo getPlayerSaleBo() {
        return playerSaleBo;
    }

    public void setPlayerSaleBo(PlayerSaleBo playerSaleBo) {
        this.playerSaleBo = playerSaleBo;
    }

    public AchievementBo getAchievementBo() {
        return achievementBo;
    }

    public int getUnionId() {
        return unionId;
    }

    public void setUnionId(int unionId) {
        this.unionId = unionId;
    }

    public FriendBo getFriendBo() {
        return friendBo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
