package com.game.service.assis;

import com.game.entity.Grid;
import com.game.entity.Monster;
import com.game.entity.Scene;
import com.game.entity.excel.DungeonsStatic;
import com.game.entity.store.DungeonsResource;
import com.game.entity.store.EquipmentResource;
import com.game.entity.store.PotionResource;
import com.game.entity.store.SceneResource;
import com.game.service.RoleService;
import com.game.service.helper.EquipmentHelper;
import com.game.service.helper.PotionHelper;

import java.util.HashMap;
import java.util.UUID;

/**
 * @Author andy
 * @create 2020/6/5 10:48
 */
public class InitGame {

    //public static HashMap<Integer, Scene> scenes = new HashMap<Integer,Scene>();

    static {
        //场景初始化-创建的临时场景同样要考虑初始化网格
        for(Integer keyScene : SceneResource.getScenesStatics().keySet()){
            String sceneName = SceneResource.getScenesStatics().get(keyScene).getName();
            GlobalResource.getScenes().put(keyScene,new Scene(keyScene,sceneName,keyScene));
            //对每个场景初始化64个网格
            for(int i=1;i<=64;i++){
                GlobalResource.getScenes().get(keyScene).getGridHashMap().put(i,new Grid(i));
            }
        }

        //场景中生成少量怪物-以下可拆分到其他类例如场景初始化类
        //两重循环，n个场景，每个场景遍历含有的怪物，实例化这些怪物
        //for(int i = SceneResource.initSceneId; i< SceneResource.initSceneId+SceneResource.scenesStatics.size(); i++) {
        for(Integer i : SceneResource.getScenesStatics().keySet()){
            for(int j=0;j<SceneResource.getScenesStatics().get(i).getMonsterId().length;j++) {
                //静态资源的怪物id
                String key = SceneResource.getScenesStatics().get(i).getMonsterId()[j];
                //此处每个场景生成一个对应静态的怪
                String monsterId = UUID.randomUUID().toString();
                Monster monster = new Monster(monsterId,Integer.valueOf(key));
                GlobalResource.getScenes().get(i).getMonsterHashMap().put(monsterId, monster);
                //将怪物放在单个网格中
                int gridId = RoleService.getGridId(monster.getPosition()[0],monster.getPosition()[1]);
                Scene scene = GlobalResource.getScenes().get(i);
                scene.getGridHashMap().get(gridId).getGridMonsterMap().put(monsterId,monster);
                //System.out.println(monster.getPosition()[0]+","+monster.getPosition()[1]);
            }
        }
    }
}
