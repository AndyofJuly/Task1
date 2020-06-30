package com.game.entity.vo;

/**
 * 成就系统
 * @Author andy
 * @create 2020/6/28 7:56
 */
public class AchievementVo {
    //和某个npc对话
    private boolean firstTalkToNpcOleMan = false;
    //通关某个副本
    private boolean passPartiDungeons = false;
    //添加一个好友？目前还没有好友系统，添加好友的方法，需要增加
    private boolean firstAddOneFriend = false;
    //第一次组队
    private boolean firstJoinTeam = false;
    //第一次加入公会？目前还没有加入公会的方法
    private boolean firstJoinUnion = false;
    //第一次与玩家交易
    private boolean firstTradeWithPlayer = false;
    //第一次在pk中战胜
    private boolean firstPkSuccess = false;

    //击杀特定小怪N只
    private boolean slayPartiMonster = false;
    //等级提升到N级？目前还没有升级的相关方法
    private boolean levelUpToTwenty = false;
    //获得N件极品装备？目前还没有对装备进行分类
    private boolean getNBestEquipment = false;
    //穿戴的装备等级总和达到XXX？还没有定制装备的等级
    private boolean sumEquipmentLevelToFourty = false;
    //当前金币达到xxxx
    private boolean sumMoneyToThousand = false;
    //完成某一系列任务
    private boolean achievSerialTask = false;

    //计数并判断是否满足数量要求，满足则设为true;
    private int slayConut = 0;

    public boolean isFirstTalkToNpcOleMan() {
        return firstTalkToNpcOleMan;
    }

    public void setFirstTalkToNpcOleMan(boolean firstTalkToNpcOleMan) {
        this.firstTalkToNpcOleMan = firstTalkToNpcOleMan;
    }

    public boolean isPassPartiDungeons() {
        return passPartiDungeons;
    }

    public void setPassPartiDungeons(boolean passPartiDungeons) {
        this.passPartiDungeons = passPartiDungeons;
    }

    public boolean isFirstAddOneFriend() {
        return firstAddOneFriend;
    }

    public void setFirstAddOneFriend(boolean firstAddOneFriend) {
        this.firstAddOneFriend = firstAddOneFriend;
    }

    public boolean isFirstJoinTeam() {
        return firstJoinTeam;
    }

    public void setFirstJoinTeam(boolean firstJoinTeam) {
        this.firstJoinTeam = firstJoinTeam;
    }

    public boolean isFirstJoinUnion() {
        return firstJoinUnion;
    }

    public void setFirstJoinUnion(boolean firstJoinUnion) {
        this.firstJoinUnion = firstJoinUnion;
    }

    public boolean isFirstTradeWithPlayer() {
        return firstTradeWithPlayer;
    }

    public void setFirstTradeWithPlayer(boolean firstTradeWithPlayer) {
        this.firstTradeWithPlayer = firstTradeWithPlayer;
    }

    public boolean isFirstPkSuccess() {
        return firstPkSuccess;
    }

    public void setFirstPkSuccess(boolean firstPkSuccess) {
        this.firstPkSuccess = firstPkSuccess;
    }

    public boolean isSlayPartiMonster() {
        return slayPartiMonster;
    }

    public void setSlayPartiMonster(boolean slayPartiMonster) {
        this.slayPartiMonster = slayPartiMonster;
    }

    public boolean isLevelUpToTwenty() {
        return levelUpToTwenty;
    }

    public void setLevelUpToTwenty(boolean levelUpToTwenty) {
        this.levelUpToTwenty = levelUpToTwenty;
    }

    public boolean isGetNBestEquipment() {
        return getNBestEquipment;
    }

    public void setGetNBestEquipment(boolean getNBestEquipment) {
        this.getNBestEquipment = getNBestEquipment;
    }

    public boolean isSumEquipmentLevelToFourty() {
        return sumEquipmentLevelToFourty;
    }

    public void setSumEquipmentLevelToFourty(boolean sumEquipmentLevelToFourty) {
        this.sumEquipmentLevelToFourty = sumEquipmentLevelToFourty;
    }

    public boolean isSumMoneyToThousand() {
        return sumMoneyToThousand;
    }

    public void setSumMoneyToThousand(boolean sumMoneyToThousand) {
        this.sumMoneyToThousand = sumMoneyToThousand;
    }

    public boolean isAchievSerialTask() {
        return achievSerialTask;
    }

    public void setAchievSerialTask(boolean achievSerialTask) {
        this.achievSerialTask = achievSerialTask;
    }

    //计数
    public int getSlayConut() {
        return slayConut;
    }

    public void setSlayConut(int slayConut) {
        this.slayConut = this.slayConut+slayConut;
    }

}
