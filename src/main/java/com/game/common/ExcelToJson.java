package com.game.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.entity.Monster;
import com.game.entity.Npc;
import com.game.entity.Scene;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import java.io.*;
import java.util.HashMap;
import java.util.Properties;

/**
 * 该类负责读取excel表，将其中的数据转换为json，并转化为java对象
 * @author andy
 */

public class ExcelToJson {
    public static HashMap<Integer,Scene> scenes = new HashMap<Integer,Scene>();
    public static HashMap<Integer,Npc> npcs = new HashMap<Integer,Npc>();
    public static HashMap<Integer,Monster> monsters = new HashMap<Integer,Monster>();
    public static HashMap<String, String[]> places= new HashMap<String,String[]>();
    public static int initSceneId;

    static {
        scenes = (HashMap<Integer, Scene>) getNeed("SCENE_CONST_PATH");
        npcs = (HashMap<Integer, Npc>) getNeed("NPC_CONST_PATH");
        monsters = (HashMap<Integer, Monster>) getNeed("MONSTER_CONST_PATH");
        //System.out.println(npcs.get(20001).getAlive()+";"+npcs.get(20002).getAlive());
    }

    //读取excel文件，解析为JSON，再转为对象
    public static Object getNeed(String str) {
        Workbook workbook = null;
        Sheet sheet;
        JSONArray jsons = new JSONArray();
        try {
            workbook = Workbook.getWorkbook(new File(getConfigPath(str)));
            sheet = workbook.getSheet(0);
            JSONObject object = new JSONObject();
            for(int i = 1; i < sheet.getRows(); i++) {
                for (int k = 0; k < sheet.getColumns(); k++) {
                    if (k == 0) {
                        object.put(sheet.getCell(k, 0).getContents(), Integer.valueOf(sheet.getCell(k, i).getContents()).intValue());
                    } else {
                        if (sheet.getCell(k, i).getContents().startsWith("[")) {
                            object.put(sheet.getCell(k, 0).getContents(), sheet.getCell(k, i).getContents().substring(1, sheet.getCell(k, i).getContents().length() - 1).split(","));
                        } else {
                            object.put(sheet.getCell(k, 0).getContents(), sheet.getCell(k, i).getContents());
                        }
                    }
                }
                jsons.add(object);
                if(str.equals("NPC_CONST_PATH")){
                    npcs.put(Integer.valueOf(sheet.getCell(0 , i).getContents()),JSONObject.toJavaObject(jsons.getJSONObject(i-1),Npc.class));
                }else if(str.equals("MONSTER_CONST_PATH")){
                    monsters.put(Integer.valueOf(sheet.getCell(0 , i).getContents()),JSONObject.toJavaObject(jsons.getJSONObject(i-1),Monster.class));
                }else {
                    initSceneId = Integer.valueOf(sheet.getCell(0 , 1).getContents()).intValue();
                    places.put(sheet.getCell(1 , i).getContents(),sheet.getCell(2 , i).getContents().substring(1,sheet.getCell(2 , i).getContents().length()-1).split(","));
                    scenes.put(Integer.valueOf(sheet.getCell(0 , i).getContents()),JSONObject.toJavaObject(jsons.getJSONObject(i-1),Scene.class));
                }
            }
            workbook.close();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(str.equals("NPC_CONST_PATH")){
            return npcs;
        }else if(str.equals("MONSTER_CONST_PATH")){
            return monsters;
        }else {
            return scenes;
        }
    }

    //读取路径配置文件
    public static String getConfigPath(String PATH){
        Properties p = new Properties();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"),"utf-8");
            p.load(inputStreamReader);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p.getProperty(PATH);
    }
}