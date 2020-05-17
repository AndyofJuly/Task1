package com.game.common;

import com.game.entity.Monster;
import com.game.entity.Npc;
import com.game.entity.Scene;

import java.util.ArrayList;

/**
 * 常量类，包括一些场景的名字，场景之间的联系等
 * @Author andy
 * @create 2020/5/15 9:30
 *
 */
public class Const {

    public static ArrayList<Scene> scenes = new ArrayList<Scene>();
    public static ArrayList<Npc[]> npcs = new ArrayList<Npc[]>();
    public static ArrayList<Monster[]> monsters = new ArrayList<Monster[]>();

    public static String[] SCENE_NAME = {"起始之地","村子","城堡","未知世界","森林"};
    public final static int[][] SCENE_POSITION = {{1,1},{1,2},{1,3},{1,4},{2,2}};
    public static String[][] NPC_NAME = {{"老爷爷"},{"村长","铁匠"},{"公主","商人"},{"守将"},{"樵夫","猎户"}};
    public static String[][] MONSTER_NAME = {{"史莱姆","飞鹰"},{"小偷","恶人"},{"逃兵","叛军"},{"恶龙","巨兽"},{"猛虎","毒蛇"}};
    public static String[][] place = new String[4][6];

    //根据常量数组的元素来设置场景的名字和位置，以及对应场景包含的NPC和怪物
    static {
        for(int i=0;i<SCENE_NAME.length;i++){
            place[SCENE_POSITION[i][0]][SCENE_POSITION[i][1]]=SCENE_NAME[i];
        }

        for(int i=0;i<NPC_NAME.length;i++){
            Npc[] temp = new Npc[NPC_NAME[i].length];
            for(int j=0;j<NPC_NAME[i].length;j++){
                temp[j]=new Npc(i+1,NPC_NAME[i][j]);
            }
            npcs.add(temp);
        }
        for(int i=0;i<MONSTER_NAME.length;i++){
            Monster[] temp = new Monster[MONSTER_NAME[i].length];
            for(int j=0;j<MONSTER_NAME[i].length;j++){
                temp[j]=new Monster(i+1,MONSTER_NAME[i][j]);
            }
            monsters.add(temp);
        }
        for(int i=0;i<SCENE_NAME.length;i++){
            scenes.add(new Scene(i+1,SCENE_NAME[i],SCENE_POSITION[i][0],SCENE_POSITION[i][1]));
            scenes.get(i).setNpcAll(npcs.get(i));
            scenes.get(i).setMonsterAll(monsters.get(i));
        }
    }

}
