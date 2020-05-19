package com.game.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.entity.Monster;
import com.game.entity.Npc;
import com.game.entity.Scene;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


/**
 * 该类负责读取excel表，将其中的数据转换为json，并转化为java对象
 * @author andy
 */
public class ExcelToJson {
    public static HashMap<Integer,Scene> scenes = new HashMap<Integer,Scene>();
    public static HashMap<Integer,Npc> npcs = new HashMap<Integer,Npc>();
    public static HashMap<Integer,Monster> monsters = new HashMap<Integer,Monster>();
    public static HashMap<String, String[]> places= new HashMap<String,String[]>();

    static {
        scenes = getScene();
        npcs = getNpcs();
        monsters = getMonsters();
    }

    public static HashMap<Integer,Scene> getScene() {
        Workbook workbook;
        Sheet sheet;
        //Cell cell1,cell2,cell3,cell4,cell5;
        JSONArray jsons = new JSONArray();
        try {
            workbook = Workbook.getWorkbook(new File( ".\\src\\main\\resources\\scenes.xls"));
            sheet = workbook.getSheet(0);
            System.out.println(sheet.getColumns());
            Cell[] cells = new Cell[sheet.getColumns()];
            for(int i = 1; i < sheet.getRows(); i++) {
                for(int j=0;j<sheet.getColumns();j++){
                    cells[j] = sheet.getCell(j , i);
                }

                JSONObject object = new JSONObject();
                String[] arrName = cells[2].getContents().substring(1,cells[2].getContents().length()-1).split(",");
                String[] arrNpc = cells[3].getContents().substring(1,cells[3].getContents().length()-1).split(",");
                String[] arrMonster = cells[4].getContents().substring(1,cells[4].getContents().length()-1).split(",");

                object.put("id", Integer.valueOf(cells[0].getContents()).intValue());
                object.put("name", cells[1].getContents());
                object.put("sceneRelation", arrName);
                object.put("npcId", arrNpc);
                object.put("monsterId", arrMonster);
                //加入json队列
                jsons.add(object);
                scenes.put(Integer.valueOf(cells[0].getContents()),JSONObject.toJavaObject(jsons.getJSONObject(i-1),Scene.class));
                places.put(cells[1].getContents(),arrName);
            }
            workbook.close();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scenes;
    }

    public static HashMap<Integer,Npc> getNpcs() {
        Workbook workbook;
        Sheet sheet;
        Cell cell1,cell2;
        JSONArray jsons = new JSONArray();
        try {
            workbook = Workbook.getWorkbook(new File( ".\\src\\main\\resources\\npc.xls"));
            sheet = workbook.getSheet(0);
            Cell[] cells = new Cell[sheet.getColumns()];
            for(int i = 1; i < sheet.getRows(); i++) {
                for(int j=0;j<sheet.getColumns();j++){
                    cells[j] = sheet.getCell(j , i);
                }

                JSONObject object = new JSONObject();
                object.put("id", Integer.valueOf(cells[0].getContents()).intValue());
                object.put("name", cells[1].getContents());
                jsons.add(object);
                npcs.put(Integer.valueOf(cells[0].getContents()),JSONObject.toJavaObject(jsons.getJSONObject(i-1),Npc.class));
            }
            workbook.close();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return npcs;
    }

    public static HashMap<Integer,Monster> getMonsters() {
        Workbook workbook;
        Sheet sheet;
        Cell cell1,cell2;
        JSONArray jsons = new JSONArray();
        try {
            workbook = Workbook.getWorkbook(new File( ".\\src\\main\\resources\\monster.xls"));
            sheet = workbook.getSheet(0);
            Cell[] cells = new Cell[sheet.getColumns()];
            for(int i = 1; i < sheet.getRows(); i++) {
                for(int j=0;j<sheet.getColumns();j++){
                    cells[j] = sheet.getCell(j , i);
                }

                JSONObject object = new JSONObject();
                object.put("id", Integer.valueOf(cells[0].getContents()).intValue());
                object.put("name", cells[1].getContents());
                jsons.add(object);
                monsters.put(Integer.valueOf(cells[0].getContents()),JSONObject.toJavaObject(jsons.getJSONObject(i-1),Monster.class));
            }
            workbook.close();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return monsters;
    }
}