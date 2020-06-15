package com.game.service.assis;

import com.game.entity.Monster;
import com.game.entity.Scene;
import com.game.entity.excel.DungeonsStatic;
import com.game.entity.store.DungeonsResource;
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
    public static String dungeonsList;

    static {
        //场景初始化
        for(Integer keyScene : SceneResource.scenesStatics.keySet()){
            scenes.put(keyScene,new Scene(keyScene));
        }

        //场景中生成少量怪物-以下可拆分到其他类例如场景初始化类
        //两重循环，n个场景，每个场景遍历含有的怪物，实例化这些怪物
        //for(int i = SceneResource.initSceneId; i< SceneResource.initSceneId+SceneResource.scenesStatics.size(); i++) {
        for(Integer i : SceneResource.scenesStatics.keySet()){
            for(int j=0;j<SceneResource.scenesStatics.get(i).getMonsterId().length;j++) {
                //静态资源的怪物id
                String key = SceneResource.scenesStatics.get(i).getMonsterId()[j];
                //此处每个场景生成一个对应静态的怪
                String monsterId = UUID.randomUUID().toString();
                //int monsterId = key+i+random.nextInt(100);
                InitGame.scenes.get(i).getMonsterHashMap().put(monsterId, new Monster(monsterId,Integer.valueOf(key)));
            }
        }

        //商品列表
        goodsList = getStaticGoodsList();

        //玩家可参与副本
        dungeonsList = getStaticDungeonsList();

    }
    //商店列表初始化
    public static String getStaticGoodsList(){
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

    //副本列表初始化，返回副本列表集合元素信息，包括id和副本名
    public static String getStaticDungeonsList(){
        StringBuilder stringBuilder = new StringBuilder("目前可参加的副本有：\n");
        for(Integer key : DungeonsResource.dungeonsStaticHashMap.keySet()){
            DungeonsStatic dungeons = DungeonsResource.dungeonsStaticHashMap.get(key);
            stringBuilder.append(dungeons.getId()+":"+dungeons.getName()+"，限时"+dungeons.getDeadTime()+"秒。\n");
        }
        return stringBuilder.toString();
    }
}
