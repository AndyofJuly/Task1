package com.game.service.assist;

import com.game.entity.Role;
import com.game.service.IPackageService;
import com.game.service.impl.PackageServiceImpl;

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
    private IPackageService iPackageService = new PackageServiceImpl();

    public CountDownTime(Timer timer,int goodsId,Role offerRole) {
        this.timer = timer;
        this.goodsId = goodsId;
        this.offerRole = offerRole;
    }

    @Override
    public void run() {
        int lastBuyRoleId = offerRole.getPlayerSaleBo().getBuyRoleId();
        Role lastRole = GlobalInfo.getRoleHashMap().get(lastBuyRoleId);
        auctionSummary(goodsId,offerRole.getPlayerSaleBo().getLastPrice(),offerRole,lastRole);
        System.out.println("时间到，竞价结束");
        this.timer.cancel();
    }

    private void auctionSummary(int goodsId,int maxPrice,Role offerRole,Role lastRole){
        iPackageService.addMoney(maxPrice,offerRole);
        //packageService.lostMoney(maxPrice,lastRole);
        //退款
        HashMap<Integer, Integer> priceHashMap = offerRole.getPlayerSaleBo().getPriceHashMap();
        for(Role role : offerRole.getPlayerSaleBo().getRoleArrayList()){
            System.out.println("现在的钱"+role.getMoney());
            if(role.getId()!=lastRole.getId()){
                iPackageService.addMoney(priceHashMap.get(role.getId()),role);
            }
        }
        iPackageService.putIntoPackage(goodsId,1,lastRole);
        offerRole.getPlayerSaleBo().setPriceHashMap(null);
    }
}
