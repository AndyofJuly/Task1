package com.game.service.impl;

import com.game.common.Const;
import com.game.dao.IRecordDao;
import com.game.dao.impl.RecordDaoImpl;
import com.game.entity.Role;
import com.game.entity.store.EquipmentResource;
import com.game.entity.store.PotionResource;
import com.game.entity.bo.PlayerSaleBo;
import com.game.service.IPackageService;
import com.game.service.IShopService;
import com.game.service.assist.CountDownTime;
import com.game.service.assist.GlobalInfo;
import com.game.service.assist.ResourceSearch;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;
import java.util.UUID;

/**
 * @Author andy
 * @create 2020/6/15 15:00
 */
public class ShopServiceImpl implements IShopService {

    private IRecordDao iRecordDao = new RecordDaoImpl();
    private IPackageService iPackageService = new PackageServiceImpl();

    @Override
    public String buyGoods(int goodsId,int number,Role role){
        //药品的情况
        if(String.valueOf(goodsId).startsWith(Const.POTION_HEAD)){
            if(!ifLimitBuy(goodsId,number,role.getId())){
                return Const.Shop.LIMIT_MSG;
            }
            int cost = ResourceSearch.getPotionPrice(goodsId) *number;
            if(!ifCanbuy(role,cost)){
                return Const.Shop.BUY_FAILURE;
            }else if(iPackageService.putIntoPackage(goodsId,number,role)==false){
                return Const.Shop.BAG_OUT_SPACE;
            }

            iPackageService.lostMoney(cost,role);
            return Const.Shop.BUY_SUCCESS+ role.getMyPackageBo().getGoodsHashMap().get(goodsId);
        }else {  //装备的情况
            int cost = ResourceSearch.getEquipmentPrice(goodsId)*number;
            if(!ifCanbuy(role,cost)){
                return Const.Shop.BUY_FAILURE;
            }else if(iPackageService.putIntoPackage(goodsId,number,role)==false){
                return Const.Shop.BAG_OUT_SPACE;
            }
            iPackageService.lostMoney(cost,role);
            return Const.Shop.BUY_SUCCESS+ role.getMyPackageBo().getGoodsHashMap().get(goodsId);
        }
    }

    //限购
    private boolean ifLimitBuy(int goodsId,int number,int roleId){
        HashMap<Integer,HashMap<Integer,Integer>> buyRecord = new HashMap<>();
        buyRecord = iRecordDao.selectBuyRecord(roleId);
        if(buyRecord.get(roleId).get(goodsId)==null){
            buyRecord.get(roleId).put(goodsId,0);
        }
        int sumNumber = number + buyRecord.get(roleId).get(goodsId);
        if(sumNumber> Const.GOODS_BUG_MAX){
            return false;
        }else {
            iRecordDao.insertBuyRecord(roleId,goodsId,sumNumber);
        }
        return true;
    }

    private boolean ifCanbuy(Role role,int cost){
        if(role.getMoney()<cost){
            return false;
        }
        return true;
    }

    //目标角色-是否同意-自己
    @Override
    public String tradeWithPlayer(int targetId,int result,Role role){//由发起者同意
        Role targetRole = GlobalInfo.getRoleHashMap().get(targetId);
        if(result==0){
            role.setDealBo(null);
            targetRole.setDealBo(null);
            return "对方拒绝交易";
        }
        role.getDealBo().setAgree(true);
        //通过判断二者的isAgree属性，确定是否都同意，如果都同意，则进行交易
        if(targetRole.getDealBo().isAgree()==true){
            //对应装备、药品、钱变少-可放在deal中
            exChangeLost(targetRole,role);
            //交易，DealVo中的物品和金钱互换
            exChangeGet(targetRole,role);
            role.setDealBo(null);
            targetRole.setDealBo(null);
            return "交易成功！";
        }
        return "等待对方同意";
    }

    private void exChangeLost(Role targetRole,Role role){
        iPackageService.lostGoods(role.getDealBo().getEquipmentId(),role);
        iPackageService.lostGoods(role.getDealBo().getPotionId(),role);
        iPackageService.lostMoney(role.getDealBo().getPrice(),role);

        iPackageService.lostGoods(targetRole.getDealBo().getEquipmentId(),targetRole);
        iPackageService.lostGoods(targetRole.getDealBo().getPotionId(),targetRole);
        iPackageService.lostMoney(targetRole.getDealBo().getPrice(),targetRole);
    }

