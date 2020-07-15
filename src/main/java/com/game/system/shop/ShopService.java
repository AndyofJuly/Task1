package com.game.system.shop;

import com.game.netty.server.ServerHandler;
import com.game.system.achievement.observer.FsTradeOB;
import com.game.system.achievement.subject.FsTradeSB;
import com.game.system.assist.AssistService;
import com.game.system.bag.PackageService;
import com.game.common.Const;
import com.game.system.role.pojo.Role;
import com.game.system.bag.pojo.EquipmentResource;
import com.game.system.bag.pojo.PotionResource;
import com.game.system.shop.pojo.DealBo;
import com.game.system.shop.pojo.AuctionBo;
import com.game.system.assist.GlobalInfo;
import com.game.system.shop.pojo.PlayerSaleBo;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;
import java.util.UUID;

/**
 * @Author andy
 * @create 2020/6/15 15:00
 */
public class ShopService {

    private RecordDao recordDao = new RecordDao();
    private PackageService packageService = new PackageService();
    //角色id-封装好的出售物品
    private static HashMap<Integer, PlayerSaleBo> playerSaleBoHashMap = new HashMap<>();

    /**
     * 购买商店物品
     * @param goodsId 物品id
     * @param number 数量
     * @param role 角色
     * @return String
     */
    public String buyGoods(int goodsId,int number,Role role){
        //限购查询
        if(!ifLimitBuy(goodsId,number,role.getId())){
            return Const.Shop.LIMIT_MSG;
        }
        int cost = AssistService.getGoodsPrice(goodsId) * number;
        if(!ifCanbuy(role,cost)){
            return Const.Shop.BUY_FAILURE;
        }else if(packageService.putIntoPackage(goodsId,number,role)==false){
            return Const.Shop.BAG_OUT_SPACE;
        }
        packageService.lostMoney(cost,role);
        return Const.Shop.BUY_SUCCESS+ role.getMyPackageBo().getGoodsHashMap().get(goodsId);
    }

    //限购查询
    private boolean ifLimitBuy(int goodsId,int number,int roleId){
        HashMap<Integer,HashMap<Integer,Integer>> buyRecord = new HashMap<>();
        buyRecord = recordDao.selectBuyRecord(roleId);
        if(buyRecord.get(roleId).get(goodsId)==null){
            buyRecord.get(roleId).put(goodsId,0);
        }
        int sumNumber = number + buyRecord.get(roleId).get(goodsId);
        if(sumNumber> Const.GOODS_BUG_MAX){
            return false;
        }else {
            recordDao.insertBuyRecord(roleId,goodsId,sumNumber);
        }
        return true;
    }

    //购买时金钱是否足够
    private boolean ifCanbuy(Role role,int cost){
        if(role.getMoney()<cost){
            return false;
        }
        return true;
    }

    /**
     * 面对面交易-发起阶段
     * @param targetId 交易对方id
     * @param equipId 装备id，默认数量1
     * @param potionId 药品id
     * @param num 药品数量
     * @param price 补价
     * @param role 角色
     * @return String
     */
    public static String dealWithOne(int targetId,int equipId,int potionId,int num,int price,Role role){
        String dealId = UUID.randomUUID().toString();
        role.setDealBo(new DealBo(dealId,targetId,equipId,potionId,num,price,role.getId()));
        //发起交易后，物品应该锁定，如果对方不同意，则交易失败，再解锁
        lockGoods(equipId,potionId,num,price,role);
        String string = "发起了交易，装备:"+equipId+
                "；药品："+potionId+"，数量："+num+"，补价："+price;
        return string;
    }

