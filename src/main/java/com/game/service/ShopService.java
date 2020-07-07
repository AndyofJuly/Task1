package com.game.service;

import com.game.common.Const;
import com.game.dao.RecordMapper;
import com.game.entity.Role;
import com.game.entity.store.EquipmentResource;
import com.game.entity.store.PotionResource;
import com.game.entity.vo.PlayerSaleVo;
import com.game.service.assis.AssistService;
import com.game.service.assis.BossAttack;
import com.game.service.assis.CountDownTime;
import com.game.service.assis.GlobalResource;
import com.game.service.helper.EquipmentHelper;
import com.game.service.helper.PotionHelper;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.UUID;

/**
 * @Author andy
 * @create 2020/6/15 15:00
 */
public class ShopService {

    private RecordMapper recordMapper = new RecordMapper();
    private PackageService packageService = new PackageService();
    //public static ArrayList<PlayerSaleVo> saleArrayList = new ArrayList<>();
    //public static ArrayList<PlayerSaleVo> auctionArrayList = new ArrayList<>();
    //竞价记录，roleId-price
    //public static HashMap<Integer,Integer> priceHashMap = new HashMap<>();

    // here
    public String buyGoods(int goodsId,int number,Role role){
        //药品的情况
        if(String.valueOf(goodsId).startsWith(Const.POTION_HEAD)){
            if(!ifLimitBuy(goodsId,number,role.getId())){
                return Const.Shop.LIMIT_MSG;
            }
            int cost = PotionHelper.getPotionPrice(goodsId) *number;
            if(!ifCanbuy(role,cost)){
                return Const.Shop.BUY_FAILURE;
            }else if(packageService.putIntoPackage(goodsId,number,role)==false){
                return Const.Shop.BAG_OUT_SPACE;
            }

            packageService.lostMoney(cost,role);
            return Const.Shop.BUY_SUCCESS+ role.getMyPackage().getGoodsHashMap().get(goodsId);
        }else {  //装备的情况
            int cost = EquipmentHelper.getEquipmentPrice(goodsId)*number;
            if(!ifCanbuy(role,cost)){
                return Const.Shop.BUY_FAILURE;
            }else if(packageService.putIntoPackage(goodsId,number,role)==false){
                return Const.Shop.BAG_OUT_SPACE;
            }
            packageService.lostMoney(cost,role);
            return Const.Shop.BUY_SUCCESS+ role.getMyPackage().getGoodsHashMap().get(goodsId);
        }
    }

    //限购
    public boolean ifLimitBuy(int goodsId,int number,int roleId){
        HashMap<Integer,HashMap<Integer,Integer>> buyRecord = new HashMap<>();
        buyRecord = recordMapper.selectBuyRecord(roleId);
        if(buyRecord.get(roleId).get(goodsId)==null){
            buyRecord.get(roleId).put(goodsId,0);
        }
        int sumNumber = number + buyRecord.get(roleId).get(goodsId);
        if(sumNumber> Const.GOODS_BUG_MAX){
            return false;
        }else {
            recordMapper.insertBuyRecord(roleId,goodsId,sumNumber);
        }
        return true;
    }

    public boolean ifCanbuy(Role role,int cost){
        if(role.getMoney()<cost){
            return false;
        }
        return true;
    }

    //目标角色-是否同意-自己
    public String tradeWithPlayer(int targetId,int result,Role role){//由发起者同意
        Role targetRole = GlobalResource.getRoleHashMap().get(targetId);
        if(result==0){
            role.setDealVo(null);
            targetRole.setDealVo(null);
            return "对方拒绝交易";
        }
        role.getDealVo().setAgree(true);
        //通过判断二者的isAgree属性，确定是否都同意，如果都同意，则进行交易
        if(targetRole.getDealVo().isAgree()==true){
            //对应装备、药品、钱变少-可放在deal中
            exChangeLost(targetRole,role);
            //交易，DealVo中的物品和金钱互换
            exChangeGet(targetRole,role);
            role.setDealVo(null);
            targetRole.setDealVo(null);
            return "交易成功！";
        }
        return "等待对方同意";
    }

    private void exChangeLost(Role targetRole,Role role){
        packageService.lostGoods(role.getDealVo().getEquipmentId(),role);
        packageService.lostGoods(role.getDealVo().getPotionId(),role);
        packageService.lostMoney(role.getDealVo().getPrice(),role);

        packageService.lostGoods(targetRole.getDealVo().getEquipmentId(),targetRole);
        packageService.lostGoods(targetRole.getDealVo().getPotionId(),targetRole);
        packageService.lostMoney(targetRole.getDealVo().getPrice(),targetRole);
    }

