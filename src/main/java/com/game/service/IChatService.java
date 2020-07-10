package com.game.service;

/**
 * @Author andy
 * @create 2020/7/7 10:41
 */
public interface IChatService {

    /**
     * 向目标发送邮件
     * @param TargetRoleId 目标角色id
     * @param words 留言
     * @param goods 物品名
     * @param num 物品数量
     * @param roleId 角色id
     * @return String
     */
    String emailToPlayer (int TargetRoleId, String words, String goods, int num, int roleId);

}
