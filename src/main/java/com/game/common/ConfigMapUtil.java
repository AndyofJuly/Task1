package com.game.common;

import com.game.entity.Monster;
import com.game.entity.Npc;
import com.game.entity.Scene;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class ConfigMapUtil  {
    //读取场景配置文件信息并缓存
    public static HashMap<Integer, String> sceneInfo = new HashMap<Integer, String>();
    //读取npc配置文件信息并缓存
    public static HashMap<Integer, String> npcInfo = new HashMap<Integer, String>();
    //读取怪物配置文件信息并缓存
    public static HashMap<Integer, String> monsterInfo = new HashMap<Integer, String>();

    public static HashMap<Integer,Scene> scenes = new HashMap<Integer,Scene>();
    public static HashMap<Integer,Npc[]> npcs = new HashMap<Integer,Npc[]>();
    public static HashMap<Integer,Monster[]> monsters = new HashMap<Integer,Monster[]>();
    public static HashMap<String, int[]> places= new HashMap<String,int[]>();

    static {
        try {
            //读取场景配置文件
            InputStreamReader inputStreamReader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("scenes.properties"),"utf-8");
            Properties propertiesScene = new Properties();
            propertiesScene.load(inputStreamReader);
            Enumeration enumeration = propertiesScene.propertyNames();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = propertiesScene.getProperty(key);
                sceneInfo.put(Integer.valueOf(key), value);
            }

            //读取npc配置文件
            inputStreamReader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("npc.properties"),"utf-8");
            Properties propertiesNpc = new Properties();
            propertiesNpc.load(inputStreamReader);
            enumeration = propertiesNpc.propertyNames();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = propertiesNpc.getProperty(key);
                npcInfo.put(Integer.valueOf(key), value);
            }

            //读取怪物配置文件
            inputStreamReader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("monster.properties"),"utf-8");
            Properties propertiesMonster = new Properties();
            propertiesMonster.load(inputStreamReader);
            enumeration = propertiesMonster.propertyNames();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = propertiesMonster.getProperty(key);
                monsterInfo.put(Integer.valueOf(key), value);
            }

            //npc集合
            for(int i = 10001; i< 10001+sceneInfo.size(); i++){
                String[] str = sceneInfo.get(i).split("/");//注意每一次不一样
                int[] str3 = Arrays.stream(str[2].split(",")).mapToInt(Integer::parseInt).toArray();//转换为int数组
                Npc[] temp = new Npc[str3.length];
                for(int j=0;j<str3.length;j++){
                    temp[j]=new Npc(npcInfo.get(str3[j]));
                }
                npcs.put(Integer.valueOf(i),temp);
            }

            //monster集合
            for(int i = 10001; i< 10001+sceneInfo.size(); i++){
                String[] str = sceneInfo.get(i).split("/");//注意每一次不一样
                int[] str4 = Arrays.stream(str[3].split(",")).mapToInt(Integer::parseInt).toArray();//转换为int数组
                Monster[] temp = new Monster[str4.length];
                for(int j=0;j<str4.length;j++){
                    temp[j]=new Monster(monsterInfo.get(str4[j]));
                }
                monsters.put(Integer.valueOf(i),temp);
            }

            //place and scene集合
            for(int i = 10001; i< 10001+sceneInfo.size(); i++){
                String[] str = sceneInfo.get(i).split("/");
                int[] str2 = Arrays.stream(str[1].split(",")).mapToInt(Integer::parseInt).toArray();//转换为int数组
                places.put(str[0],str2);
                scenes.put(Integer.valueOf(i),new Scene(str[0],str2));
                scenes.get(i).setNpcAll(npcs.get(i));
                scenes.get(i).setMonsterAll(monsters.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*    public static String gets(Integer key) {
        return sceneInfo.get(key);
    }

    public static String getn(Integer key) {
        return npcInfo.get(key);
    }

    public static String getm(Integer key) {
        return monsterInfo.get(key);
    }*/

}