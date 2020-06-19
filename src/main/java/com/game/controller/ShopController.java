package com.game.controller;

import com.game.service.ShopService;
import com.game.service.assis.GlobalResource;
import com.game.service.assis.InitGame;
import org.springframework.stereotype.Service;
import com.game.common.MyAnnontation;

import java.util.ArrayList;

/**
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Service
public class ShopController {

    private ArrayList<String> strList = GlobalResource.getStrList();
    private ArrayList<Integer> intList = GlobalResource.getIntList();

    //获得商店的商品列表，使用举例：getGoodsList
    @MyAnnontation(MethodName = "getGoodsList")
    public String getGoodsList(){
        return InitGame.getStaticGoodsList();
    }

    //购买药品or装备，使用举例：buy 清泉酒 20
    @MyAnnontation(MethodName = "buy")
    public String buyGoods(){
        return ShopService.buyGoods(strList.get(1),intList.get(0),intList.get(1));
    }
}
