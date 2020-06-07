package com.game.service.assis;

import com.game.entity.Monster;
import com.game.entity.Scene;
import com.game.entity.store.EquipmentResource;
import com.game.entity.store.PotionResource;
import com.game.entity.store.SceneResource;

import java.util.HashMap;
import java.util.UUID;

/**
 * @Author andy
 * @create 2020/6/5 10:48
 */
public class InitGame {

    public static HashMap<Integer, Scene> scenes = new HashMap<Integer,Scene>();
    public static String goodsList;
    static {
        //场景初始化
        for(Integer keyScene : SceneResource.scenesStatics.keySet()){
            scenes.put(keyScene,new Scene(keyScene));
        }

        //场景中生成少量怪物-以下可拆分到其他类例如场景初始化类
        //两重循环，n个场景，每个场景遍历含有的怪物，实例化这些怪物
        for(int i = SceneResource.initSceneId; i< SceneResource.initSceneId+SceneResource.scenesStatics.size(); i++) {
            for(int j=0;j<SceneResource.scenesStatics.get(i).getMonsterId().length;j++) {
                String key = SceneResource.scenesStatics.get(i).getMonsterId()[j];//静态资源的怪物id
                //此处每个场景生成一个对应静态的怪
                String monsterId = UUID.randomUUID().toString();
                //int monsterId = key+i+random.nextInt(100);
                System.out.println(monsterId);
                InitGame.scenes.get(i).getMonsterHashMap().put(monsterId, new Monster(monsterId,Integer.valueOf(key)));
                System.out.println(InitGame.scenes.get(i).getMonsterHashMap().get(monsterId).getMonsterId());
            }
            System.out.println();
        }
        goodsList = getStaticList();

    }
    //商店列表初始化
    public static String getStaticList(){
        StringBuilder stringBuilder = new StringBuilder("欢迎光临本商店，商店提供： ");
        for(Integer key : EquipmentResource.equipmentStaticHashMap.keySet()){
            stringBuilder.append(EquipmentResource.equipmentStaticHashMap.get(key).getName()+":"+EquipmentResource.equipmentStaticHashMap.get(key).getPrice()).append("银； ");
        }
        //stringBuilder.append("。 ");
        for(Integer key : PotionResource.potionStaticHashMap.keySet()){
            stringBuilder.append(PotionResource.potionStaticHashMap.get(key).getName()+":"+PotionResource.potionStaticHashMap.get(key).getPrice()).append("银； ");
        }
        return stringBuilder.toString();
    }
}
