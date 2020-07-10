package com.game.service;

import com.game.entity.Role;

/**
 * @Author andy
 * @create 2020/7/7 10:41
 */
public interface IAchievementService {
    /**  */
    void countKilledMonster(int monsterId, Role role);
    /**  */
    void countLevel(Role role);
    /**  */
    void talkToNpc(int npcId,Role role);
    /**  */
    void countBestEquipment(int equipmentId,Role role);
    /**  */
    void ifPassPartiDungeons(int dungeonsId,Role role);
    /**  */
    void countSumWearLevel(Role role);
    /**  */
    void countAddFriend(int friendId,Role role);
    /**  */
    void ifFirstJoinTeam(Role role);
    /**  */
    void ifFirstJoinUnion(Role role);
    /**  */
    void ifFirstTradeWithPlayer(Role role);
    /**  */
    void ifFirstPkSuccess(Role role);
    /**  */
    void ifSumMoneyToThousand(int number,Role role);
    /**  */
    void ifAchievSerialTask(Role role);
    /**  */
    String getAchievmentList(Role role);

}
