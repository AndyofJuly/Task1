package com.game.controller;

import com.game.entity.Role;
import com.game.service.ShopService;
import com.game.service.assis.GlobalResource;
import com.game.service.assis.InitGame;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import com.game.common.MyAnnontation;

import java.util.ArrayList;

/**
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Controller
public class ShopController {

    private ArrayList<Integer> intList = RoleController.getIntList();
    private ShopService shopService = new ShopService();

    //获得商店的商品列表，使用举例：getGoodsList
    @MyAnnontation(MethodName = "getGoodsList")
    public String getGoodsList(){
        return shopService.getStaticGoodsList();
    }

    //购买药品or装备，使用举例：buy 清泉酒 20 ：here修改成goodsId
    @MyAnnontation(MethodName = "buy")
    public String buyGoods(){
        return shopService.buyGoods(intList.get(0),intList.get(1),getRole());
    }

    //玩家面对面交易，使用举例：trade targetRole yes/no ：here修改成1/0:  trade 16 1/ trade 1 0
    @MyAnnontation(MethodName = "trade")
    public String tradeWithPlayer(){
        return shopService.tradeWithPlayer(intList.get(0),intList.get(1),getRole());
    }

    //一口价交易-卖，玩家将商品和价格寄托在商店，商店代卖，使用举例：sale goodsId price (roleId)
    @MyAnnontation(MethodName = "sale")
    public String saleToPlayer(){
        shopService.saleToPlayer(intList.get(0),intList.get(1),getRole());
        return "上架成功";
    }

    //可以编写获取所有玩家寄卖的物品的列表方法

    //一口价交易-买，使用举例：buyR goodsId price offerId (roleId)
    @MyAnnontation(MethodName = "buyR")
    public String buyFromePlayer(){
        shopService.buyFromePlayer(intList.get(0),intList.get(1),intList.get(2),getRole());
        return "交易成功";
    }

    //拍卖-卖方，使用举例：auctionSale goodsId minPrice (roleId)
    @MyAnnontation(MethodName = "aucSale")
    public String auctionSale(){
        shopService.auctionSale(intList.get(0),intList.get(1),getRole());
        return "开始拍卖";
    }

    //拍卖-买方，使用举例：auctionBuy goodsId price offerId (roleId)
    @MyAnnontation(MethodName = "aucBuy")
    public String auctionBuy(){
        return shopService.auctionBuy(intList.get(0),intList.get(1),intList.get(2),getRole());
    }

    //获得角色，适用于输入参数最后一位为roleId的情况
    public Role getRole(){
        return GlobalResource.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
