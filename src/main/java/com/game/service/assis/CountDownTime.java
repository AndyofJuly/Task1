package com.game.service.assis;

import com.game.common.Const;
import com.game.entity.Role;
import com.game.service.PackageService;
import com.game.service.ShopService;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author andy
 * @create 2020/7/6 11:09
 */
public class CountDownTime extends TimerTask {
    //只需执行一次，延时时间即为拍卖时间
    //定时器操作对象
    private Timer timer;
    private int goodsId;
    private Role offerRole;
    PackageService packageService = new PackageService();

    public CountDownTime(Timer timer,int goodsId,Role offerRole) {
        this.timer = timer;
        this.goodsId = goodsId;
        this.offerRole = offerRole;
    }

    @Override
    public void run() {
        int lastBuyRoleId = offerRole.getPlayerSaleVo().getBuyRoleId();
        Role lastRole = GlobalResource.getRoleHashMap().get(lastBuyRoleId);
        auctionSummary(goodsId,offerRole.getPlayerSaleVo().getLastPrice(),offerRole,lastRole);
        System.out.println("时间到，竞价结束");
        this.timer.cancel();
    }

    private void auctionSummary(int goodsId,int maxPrice,Role offerRole,Role lastRole){
        packageService.addMoney(maxPrice,offerRole);
        //packageService.lostMoney(maxPrice,lastRole);
        //退款
        HashMap<Integer, Integer> priceHashMap = offerRole.getPlayerSaleVo().getPriceHashMap();
        for(Role role : offerRole.getPlayerSaleVo().getRoleArrayList()){
            System.out.println("现在的钱"+role.getMoney());
            if(role.getId()!=lastRole.getId()){
                packageService.addMoney(priceHashMap.get(role.getId()),role);
            }
        }
        packageService.putIntoPackage(goodsId,1,lastRole);
        offerRole.getPlayerSaleVo().setPriceHashMap(null);
    }
}
