package com.game.entity.vo;

/**
 * 成就系统
 * @Author andy
 * @create 2020/6/28 7:56
 */
public class AchievementVo {
    //和某个npc对话
    private boolean firstTalkToNpcOleMan;
    //通关某个副本
    private boolean passPartiDungeons;
    //添加一个好友
    private boolean firstAddOneFriend;
    //第一次组队
    private boolean firstJoinTeam;
    //第一次加入公会
    private boolean firstJoinUnion;
    //第一次与玩家交易
    private boolean firstTradeWithPlayer;
    //第一次在pk中战胜
    private boolean firstPkSuccess;

    //击杀特定小怪N只
    private boolean slayPartiMonster;
    //等级提升到N级
    private boolean levelUpToTwenty;
    //获得N件极品装备
    private boolean getNBestEquipment;
    //穿戴的装备等级总和达到XXX
    private boolean sumEquipmentLevelToFourty;
    //当前金币达到xxxx
    private boolean sumMoneyToThousand;
    //完成某一系列任务
    private boolean achievSerialTask;

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
}
