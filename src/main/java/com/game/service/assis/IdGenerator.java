package com.game.service.assis;

import com.game.common.Const;

import java.util.HashSet;
import java.util.Random;

/**
 * 不重复的id生成器
 * @Author andy
 * @create 2020/6/16 16:38
 */
public class IdGenerator {
    //可以通过remove方法在结束队伍和回收场景时去掉该编号，便于后续复用
    public static HashSet<Integer> teamSet = new HashSet<>();
    public static HashSet<Integer> sceneSet = new HashSet<>();

    //如果游戏中生成的队伍数量应该小于10000
    public static String generateTeamId(){
        Integer tmp=0;
        int size = teamSet.size();
        Random ran = new Random();
        while (size+1>teamSet.size()){
            tmp = ran.nextInt(Const.Max_ID);
            teamSet.add(tmp);
        }
        return tmp+"";
    }

    //如果游戏中生成的队伍数量应该小于10000
    public static int generateSceneId(){
        Integer tmp=0;
        int size = sceneSet.size();
        Random ran = new Random();
        while (size+1>sceneSet.size()){
            tmp = ran.nextInt(Const.Max_ID);
            sceneSet.add(tmp);
        }
        return tmp;
    }
}
