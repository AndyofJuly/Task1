package com.game.system.scene;

import com.game.common.Const;
import com.game.system.scene.pojo.*;
import com.game.system.dungeons.pojo.DungeonsResource;
import com.game.system.role.RoleService;
import com.game.system.assist.AssistService;
import com.game.system.assist.GlobalInfo;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 场景模块的业务逻辑处理
 * @Author andy
 * @create 2020/6/12 10:32
 */
@Component
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
        monster.setSceneId(tempSceneId);

        //怪物自由移动和普通攻击
        ExecutorService pool = Executors.newCachedThreadPool();
        pool.submit(new MonsterWalk(monster));

        initSceneGrid(tempSceneId,monster);
        return tempSceneId;
    }

    /**
     * 初始化小网格并放入怪物
     * @param tempSceneId 临时场景id
     */
    private void initSceneGrid(int tempSceneId,Monster monster){
        for(int i=1;i<=64;i++){
            GlobalInfo.getScenes().get(tempSceneId).getGridHashMap().put(i,new Grid(i));
        }
        int gridId = RoleService.getGridId(monster.getPosition()[0],monster.getPosition()[1]);
        Scene scene = GlobalInfo.getScenes().get(tempSceneId);
        scene.getGridHashMap().get(gridId).getGridMonsterMap().put(monster.getId(),monster);
    }

    /**
     * 回收临时场景，解散队伍-可拆
     * @param tempSceneId 副本id
     * @param teamId 队伍id
     */
    public void deleteTempScene(int tempSceneId,String teamId){
        System.out.println("对id为"+tempSceneId+"的临时场景进行回收");
        GlobalInfo.getScenes().remove(tempSceneId);
        GlobalInfo.getTeamList().remove(teamId);
        System.gc();
    }

    /**
     * 在怪物移动导致跨越格子时更新格子
     * @param x x坐标
     * @param y y坐标
     * @param monster 怪物
     * @return 返回信息提示
     */
    public static String refleshGrid(int x,int y,Monster monster){
        int oldGridId = getGridId(monster.getPosition()[0],monster.getPosition()[1]);
        int newGridId = getGridId(x,y);
        Scene scene = GlobalInfo.getScenes().get(monster.getSceneId());
        if(oldGridId==newGridId){return "原格子Monster";}
        scene.getGridHashMap().get(oldGridId).getGridMonsterMap().remove(monster.getId());
        scene.getGridHashMap().get(newGridId).getGridMonsterMap().put(monster.getId(),monster);
        return "==怪物跨格子移动了！过去格子"+oldGridId+"。现在格子"+newGridId;
    }

    /**
     * 在Npc移动导致跨越格子时更新格子
     * @param x x坐标
     * @param y y坐标
     * @param npc npc
     * @return 返回信息提示
     */
    public static String refleshGrid(int x, int y, Npc npc){
        int oldGridId = getGridId(npc.getPosition()[0],npc.getPosition()[1]);
        int newGridId = getGridId(x,y);
        Scene scene = GlobalInfo.getScenes().get(npc.getSceneId());
        if(oldGridId==newGridId){return "原格子NPC";}
        scene.getGridHashMap().get(oldGridId).getGridNpcMap().remove(npc.getNpcId());
        scene.getGridHashMap().get(newGridId).getGridNpcMap().put(npc.getNpcId(),npc);
        return "过去格子"+oldGridId+"。现在格子"+newGridId;
    }

    public static void checkAndSetMonsterHp(int hp,Monster monster){
        if(hp<0){
            monster.setMonsterHp(0);
            monster.setAlive(0);
        }else{
            monster.setMonsterHp(hp);
        }
    }

    /**
     * 根据坐标位置计算小网格id
     * @param x 水平方向移动距离
     * @param y 垂直方向移动距离
     * @return 小网格id
     */
    private static int getGridId(int x,int y){
        return x/Const.GRID_LENGTH+1+y/Const.GRID_WIDTH*Const.GRID_WIDTH;
    }
}
