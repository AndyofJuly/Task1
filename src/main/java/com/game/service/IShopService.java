package com.game.service;

import com.game.entity.Role;

/**
 * @Author andy
 * @create 2020/7/7 10:58
 */
public interface IShopService {

    /**
     * 购买商店物品
     * @param goodsId 物品id
     * @param number 数量
     * @param role 角色
     * @return String
     */
    String buyGoods(int goodsId, int number, Role role);

    /**
     * 面对面交易金钱和物品
     * @param targetId 交易对方id
     * @param result 1同意或者0不同意
     * @param role 角色
     * @return String
     */
    String tradeWithPlayer(int targetId, int result, Role role);

    /**
     * 一口价出售
     * @param goodsId 物品id
     * @param price 甩卖价格
     * @param role 角色
     */
    void saleToPlayer(int goodsId, int price, Role role);

    /**
     * 购买一口价物品
     * @param goodsId 物品id
     * @param price 价格
     * @param offerId 出售人id
     * @param role 角色
     */
    void buyFromePlayer(int goodsId, int price, int offerId, Role role);

    /**
     * 拍卖某件物品
     * @param goodsId 物品id
     * @param minPrice 拍卖底价
     * @param role 角色
     */
    void auctionSale(int goodsId, int minPrice, Role role);

    /**
     * 竞价购买某物品
     * @param goodsId 物品id
     * @param price 竞价价格
     * @param offerId 拍卖人id
     * @param role 角色
     * @return String
     */
    String auctionBuy(int goodsId, int price, int offerId, Role role);

    /**
     * 获取商店物品列表
     * @return String
     */
    String getStaticGoodsList();

}
