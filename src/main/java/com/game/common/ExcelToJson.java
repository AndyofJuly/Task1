package com.game.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.entity.*;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import java.io.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 该类负责读取excel表，将其中的数据转换为json，并转化为java对象
 * @author andy
 */

public class ExcelToJson {
    public static Pattern pattern = Pattern.compile("[0-9]*");
    //读取excel文件，解析为JSON
    public static JSONArray getNeed(String str) {
        Workbook workbook = null;
        Sheet sheet;
        JSONArray jsons = new JSONArray();
        try {
            workbook = Workbook.getWorkbook(new File(str));
            sheet = workbook.getSheet(0);
            for(int i = 1; i < sheet.getRows(); i++) {
                JSONObject object = new JSONObject();
                for (int k = 0; k < sheet.getColumns(); k++) {
                        if (sheet.getCell(k, i).getContents().startsWith("[")) {
                            object.put(sheet.getCell(k, 0).getContents(), sheet.getCell(k, i).getContents().substring(1, sheet.getCell(k, i).getContents().length() - 1).split(","));
                        } else if(pattern.matcher(sheet.getCell(k, i).getContents()).matches()){
                            object.put(sheet.getCell(k, 0).getContents(), Integer.parseInt(sheet.getCell(k, i).getContents()));
                        } else {
                            object.put(sheet.getCell(k, 0).getContents(), sheet.getCell(k, i).getContents());
                        }
                }
                jsons.add(object);
            }
            workbook.close();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsons;
    }

/*    //读取路径配置文件
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
    }*/
}