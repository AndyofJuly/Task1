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

import java.util.HashMap;
import java.util.UUID;

/**
 * @Author andy
 * @create 2020/6/5 10:48
 */
public class InitGame {

    //public static HashMap<Integer, Scene> scenes = new HashMap<Integer,Scene>();

    static {
        System.out.println("hello");
        //场景初始化-创建的临时场景同样要考虑初始化网格
        for(Integer keyScene : SceneResource.getScenesStatics().keySet()){
            GlobalResource.getScenes().put(keyScene,new Scene(keyScene,SceneResource.getScenesStatics().get(keyScene).getName(),keyScene));
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
                GlobalResource.getScenes().get(i).getGridHashMap().get(RoleService.getGridId(monster.getPosition()[0],monster.getPosition()[1])).getGridMonsterMap().put(monsterId,monster);
                System.out.println(monster.getPosition()[0]+","+monster.getPosition()[1]);
            }
        }
/*        //商品列表
        goodsList = getStaticGoodsList();
        //玩家可参与的副本
        dungeonsList = getStaticDungeonsList();*/
        //初始化网格-暂定先写一个场景下的demo
/*        for(int i=1;i<=64;i++){
            GlobalResource.getGridHashMap().put(i,new Grid(i));
        }*/



    }
    //商店列表初始化
    public static String getStaticGoodsList(){
        StringBuilder stringBuilder = new StringBuilder("欢迎光临本商店，商店提供： ");
        for(Integer key : EquipmentResource.getEquipmentStaticHashMap().keySet()){
            stringBuilder.append(EquipmentResource.getEquipmentStaticHashMap().get(key).getName()+":"+EquipmentResource.getEquipmentStaticHashMap().get(key).getPrice()).append("银； ");
        }
        for(Integer key : PotionResource.getPotionStaticHashMap().keySet()){
            stringBuilder.append(PotionResource.getPotionStaticHashMap().get(key).getName()+":"+PotionResource.getPotionStaticHashMap().get(key).getPrice()).append("银； ");
        }
        return stringBuilder.toString();
    }

    //副本列表初始化，返回副本列表集合元素信息，包括id和副本名
    public static String getStaticDungeonsList(){
        StringBuilder stringBuilder = new StringBuilder("目前可参加的副本有：\n");
        for(Integer key : DungeonsResource.getDungeonsStaticHashMap().keySet()){
            DungeonsStatic dungeons = DungeonsResource.getDungeonsStaticHashMap().get(key);
            stringBuilder.append(dungeons.getId()+":"+dungeons.getName()+"，限时"+dungeons.getDeadTime()+"秒。\n");
        }
        return stringBuilder.toString();
    }
}
