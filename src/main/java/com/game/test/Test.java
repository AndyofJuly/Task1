package com.game.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * @Author andy
 * @create 2020/6/9 12:33
 */
public class Test {
    public static void main(String[] args) {

/*        HashSet<Integer> set = new HashSet<>();
        int out = 0;
        Integer tmp=0;
        for(int i=0;i<10;i++){
            int size = set.size();
            Random ran = new Random();
            while (size+1>set.size()){
                tmp = ran.nextInt(10);
                set.add(tmp);
            }
            System.out.println(tmp);
        }*/


/*        ArrayList<Integer> roleList = new ArrayList<Integer>();
        roleList.add(10);
        roleList.add(20);
        Integer i = 20;
        for(Integer teamId : roleList){//队伍通常较少，此循环可以接受
            //roleList.remove(20);
            //不要在遍历中使用remove方法
        }
        roleList.remove(i);//传int默认为下标，传对象则可以直接找到该对象
        roleList.remove(0);
        System.out.println(roleList.size());*/
/*        //测试结论
        //  1.HashMap中没有的key和value，当去get这个没有的key时，返回null
        //  2.引用也可以改变原值
        //  3.数组复制可以使用System.arraycopy，此为native方法
        //  4.HashMap中对重复key进行put时，value会更新
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

/*        HashMap<Integer,String> stringHashMap = new HashMap<>();
        int i=1;
        stringHashMap.put(i,"hello");
        stringHashMap.put(i,"hey");
        System.out.println(stringHashMap.get(1));*/
        //GlobalInfo.roleAndClient.put(1,"hello");//netty客户端似乎不让存储任何缓存
    }
}




