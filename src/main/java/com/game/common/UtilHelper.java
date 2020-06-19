package com.game.common;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * 工具类
 * @Author andy
 * @create 2020/6/18 22:10
 */
public class UtilHelper {

    //正则表达式判断，获取字符串集合
    public static ArrayList<Integer> getIntList(String[] str){
        ArrayList<Integer> intList = new ArrayList();
        for(int i=0;i<str.length;i++){
            if(Pattern.matches("[0-9]*", str[i])){
                intList.add(Integer.parseInt(str[i]));
            }
        }
        return intList;
    }

    //正则表达式判断，获取数字集合
    public static ArrayList<String> getStrList(String[] str){
        ArrayList<String> strList = new ArrayList();
        for(int i=0;i<str.length;i++){
            if(!Pattern.matches("[0-9]*", str[i])){
                strList.add(str[i]);
            }
        }
        return strList;
    }
}
