package com.game.service.assis;

import com.game.entity.Grid;
import com.game.entity.Monster;
import com.game.entity.Scene;
import com.game.entity.store.DungeonsResource;
import com.game.entity.store.SceneResource;
import com.game.service.RoleService;

import java.util.UUID;

/**
 * 生成临时场景及回收，可扩展成工厂模式
 * @Author andy
 * @create 2020/6/12 10:32
 */
public class TempSceneService {

    public static int createTempScene(int dungeonsId){ //传参为队伍要攻打的副本id
        int tempSceneId = IdGenerator.generateSceneId();

        String tempSceneName = UUID.randomUUID().toString();
        String monsterId = UUID.randomUUID().toString();

        //当前副本对应的静态场景id
        int staticSceneId = DungeonsResource.getDungeonsStaticHashMap().get(dungeonsId).getSceneId();
        Scene tempScene = new Scene(tempSceneId,tempSceneName,staticSceneId);
        //对需要的部分进行改造，例如名字(可以不修改)和Npc
        int monsterStaticId = Integer.parseInt(SceneResource.getScenesStatics().get(staticSceneId).getMonsterId()[0]);
        //目前只有一个元素
        Monster monster = new Monster(monsterId,monsterStaticId);
        GlobalResource.getScenes().put(tempSceneId,tempScene);
        //生成boss放入临时场景
        GlobalResource.getScenes().get(tempSceneId).getMonsterHashMap().put(monsterId, monster);
        GlobalResource.getTempIdHashMap().put(tempSceneId,monsterId);
        //GlobalResource.tempNameHashMap.put(tempSceneName,tempSceneId);
        //返回该场景id，根据id可获取场景名，供角色移动使用

        //初始化网格并放入怪物或npc等
        for(int i=1;i<=64;i++){
            GlobalResource.getScenes().get(tempSceneId).getGridHashMap().put(i,new Grid(i));
        }
        int gridId = RoleService.getGridId(monster.getPosition()[0],monster.getPosition()[1]);
        Scene scene = GlobalResource.getScenes().get(tempSceneId);
        scene.getGridHashMap().get(gridId).getGridMonsterMap().put(monsterId,monster);

        return tempSceneId;
    }

    public static void deleteTempScene(int tempSceneId,String teamId){
        System.out.println("对id为"+tempSceneId+"的临时场景进行回收");
        GlobalResource.getScenes().remove(tempSceneId);
        //team也要解散回收
        GlobalResource.getTeamList().remove(teamId);
        System.gc();
    }
}
