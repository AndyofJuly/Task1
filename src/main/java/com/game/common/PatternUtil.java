package com.game.common;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * 工具类
 * @Author andy
 * @create 2020/6/18 22:10
 */
public class PatternUtil {

    /** 正则表达式判断，获取包含数字的字符串集合*/
    public static ArrayList<Integer> getIntList(String[] str){
        ArrayList<Integer> intList = new ArrayList();
        for (String s : str) {
            if (Pattern.matches("[0-9]*", s.trim())) {
                intList.add(Integer.parseInt(s));
            }
        }
        return intList;
    }

    /** 正则表达式判断，获取不包含数字的数字集合*/
    public static ArrayList<String> getStrList(String[] str){
        ArrayList<String> strList = new ArrayList();
        for (String s : str) {
            if (!Pattern.matches("[0-9]*", s.trim())) {
                strList.add(s);
            }
        }
        return strList;
    }

    /** 字符串数组转为int数组*/
    public static int[] stringToInt(String[] arrs){
        int[] ints = new int[arrs.length];
        for(int i=0;i<arrs.length;i++){
            ints[i] = Integer.parseInt(arrs[i]);
        }
        return ints;
    }
}