    private void exChangeGet(Role targetRole,Role role){
        packageService.putIntoPackage(targetRole.getDealVo().getEquipmentId(),1,role);
        packageService.putIntoPackage(targetRole.getDealVo().getPotionId(),1,role);
        packageService.addMoney(targetRole.getDealVo().getPrice(),role);

        packageService.putIntoPackage(role.getDealVo().getEquipmentId(),1,targetRole);
        packageService.putIntoPackage(role.getDealVo().getPotionId(),1,targetRole);
        packageService.addMoney(role.getDealVo().getPrice(),targetRole);
    }

    //saleToPlayer  goods price id  将该商品上架到店中
    public void saleToPlayer(int goodsId,int price,Role role){
        //商店列表中增加一个集合，存放玩家放在商店卖的所有信息，并将该新信息添加到集合中，包括提供的角色id，物品id和价格
        //其他玩家可以通过查看该集合组成的列表来获得目前的一口价商品信息
        //saleArrayList.add(new PlayerSaleVo(goodsId,price,role.getId()));
        packageService.lostGoods(goodsId,role);//可提供撤销上架的方法
    }

    //在商品列表中购买，购买后商品进行下架
    public void buyFromePlayer(int goodsId,int price,int offerId,Role role){
        packageService.putIntoPackage(goodsId,1,role);
        packageService.lostMoney(price,role);
        packageService.addMoney(price,GlobalResource.getRoleHashMap().get(offerId));
    }

    public void auctionSale(int goodsId,int minPrice,Role role){
        String auctionId = UUID.randomUUID().toString();
        PlayerSaleVo playerSaleVo = new PlayerSaleVo(auctionId,goodsId,minPrice,role.getId());
        //auctionArrayList.add(playerSaleVo);//存储拍卖行所有拍卖的东西
        role.setPlayerSaleVo(playerSaleVo);
        role.getPlayerSaleVo().setTagTime(Instant.now());
        countdown(goodsId,role);
        packageService.lostGoods(goodsId,role);
    }

    public String auctionBuy(int goodsId,int price,int offerId,Role role){
        Role offerRole = GlobalResource.getRoleHashMap().get(offerId);//提供者
        HashMap<Integer, Integer> priceHashMap = offerRole.getPlayerSaleVo().getPriceHashMap();
        //没有记录，先扣出价钱，如果已经有记录，只失去多出来的部分，最后竞价失败的人统一收到退款
        if(priceHashMap.get(role.getId())==null){
            packageService.lostMoney(price,role);
        }else{
            packageService.lostMoney(price-priceHashMap.get(role.getId()),role);
        }
        priceHashMap.put(role.getId(),price);
        System.out.println("现在的钱"+role.getMoney());
        Duration between = Duration.between(offerRole.getPlayerSaleVo().getTagTime(), Instant.now());
        long l = between.toMillis()/Const.TO_MS; // 拍卖已进行的时间
        offerRole.getPlayerSaleVo().setBuyRoleId(role.getId());
        offerRole.getPlayerSaleVo().setLastPrice(price);
        offerRole.getPlayerSaleVo().getRoleArrayList().add(role);
        long restTime = Const.AUCTION_TIME-l;
        return "出价为"+offerRole.getPlayerSaleVo().getLastPrice()+"，剩余时间："+restTime;//+"，剩余时间："+restTime
    }

    public void countdown(int goodsId,Role offerRole){
        Timer timer = new Timer();
        CountDownTime countDownTime = new CountDownTime(timer,goodsId,offerRole);
        timer.schedule(countDownTime, Const.AUCTION_TIME, Const.AUCTION_TIME);
    }

    //商店列表初始化
    public String getStaticGoodsList(){
        StringBuilder stringBuilder = new StringBuilder("欢迎光临本商店，商店提供： ");
        for(Integer key : EquipmentResource.getEquipmentStaticHashMap().keySet()){
            stringBuilder.append(EquipmentHelper.getEquipmentName(key)+":"+
                    EquipmentHelper.getEquipmentPrice(key)).append("银； ");
        }
        for(Integer key : PotionResource.getPotionStaticHashMap().keySet()){
            stringBuilder.append(PotionHelper.getPotionName(key)+":"+
                    PotionHelper.getPotionPrice(key)).append("银； ");
        }
        return stringBuilder.toString();
    }
}
