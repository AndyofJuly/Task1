package com.game.system.scene;

import com.game.system.scene.pojo.Grid;
import com.game.system.scene.pojo.Monster;
import com.game.system.scene.pojo.Scene;
import com.game.system.dungeons.pojo.DungeonsResource;
import com.game.system.scene.pojo.SceneResource;
import com.game.system.role.RoleServiceImpl;
import com.game.system.assist.AssistService;
import com.game.system.assist.GlobalInfo;

import java.util.UUID;

/**
 * 生成临时场景及回收，可扩展成工厂模式
 * @Author andy
 * @create 2020/6/12 10:32
 */
public class SceneServiceImpl implements ISceneService {

    @Override
    public int createTempScene(int dungeonsId){ //传参为队伍要攻打的副本id
        int tempSceneId = AssistService.generateSceneId();

        String tempSceneName = UUID.randomUUID().toString();
        String monsterId = UUID.randomUUID().toString();

        //当前副本对应的静态场景id
        int staticSceneId = DungeonsResource.getDungeonsStaticHashMap().get(dungeonsId).getSceneId();
        Scene tempScene = new Scene(tempSceneId,tempSceneName,staticSceneId);
        //对需要的部分进行改造，例如名字(可以不修改)和Npc
        int monsterStaticId = SceneResource.getScenesStatics().get(staticSceneId).getMonsterId()[0];
        //目前只有一个元素
        Monster monster = new Monster(monsterId,monsterStaticId);
        GlobalInfo.getScenes().put(tempSceneId,tempScene);
        //生成boss放入临时场景
        GlobalInfo.getScenes().get(tempSceneId).getMonsterHashMap().put(monsterId, monster);
        GlobalInfo.getTempIdHashMap().put(tempSceneId,monsterId);
        //GlobalInfo.tempNameHashMap.put(tempSceneName,tempSceneId);
        //返回该场景id，根据id可获取场景名，供角色移动使用

        //初始化网格并放入怪物或npc等
        for(int i=1;i<=64;i++){
            GlobalInfo.getScenes().get(tempSceneId).getGridHashMap().put(i,new Grid(i));
        }
        int gridId = RoleServiceImpl.getGridId(monster.getPosition()[0],monster.getPosition()[1]);
        Scene scene = GlobalInfo.getScenes().get(tempSceneId);
        scene.getGridHashMap().get(gridId).getGridMonsterMap().put(monsterId,monster);

        return tempSceneId;
    }

    @Override
    public void deleteTempScene(int tempSceneId,String teamId){
        System.out.println("对id为"+tempSceneId+"的临时场景进行回收");
        GlobalInfo.getScenes().remove(tempSceneId);
        //team也要解散回收
        GlobalInfo.getTeamList().remove(teamId);
        System.gc();
    }

    //怪物移动，初始化怪物时调用
}
