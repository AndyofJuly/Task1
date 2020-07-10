package com.game.service;

/**
 * @Author andy
 * @create 2020/7/7 14:54
 */
public interface ISceneService {
    /**
     * 创建临时场景
     * @param dungeonsId 副本id
     * @return int
     */
    int createTempScene(int dungeonsId);
    /**
     * 删除临时场景
     * @param tempSceneId 副本id
     * @param teamId 队伍id
     */
    void deleteTempScene(int tempSceneId, String teamId);

}
