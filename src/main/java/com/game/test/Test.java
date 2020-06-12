package com.game.test;

import com.game.entity.Monster;
import com.game.entity.store.BabyResource;
import com.game.service.assis.InitGame;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/6/9 12:33
 */
public class Test {
    public static void main(String[] args) {
/*        //测试结论
        //  1.HashMap中没有的key和value，当去get这个没有的key时，返回null
        //  2.引用也可以改变原值
        //  3.数组复制可以使用System.arraycopy，此为native方法
        Monster baby = new Monster("101",30017);
        HashMap<String,Monster> monsterHashMap = new HashMap<>();
        monsterHashMap.put("101",baby);
        InitGame.scenes.get(10007).setMonsterHashMap(monsterHashMap);
        Monster ss=InitGame.scenes.get(10007).getMonsterHashMap().get("101");
        System.out.println(ss.getAlive());
        ss.setAlive(0);
        Monster tt = InitGame.scenes.get(10007).getMonsterHashMap().get("101");
        System.out.println(tt.getAlive());*/

/*        int arr1[] = {0,1,2,3,4,5};
        int arr2[] = {10,10,20,30,40,50,60};
        System.arraycopy(arr1,0,arr2,0,6);
        arr2[arr2.length-1]=6;
        System.out.println(Arrays.toString(arr2));*/


    }
}




