package com.game.test.self;

import com.game.system.bag.pojo.Equipment;
import com.game.system.bag.pojo.Potion;

import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/7/20 10:15
 */
public class Test2 {

    private static HashMap<Object,Integer> itemsHashMap = new HashMap<>();

    /*public static void main(String[] args) {
        Equipment equipment = new Equipment(1,3001);
        Potion potion = new Potion(2002);

        itemsHashMap.put(equipment,1);
        itemsHashMap.put(potion,2);
        Equipment equipment2 = new Equipment(1,3001);
        itemsHashMap.put(equipment2,1);
        System.out.println(itemsHashMap.size()+";"+itemsHashMap.get(equipment)+"==========");
        System.out.println(itemsHashMap.size()+";"+itemsHashMap.get(potion)+"==========");
        for(Object object : itemsHashMap.keySet()){
            if(object instanceof Equipment){
                Equipment equipment1 = (Equipment)object;
                System.out.println(equipment1.getId()+"==========");
            }else {
                Potion potion1 = (Potion)object;
                System.out.println(potion1.getPotionId()+"==========");
            }
        }

    }*/

}
