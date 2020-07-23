package com.game.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 该类负责读取excel表，将其中的数据转换为json，并转化为java对象。使用了工具类，并自定义读取和解析的方式
 * @author andy
 */

public class ExcelToJson {

    public static Pattern pattern = Pattern.compile("[0-9]*");

    /**
     * 读去excel文件内容，转换为json数组
     * @param excelPath excel文件路径
     * @return json数组
     */
    public static JSONArray getNeed(String excelPath) {
        Workbook workbook = null;
        Sheet sheet;
        JSONArray jsons = new JSONArray();
        try {
            workbook = Workbook.getWorkbook(new File(excelPath));
            sheet = workbook.getSheet(0);
            for(int i = 1; i < sheet.getRows(); i++) {
                JSONObject object = new JSONObject();
                for (int k = 0; k < sheet.getColumns(); k++) {
                    if(sheet.getCell(k, i).getContents()==""){
                        object.put(sheet.getCell(k, 0).getContents(), null);
                    } else if (sheet.getCell(k, i).getContents().startsWith("[")) {
                        int[] arr = PatternUtil.stringToInt(sheet.getCell(k, i).getContents().substring(1, sheet.getCell(k, i).getContents().length() - 1).split(","));
                        object.put(sheet.getCell(k, 0).getContents(), arr);
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
}