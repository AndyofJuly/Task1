package com.game.service.assis;

import com.game.entity.Team;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 存储全局的共享资源索引
 * @Author andy
 * @create 2020/6/6 10:58
 */
public class DynamicResource {

    // 组队列表，key为teamId
    public static HashMap<String, Team> teamList = new HashMap<String, Team>();

    // BOSS所在场景列表，元素为BOSS所在场景的id
    public static ArrayList<String> sceneOfBossList = new ArrayList<String>();

    // 限购10件，角色购买数量记录，参数：roleId,goodsId,goodsNum，key为roleId,value重的key为物品id，value为物品数量
    public static HashMap<Integer,HashMap<Integer,Integer>> buyRecord = new HashMap<>();

    //临时副本id获取该副本内的临时怪物id，分别对应key和value
    public static HashMap<Integer,String> tempIdHashMap = new HashMap<>();

}
