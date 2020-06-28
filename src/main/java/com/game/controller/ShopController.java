package com.game.controller;

import com.game.entity.Role;
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

    private ArrayList<String> strList = RoleController.getStrList();
    private ArrayList<Integer> intList = RoleController.getIntList();

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

    //玩家面对面交易，使用举例：trade yes/no
    @MyAnnontation(MethodName = "trade")
    public String tradeWithPlayer(){
        if(ShopService.tradeWithPlayer(strList.get(1),intList.get(0))){
            return "交易成功";
        }
        return "对方拒绝交易";
    }

    //一口价交易-卖，玩家将商品和价格寄托在商店，商店代卖，使用举例：sale goodsId price (roleId)
    @MyAnnontation(MethodName = "sale")
    public String saleToPlayer(){
        ShopService.saleToPlayer(intList.get(0),intList.get(1),intList.get(2));
        return "上架成功";
    }

    //可以编写获取所有玩家寄卖的物品的列表方法

    //一口价交易-买，使用举例：buyR goodsId price offerId (roleId)
    @MyAnnontation(MethodName = "buyR")
    public String buyFromePlayer(){
        ShopService.buyFromePlayer(intList.get(0),intList.get(1),intList.get(2),intList.get(3));
        return "交易成功";
    }

    //拍卖-卖方，使用举例：auctionSale goodsId minPrice (roleId)
    @MyAnnontation(MethodName = "aucSale")
    public String auctionSale(){
        ShopService.auctionSale(intList.get(0),intList.get(1),intList.get(2));
        return "开始拍卖";
    }

    //拍卖-买方，使用举例：auctionBuy goodsId price offerId (roleId)
    @MyAnnontation(MethodName = "aucBuy")
    public String auctionBuy(){
        return ShopService.auctionBuy(intList.get(0),intList.get(1),intList.get(2),intList.get(3));
    }
}
