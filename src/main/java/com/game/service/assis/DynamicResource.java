package com.game.service.assis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 存储全局的共享资源索引
 * @Author andy
 * @create 2020/6/6 10:58
 */
public class DynamicResource {

    // 组队列表，元素为teamId
    public static List<Integer> teamList = new ArrayList<Integer>();

    // BOSS所在场景列表，元素为BOSS所在场景的id
    public static List<Integer> sceneOfBossList = new ArrayList<Integer>();
}