    private void exChangeGet(Role targetRole,Role role){
        iPackageService.putIntoPackage(targetRole.getDealBo().getEquipmentId(),1,role);
        iPackageService.putIntoPackage(targetRole.getDealBo().getPotionId(),1,role);
        iPackageService.addMoney(targetRole.getDealBo().getPrice(),role);

        iPackageService.putIntoPackage(role.getDealBo().getEquipmentId(),1,targetRole);
        iPackageService.putIntoPackage(role.getDealBo().getPotionId(),1,targetRole);
        iPackageService.addMoney(role.getDealBo().getPrice(),targetRole);
    }

    //saleToPlayer  goods price id  将该商品上架到店中
    @Override
    public void saleToPlayer(int goodsId,int price,Role role){
        //商店列表中增加一个集合，存放玩家放在商店卖的所有信息，并将该新信息添加到集合中，包括提供的角色id，物品id和价格
        //其他玩家可以通过查看该集合组成的列表来获得目前的一口价商品信息
        iPackageService.lostGoods(goodsId,role);//可提供撤销上架的方法
    }

    //获取玩家售卖列表的方法

    //在商品列表中购买，购买后商品进行下架
    @Override
    public void buyFromePlayer(int goodsId,int price,int offerId,Role role){
        iPackageService.putIntoPackage(goodsId,1,role);
        iPackageService.lostMoney(price,role);
        iPackageService.addMoney(price, GlobalInfo.getRoleHashMap().get(offerId));
    }

    @Override
    public void auctionSale(int goodsId,int minPrice,Role role){
        String auctionId = UUID.randomUUID().toString();
        PlayerSaleBo playerSaleBo = new PlayerSaleBo(auctionId,goodsId,minPrice,role.getId());
        role.setPlayerSaleBo(playerSaleBo);
        role.getPlayerSaleBo().setTagTime(Instant.now());
        countdown(goodsId,role);
        iPackageService.lostGoods(goodsId,role);
    }

    @Override
    public String auctionBuy(int goodsId,int price,int offerId,Role role){
        Role offerRole = GlobalInfo.getRoleHashMap().get(offerId);//提供者
        HashMap<Integer, Integer> priceHashMap = offerRole.getPlayerSaleBo().getPriceHashMap();
        //没有记录，先扣出价钱，如果已经有记录，只失去多出来的部分，最后竞价失败的人统一收到退款
        if(priceHashMap.get(role.getId())==null){
            iPackageService.lostMoney(price,role);
        }else{
            iPackageService.lostMoney(price-priceHashMap.get(role.getId()),role);
        }
        priceHashMap.put(role.getId(),price);
        Duration between = Duration.between(offerRole.getPlayerSaleBo().getTagTime(), Instant.now());
        long l = between.toMillis()/Const.TO_MS; // 拍卖已进行的时间
        offerRole.getPlayerSaleBo().setBuyRoleId(role.getId());
        offerRole.getPlayerSaleBo().setLastPrice(price);
        offerRole.getPlayerSaleBo().getRoleArrayList().add(role);
        long restTime = Const.AUCTION_TIME/1000-l;
        return "出价为"+offerRole.getPlayerSaleBo().getLastPrice()+"，剩余时间："+restTime;//+"，剩余时间："+restTime
    }

    private void countdown(int goodsId,Role offerRole){
        Timer timer = new Timer();
        CountDownTime countDownTime = new CountDownTime(timer,goodsId,offerRole);
        timer.schedule(countDownTime, Const.AUCTION_TIME, Const.AUCTION_TIME);
    }

    //商店列表初始化
    @Override
    public String getStaticGoodsList(){
        StringBuilder stringBuilder = new StringBuilder("欢迎光临本商店，商店提供： ");
        for(Integer key : EquipmentResource.getEquipmentStaticHashMap().keySet()){
            stringBuilder.append(ResourceSearch.getEquipmentName(key)+":"+
                    ResourceSearch.getEquipmentPrice(key)).append("银； ");
        }
        for(Integer key : PotionResource.getPotionStaticHashMap().keySet()){
            stringBuilder.append(ResourceSearch.getPotionName(key)+":"+
                    ResourceSearch.getPotionPrice(key)).append("银； ");
        }
        return stringBuilder.toString();
    }
}
