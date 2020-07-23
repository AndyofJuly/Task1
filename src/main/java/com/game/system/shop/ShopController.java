package com.game.system.shop;

import com.game.netty.server.ServerHandler;
import com.game.system.gameserver.GameController;
import com.game.system.role.entity.Role;
import com.game.common.ResponseInf;
import com.game.system.gameserver.GlobalInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.game.common.MyAnnontation;

import java.util.ArrayList;

/**
 * 购物模块调用方法入口
 * @Author andy
 * @create 2020/6/15 15:23
 */
@Controller("shopController")
public class ShopController {

    private ArrayList<Integer> intList = GameController.getIntList();

    @Autowired
    private ShopService shopService;

    /** 获得商店的商品列表，使用方式：getGoodsList */
    @MyAnnontation(MethodName = "getGoodsList")
    public ResponseInf getGoodsList(){
        return ResponseInf.setResponse(shopService.getStaticGoodsList(),getRole());
    }

    /** 购买药品or装备，使用方式：buy goodsId num */
    @MyAnnontation(MethodName = "buy")
    public ResponseInf buyGoods(){
        String msg = shopService.buyGoods(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 玩家面对面交易，双方都需要同时发起，使用方式：deal targetRoleId equipmentId potionId num money */
    @MyAnnontation(MethodName = "deal")
    public ResponseInf dealWithOne(){
        String result = shopService.dealWithOne(intList.get(0),intList.get(1),intList.get(2),intList.get(3),intList.get(4),getRole());
        ServerHandler.notifyRole(intList.get(0),getRole().getName()+result,getRole().getId(),"");
        return ResponseInf.setResponse(result,getRole());
    }

    /** 玩家面对面交易，双方都可以选择同意本次交易或者拒绝，使用方式：trade targetRoleId yes (yes:1;no:0) */
    @MyAnnontation(MethodName = "trade")
    public ResponseInf tradeWithPlayer(){
        shopService.tradeWithPlayer(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse("",getRole());
    }

    /** 一口价交易-卖-玩家将商品和价格寄托在商店，商店代卖，使用方式：sale goodsId num price */
    @MyAnnontation(MethodName = "sale")
    public ResponseInf saleToPlayer(){
        String msg = shopService.saleByStore(intList.get(0),intList.get(1),intList.get(2),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 撤销上架在商店进行出售的物品，使用方式：undoSale */
    @MyAnnontation(MethodName = "undoSale")
    public ResponseInf undoSale(){
        return ResponseInf.setResponse(shopService.undoSale(getRole()),getRole());
    }

    /** 获取所有玩家寄卖的物品的列表，使用方式：roleSaleList */
    @MyAnnontation(MethodName = "roleSaleList")
    public ResponseInf getPlayerSaleList(){
        return ResponseInf.setResponse(shopService.getPlayerSaleList(),getRole());
    }

    /** 一口价交易-买-购买其他的玩家上架的物品，使用方式：buyR goodsId num offerId */
    @MyAnnontation(MethodName = "buyR")
    public ResponseInf buyFromePlayer(){
        shopService.buyFromePlayer(intList.get(0),intList.get(1),intList.get(2),getRole());
        return ResponseInf.setResponse("购买成功",getRole());
    }

    /** 拍卖-卖方使用，使用方式：auctionSale goodsId minPrice */
    @MyAnnontation(MethodName = "aucSale")
    public ResponseInf auctionSale(){
        String msg = shopService.auctionSale(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 拍卖-买方使用，使用方式：auctionBuy price offerId */
    @MyAnnontation(MethodName = "aucBuy")
    public ResponseInf auctionBuy(){
        String msg = shopService.auctionBuy(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 根据输入获得角色，输入参数最后一位为roleId */
    private Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
