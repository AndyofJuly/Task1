package com.game.service.assis;

import com.game.common.Const;
import com.game.entity.Monster;
import com.game.entity.Scene;
import com.game.entity.excel.SceneStatic;
import com.game.entity.store.DungeonsResource;
import com.game.entity.store.SceneResource;

import java.util.UUID;

/**
 * 生成临时场景及回收，可扩展成工厂模式
 * @Author andy
 * @create 2020/6/12 10:32
 */
public class TempSceneService {
    //生成的场景id，下次生成临时场景时加1，保持每个临时场景id不同
    public static int count = 11000;
    //SceneStatic(int id, String name, String[] sceneRelation, String[] npcId, String[] monsterId)
    public static String[] sceneRelation = {Const.DUNGEONS_START_SCENEID};//固定不变，副本传送点的id
    public static String[] npcId = new String[0];
    public static String[] monsterStaticId = new String[1];

    //SceneResource.scenesStatics属性中加入，SceneResource.places属性中加入，InitGame.scenes属性中加入
    public static int createTempScene(int dungeonsId){ //传参为队伍要攻打的副本id
        //自动化
        count++;//从11001开始
        SceneStatic tempSceneStatic = null;
        Monster monster = null;
        String tempSceneName = UUID.randomUUID().toString();
        String monsterId = UUID.randomUUID().toString();
        for(Integer key : DungeonsResource.dungeonsStaticHashMap.keySet()){
            if(dungeonsId==key){ //蜘蛛副本/白虎副本/狼副本/龙副本
                monsterStaticId[0] = DungeonsResource.dungeonsStaticHashMap.get(key).getBossId()+"";
                tempSceneStatic = new SceneStatic(count,tempSceneName,sceneRelation,npcId,monsterStaticId);
                monster = new Monster(monsterId,Integer.parseInt(monsterStaticId[0]));
            }
        }
        Scene tempScene = new Scene(count);
        SceneResource.scenesStatics.put(tempSceneStatic.getId(),tempSceneStatic);
        //添加回去的路径
        SceneResource.places.put(tempSceneStatic.getName(),tempSceneStatic.getSceneRelation());
        String[] str = SceneResource.places.get(Const.DUNGEONS_START_SCENE);
        String[] strings = new String[str.length+1];
        System.arraycopy(str,0,strings,0,str.length);
        strings[strings.length-1] = count+"";//给一条通往临时副本的路，在副本传送点增加该场景id
        SceneResource.places.put(Const.DUNGEONS_START_SCENE,strings);
        InitGame.scenes.put(tempSceneStatic.getId(),new Scene(tempSceneStatic.getId()));
        //生成boss放入临时场景
        InitGame.scenes.get(count).getMonsterHashMap().put(monsterId, monster);
        //返回该场景id，根据id可获取场景名，供角色移动使用
        return count;
    }

    //SceneResource.scenesStatics静态属性中删除，SceneResource.places静态属性中删除，InitGame.scenes动态属性中删除-直接remove
    //new SceneStatic;new Scene;new String;new Monster
    public static void deleteTempScene(int tempSceneId){
        System.out.println(tempSceneId);
        InitGame.scenes.remove(tempSceneId);
        SceneResource.places.remove(SceneResource.scenesStatics.get(tempSceneId).getName());
        SceneResource.scenesStatics.remove(tempSceneId);
        System.gc();
    }
}
