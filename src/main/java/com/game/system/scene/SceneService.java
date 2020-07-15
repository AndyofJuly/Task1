package com.game.system.scene;

import com.game.common.Const;
import com.game.system.scene.pojo.*;
import com.game.system.dungeons.pojo.DungeonsResource;
import com.game.system.role.RoleService;
import com.game.system.assist.AssistService;
import com.game.system.assist.GlobalInfo;

import java.util.UUID;

/**
 * 生成临时场景及回收，可扩展成工厂模式
 * @Author andy
 * @create 2020/6/12 10:32
 */
public class SceneService {

    /**
     * 创建临时场景
     * @param dungeonsId 副本id
     * @return 返回该临时场景id
     */
    public int createTempScene(int dungeonsId){ //传参为队伍要攻打的副本id
        int tempSceneId = AssistService.generateSceneId();
        String tempSceneName = UUID.randomUUID().toString();
        String monsterId = UUID.randomUUID().toString();

        //临时场景
        int staticSceneId = DungeonsResource.getDungeonsStaticHashMap().get(dungeonsId).getSceneId();
        Scene tempScene = new Scene(tempSceneId,tempSceneName,staticSceneId);
        GlobalInfo.getScenes().put(tempSceneId,tempScene);

        //场景怪物
        int monsterStaticId = SceneResource.getScenesStatics().get(staticSceneId).getMonsterId()[0];
        Monster monster = new Monster(monsterId,monsterStaticId);
        GlobalInfo.getScenes().get(tempSceneId).getMonsterHashMap().put(monsterId, monster);
        GlobalInfo.getTempIdHashMap().put(tempSceneId,monsterId);

        initSceneGrid(tempSceneId,monster);
        return tempSceneId;
    }

    //初始化网格并放入怪物或npc等
    private void initSceneGrid(int tempSceneId,Monster monster){
        for(int i=1;i<=64;i++){
            GlobalInfo.getScenes().get(tempSceneId).getGridHashMap().put(i,new Grid(i));
        }
        int gridId = RoleService.getGridId(monster.getPosition()[0],monster.getPosition()[1]);
        Scene scene = GlobalInfo.getScenes().get(tempSceneId);
        scene.getGridHashMap().get(gridId).getGridMonsterMap().put(monster.getId(),monster);
    }



    /**
     * 删除临时场景
     * @param tempSceneId 副本id
     * @param teamId 队伍id
     */
    public void deleteTempScene(int tempSceneId,String teamId){
        System.out.println("对id为"+tempSceneId+"的临时场景进行回收");
        GlobalInfo.getScenes().remove(tempSceneId);
        //team也要解散回收
        GlobalInfo.getTeamList().remove(teamId);
        System.gc();
    }

    //怪物移动，初始化怪物时调用
    //给怪物和npc的网格更新与角色相同，只需要更新小格子即可
    public static String refleshGrid(int x,int y,Monster monster){
        int oldGridId = getGridId(monster.getPosition()[0],monster.getPosition()[1]);
        int newGridId = getGridId(x,y);
        Scene scene = GlobalInfo.getScenes().get(monster.getSceneId());
        if(oldGridId==newGridId){return "原格子Monster";}
        scene.getGridHashMap().get(oldGridId).getGridMonsterMap().remove(monster.getId());
        scene.getGridHashMap().get(newGridId).getGridMonsterMap().put(monster.getId(),monster);
        return "==怪物跨格子移动了！过去格子"+oldGridId+"。现在格子"+newGridId;
    }

    //给Npc一个扩展的更新方法，只需要更新小格子即可
    public static String refleshGrid(int x, int y, Npc npc){
        int oldGridId = getGridId(npc.getPosition()[0],npc.getPosition()[1]);
        int newGridId = getGridId(x,y);
        Scene scene = GlobalInfo.getScenes().get(npc.getSceneId());
        if(oldGridId==newGridId){return "原格子NPC";}
        scene.getGridHashMap().get(oldGridId).getGridNpcMap().remove(npc.getNpcId());
        scene.getGridHashMap().get(newGridId).getGridNpcMap().put(npc.getNpcId(),npc);
        return "过去格子"+oldGridId+"。现在格子"+newGridId;
    }

    private static int getGridId(int x,int y){
        return x/Const.GRID_LENGTH+1+y/Const.GRID_WIDTH*Const.GRID_WIDTH;
    }
}
