package com.game.service.assis;

import com.game.common.Const;
import com.game.entity.Monster;
import com.game.entity.Scene;
import com.game.entity.excel.SceneStatic;
import com.game.entity.store.DungeonsResource;
import com.game.entity.store.SceneResource;
import com.game.service.assis.InitGame;

import java.util.UUID;

/**
 * 生成临时场景及回收，可扩展成工厂模式
 * @Author andy
 * @create 2020/6/12 10:32
 */
public class TempSceneService {
    //生成的场景id，下次生成临时场景时加1，保持每个临时场景id不同
    public static int tempSceneId;

    public static int createTempScene(int dungeonsId){ //传参为队伍要攻打的副本id
        tempSceneId = IdGenerator.generateSceneId();

        String tempSceneName = UUID.randomUUID().toString();
        String monsterId = UUID.randomUUID().toString();

        //当前副本对应的静态场景id
        int staticSceneId = DungeonsResource.dungeonsStaticHashMap.get(dungeonsId).getSceneId();
        Scene tempScene = new Scene(tempSceneId,tempSceneName,staticSceneId);
        //对需要的部分进行改造，例如名字(可以不修改)和Npc

        Monster monster = new Monster(monsterId,Integer.parseInt(SceneResource.scenesStatics.get(staticSceneId).getMonsterId()[0]));//目前只有一个元素

        InitGame.scenes.put(tempSceneId,tempScene);
        //生成boss放入临时场景
        InitGame.scenes.get(tempSceneId).getMonsterHashMap().put(monsterId, monster);
        DynamicResource.tempIdHashMap.put(tempSceneId,monsterId);
        //DynamicResource.tempNameHashMap.put(tempSceneName,tempSceneId);
        //返回该场景id，根据id可获取场景名，供角色移动使用
        return tempSceneId;
    }

    public static void deleteTempScene(int tempSceneId,String teamId){
        System.out.println("对id为"+tempSceneId+"的临时场景进行回收");
        InitGame.scenes.remove(tempSceneId);
        IdGenerator.sceneSet.remove(tempSceneId);
        //team也要解散回收
        DynamicResource.teamList.remove(teamId);
        IdGenerator.teamSet.remove(teamId);
        System.gc();
    }
}