package com.game.system.shop;

import com.game.common.Const;
import com.game.netty.server.ServerHandler;
import com.game.system.assist.GlobalInfo;
import com.game.system.role.pojo.Role;
import com.game.system.bag.PackageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 拍卖倒计时，使用Timer定时器
 * @Author andy
 * @create 2020/7/6 11:09
 */
public class CountDownTime extends TimerTask {

    private Timer timer;
    private int goodsId;
    private Role offerRole;
    private PackageService packageService = new PackageService();

    public CountDownTime(Timer timer,int goodsId,Role offerRole) {
        this.timer = timer;
        this.goodsId = goodsId;
        this.offerRole = offerRole;
    }

    //定时器倒计时结束后执行，即拍卖结束
    @Override
    public void run() {
        int lastBuyRoleId = offerRole.getAuctionBo().getBuyRoleId();
        Role lastRole = GlobalInfo.getRoleHashMap().get(lastBuyRoleId);
        auctionSummary(goodsId,offerRole.getAuctionBo().getLastPrice(),offerRole,lastRole);

        ArrayList<Role> roles = GlobalInfo.getScenes().get(Const.AUCTION_SCENE).getRoleAll();
        ServerHandler.notifyGroupRoles(roles,"时间到，拍卖结束！");
        offerRole.getAuctionBo().setIfEnding(true);
        this.timer.cancel();
    }

    /**
     * 拍卖结束时结算
     * @param goodsId 物品id
     * @param maxPrice 最终定价
     * @param offerRole 物品出售者
     * @param lastRole 物品出价购买者
     */
    private void auctionSummary(int goodsId,int maxPrice,Role offerRole,Role lastRole){
        packageService.addMoney(maxPrice,offerRole);

        //退款
        HashMap<Integer, Integer> priceHashMap = offerRole.getAuctionBo().getPriceHashMap();
        for(Role role : offerRole.getAuctionBo().getRoleArrayList()){
            if(!role.getId().equals(lastRole.getId())){
                packageService.addMoney(priceHashMap.get(role.getId()),role);
            }
        }
        packageService.putIntoPackage(goodsId,1,lastRole);
        offerRole.getAuctionBo().setPriceHashMap(null);
    }
}