    /**
     * 面对面交易金钱和物品，一方同意后等待另一方同意，都同意后，双方物品和金钱进行交换
     * @param targetId 交易对方id
     * @param result 1同意或者0不同意
     * @param role 角色
     * @return String
     */
    public void tradeWithPlayer(int targetId,int result,Role role){//由发起者同意
        Role targetRole = GlobalInfo.getRoleHashMap().get(targetId);
        //拒绝交易
        if(result==0){
            unlockGoods(targetRole.getDealBo().getEquipmentId(),targetRole.getDealBo().getPotionId(),
                    targetRole.getDealBo().getNum(),targetRole.getDealBo().getPrice(),role);
            unlockGoods(role.getDealBo().getEquipmentId(),role.getDealBo().getPotionId(),
                    role.getDealBo().getNum(),role.getDealBo().getPrice(),role);
            role.setDealBo(null);
            targetRole.setDealBo(null);
            ServerHandler.notifyTrade(targetId,role.getName()+"拒绝了交易",role);
            return;
        }
        //同意交易
        role.getDealBo().setAgree(true);
        //通过判断二者的isAgree属性，确定是否都同意，如果都同意，则进行交易
        if(targetRole.getDealBo().isAgree()==true){
            //将双方锁定的物品进行交换
            exChangeGoods(targetRole,role);
            exChangeGoods(role,targetRole);
            //通知成就模块
            FsTradeSB.notifyObservers(Const.achieve.TASK_FRIST_TRADE,role);
            FsTradeSB.notifyObservers(Const.achieve.TASK_FRIST_TRADE,targetRole);
            ServerHandler.notifyTrade(targetId,"交易双方都已同意，交易成功",role);
            return;
        }
        ServerHandler.notifyTrade(targetId,role.getName()+"已同意，正等待"+targetRole.getName()+"同意",role);
    }

    private static void lockGoods(int equipId,int potionId,int num,int price,Role role){
        role.getGoodsLockMap().put(equipId,1);
        role.getGoodsLockMap().put(potionId,num);
        PackageService packageService = new PackageService();
        packageService.getFromPackage(equipId,1,role);
        packageService.getFromPackage(potionId,1,role);
        packageService.lostMoney(price,role);
    }

    private static void unlockGoods(int equipId,int potionId,int num,int price,Role role){
        PackageService packageService = new PackageService();
        packageService.putIntoPackage(equipId,1,role);
        packageService.putIntoPackage(potionId,num,role);
        packageService.addMoney(price,role);
        role.getGoodsLockMap().clear();
    }

    // 对方，药品数量，角色
    private void exChangeGoods(Role targetRole,Role role){
        //自己的背包获得交易物品，同时将对方锁定清空
        packageService.putIntoPackage(targetRole.getDealBo().getEquipmentId(),1,role);
        packageService.putIntoPackage(targetRole.getDealBo().getPotionId(),targetRole.getDealBo().getNum(),role);
        packageService.addMoney(targetRole.getDealBo().getPrice(),role);
        targetRole.getGoodsLockMap().clear();
        targetRole.setDealBo(null);
    }

    /**
     * 一口价出售
     * @param goodsId 物品id
     * @param price 甩卖价格
     * @param role 角色
     */
    //saleToPlayer  goods price id  将该商品上架到店中，给其他玩家购买
    public void saleByStore(int goodsId,int price,int num,Role role){
        //打包成PlayerSaleBo，放到店中
        PlayerSaleBo playerSaleBo = new PlayerSaleBo(goodsId,price,num);
        playerSaleBoHashMap.put(role.getId(),playerSaleBo);
        packageService.getFromPackage(goodsId,num,role);
    }

    // 提供撤销已上架物品的方法
    public String undoSale(Role role){
        if(playerSaleBoHashMap.get(role.getId())==null){
            return "无可撤销";
        }
        int goodsId = playerSaleBoHashMap.get(role.getId()).getGoodsId();
        int num = playerSaleBoHashMap.get(role.getId()).getNum();
        packageService.putIntoPackage(goodsId,num,role);
        playerSaleBoHashMap.remove(role.getId());
        return "撤销上架成功";
    }

