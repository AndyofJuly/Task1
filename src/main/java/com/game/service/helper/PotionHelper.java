package com.game.service.helper;

import com.game.entity.store.PotionResource;

/**
 * @Author andy
 * @create 2020/6/29 16:51
 */
public class PotionHelper {
    public static String getPotionName(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getName();
    }
    //获得价格
    public static int getPotionPrice(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getPrice();
    }

    public static int getPotionAddHp(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getAddHp();
    }

    public static int getPotionAddMp(int key){
        return PotionResource.getPotionStaticHashMap().get(key).getAddMp();
    }
}