    //获取玩家售卖列表的方法
    public String getPlayerSaleList(){
        StringBuilder stringBuilder = new StringBuilder("玩家上架的物品有：\n");
        for(Integer key : playerSaleBoHashMap.keySet()){
            stringBuilder.append("出售者:"+key+
                    "，物品："+playerSaleBoHashMap.get(key).getGoodsId()+
                    "，价格："+playerSaleBoHashMap.get(key).getPrice()+
                    "，数量："+ playerSaleBoHashMap.get(key).getNum()+"\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 购买一口价物品，在商品列表中购买
     * @param goodsId 物品id
     * @param buyNum 购买数量
     * @param offerId 出售人id
     * @param role 角色
     */
    public void buyFromePlayer(int goodsId,int buyNum,int offerId,Role role){
        int price = playerSaleBoHashMap.get(offerId).getPrice();
        int saleNum = playerSaleBoHashMap.get(offerId).getNum();
        if(buyNum>saleNum){return;}
        packageService.putIntoPackage(goodsId,buyNum,role);
        packageService.lostMoney(price*buyNum,role);
        packageService.addMoney(price*buyNum, GlobalInfo.getRoleHashMap().get(offerId));
        playerSaleBoHashMap.get(offerId).setNum(saleNum-buyNum);
        ServerHandler.talkWithOne(offerId,"上架的物品被购买了",role);
    }

    /**
     * 拍卖某件物品，默认只拍卖一件物品
     * @param goodsId 物品id
     * @param minPrice 拍卖底价
     * @param role 角色
     */
    public void auctionSale(int goodsId,int minPrice,Role role){
        //需要再拍卖场进行拍卖-(还可以预先专门确定参与拍卖的人)
        if(role.getNowScenesId()!=Const.AUCTION_SCENE){
            System.out.println("需要移动到拍卖场进行拍卖-测试");
            return;
        }
        String auctionId = UUID.randomUUID().toString();
        AuctionBo auctionBo = new AuctionBo(auctionId,goodsId,minPrice,role.getId());
        role.setAuctionBo(auctionBo);
        role.getAuctionBo().setTagTime(Instant.now());
        countdown(goodsId,role);
        ServerHandler.notifySceneGroup(Const.AUCTION_SCENE,role.getName()+"已经开始拍卖了，物品"+goodsId+"，起价："+minPrice);
        packageService.getFromPackage(goodsId,1,role);
    }

    /**
     * 竞价购买某物品
     * @param goodsId 物品id
     * @param price 竞价价格
     * @param offerId 拍卖人id
     * @param role 角色
     * @return String
     */
    public String auctionBuy(int goodsId,int price,int offerId,Role role){
        Role offerRole = GlobalInfo.getRoleHashMap().get(offerId);

        //判断是否能参与拍卖
        if(role.getNowScenesId()!=Const.AUCTION_SCENE){return "需要移动到拍卖场进行拍卖";}//需要再拍卖场进行拍卖
        if(offerRole.getAuctionBo().isIfEnding()){return "拍卖已经结束，不可以再进行操作";}
        if(price<=offerRole.getAuctionBo().getLastPrice()){return "请出更高的价格！";}

        //资金冻结
        lockMoney(price,offerRole,role);
        offerRole.getAuctionBo().getRoleArrayList().add(role);
        offerRole.getAuctionBo().setBuyRoleId(role.getId());
        offerRole.getAuctionBo().setLastPrice(price);

        //通知其他玩家
        ServerHandler.notifyAuctionGroup(role,role.getNowScenesId(),offerRole.getAuctionBo().getLastPrice());
        return "出价为"+offerRole.getAuctionBo().getLastPrice();
    }

    private void lockMoney(int price,Role offerRole,Role role){
        HashMap<Integer, Integer> priceHashMap = offerRole.getAuctionBo().getPriceHashMap();
        //没有记录，先扣出价钱，如果已经有记录，只失去多出来的部分，最后竞价失败的人统一收到退款
        if(priceHashMap.get(role.getId())==null){
            packageService.lostMoney(price,role);
        }else{
            packageService.lostMoney(price-priceHashMap.get(role.getId()),role);
        }
        priceHashMap.put(role.getId(),price);
    }

    private void countdown(int goodsId,Role offerRole){
        Timer timer = new Timer();
        CountDownTime countDownTime = new CountDownTime(timer,goodsId,offerRole);
        timer.schedule(countDownTime, Const.AUCTION_TIME, Const.AUCTION_TIME);
    }

    /**
     * 获取商店物品列表
     * @return String
     */
    //商店列表初始化
    public String getStaticGoodsList(){
        StringBuilder stringBuilder = new StringBuilder("欢迎光临本商店，商店提供： ");
        for(Integer key : EquipmentResource.getEquipmentStaticHashMap().keySet()){
            stringBuilder.append(AssistService.getEquipmentName(key)+":"+
                    AssistService.getEquipmentPrice(key)).append("银； ");
        }
        for(Integer key : PotionResource.getPotionStaticHashMap().keySet()){
            stringBuilder.append(AssistService.getPotionName(key)+":"+
                    AssistService.getPotionPrice(key)).append("银； ");
        }
        return stringBuilder.toString();
    }

    static {
        FsTradeSB.registerObserver(new FsTradeOB());
    }
}
