package com.game.system.shop;

import com.game.common.Const;
import com.game.netty.server.ServerHandler;
import com.game.system.achievement.observer.FsTradeOb;
import com.game.system.achievement.pojo.Subject;
import com.game.system.gameserver.AssistService;
import com.game.system.gameserver.GlobalInfo;
import com.game.system.bag.PackageService;
import com.game.system.bag.pojo.Equipment;
import com.game.system.bag.pojo.EquipmentResource;
import com.game.system.bag.pojo.PotionResource;
import com.game.system.role.pojo.Role;
import com.game.system.shop.pojo.AuctionBo;
import com.game.system.shop.pojo.DealBo;
import com.game.system.shop.pojo.PlayerSaleBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.UUID;

/**
 * 购物模块的业务逻辑处理
 * @Author andy
 * @create 2020/6/15 15:00
 */
@Service
public class ShopService {

/*    @Autowired
    private RecordDao recordDao;*/
    @Autowired
    private PackageService packageService;

    //商店仓库中角色上架的物品，key为角色id，value为出售物品和数量
    private static HashMap<Integer, PlayerSaleBo> playerSaleBoHashMap = new HashMap<>();

    /**
     * 购买商店物品
     * @param goodsId 物品id
     * @param number 数量
     * @param role 角色
     * @return 信息提示
     */
    public String buyGoods(int goodsId,int number,Role role){
/*        if(!ifLimitBuy(goodsId,number,role.getId())){
            return Const.Shop.LIMIT_MSG;
        }*/
        int cost = AssistService.getGoodsPrice(goodsId) * number;
        if(!packageService.lostMoney(cost,role)){
            return Const.Shop.BUY_FAILURE;
        }
        //检查是否为装备，如果是装备，则要有唯一id
        if(String.valueOf(goodsId).startsWith(Const.EQUIPMENT_HEAD)){
            int equipId = AssistService.generateEquipId();
            GlobalInfo.getEquipmentHashMap().put(equipId,new Equipment(equipId,goodsId,100));//默认耐久
            goodsId=equipId;
        }
        if(!packageService.putIntoPackage(goodsId, number, role)){
            return Const.Shop.BAG_OUT_SPACE;
        }
        return Const.Shop.BUY_SUCCESS+ role.getMyPackageBo().getGoodsHashMap().get(goodsId);
    }

    /**
     * 查询是否限购
     * @param goodsId 物品id
     * @param number 数量
     * @param roleId 角色id
     * @return 是否限购
     */
/*    private boolean ifLimitBuy(int goodsId,int number,int roleId){
        HashMap<Integer,HashMap<Integer,Integer>> buyRecord = recordDao.selectBuyRecord(roleId);
        buyRecord.get(roleId).putIfAbsent(goodsId, 0);
        int sumNumber = number + buyRecord.get(roleId).get(goodsId);
        if(sumNumber> Const.GOODS_BUG_MAX){
            return false;
        }else {
            recordDao.insertBuyRecord(roleId,goodsId,sumNumber);
        }
        return true;
    }*/

    /**
     * 面对面交易-双方发起阶段
     * @param targetId 交易对方id
     * @param equipId 装备id，默认数量1
     * @param potionId 药品id
     * @param num 药品数量
     * @param price 补价
     * @param role 角色
     * @return 信息提示
     */
    public String dealWithOne(int targetId,int equipId,int potionId,int num,int price,Role role){
        if(!packageService.lostMoney(price,role)){
            return "金钱不够";
        }
        if(!packageService.getFromPackage(equipId,1,role) || !packageService.getFromPackage(potionId,num,role)){
            return "背包中没有这么多物品";
        }
        String dealId = UUID.randomUUID().toString();
        role.setDealBo(new DealBo(dealId,targetId,equipId,potionId,num,price,role.getId()));
        //发起交易后，物品将被锁定；当一方拒绝交易后，才进行解锁
        lockGoods(equipId,potionId,num,role);
        return "发起了交易，装备:"+equipId+ "；药品："+potionId+"，数量："+num+"，补价："+price;
    }

    /**
     * 面对面交易金钱和物品，一方同意后等待另一方同意，都同意后，双方物品和金钱进行交换
     * @param targetId 交易对方id
     * @param result 1同意，0不同意
     * @param role 角色
     */
    public void tradeWithPlayer(int targetId,int result,Role role){
        Role targetRole = GlobalInfo.getRoleHashMap().get(targetId);
        if(result==0){
            unlockGoods(targetRole.getDealBo().getEquipmentId(),targetRole.getDealBo().getPotionId(),
                    targetRole.getDealBo().getNum(),targetRole.getDealBo().getPrice(),targetRole);
            unlockGoods(role.getDealBo().getEquipmentId(),role.getDealBo().getPotionId(),
                    role.getDealBo().getNum(),role.getDealBo().getPrice(),role);
            role.setDealBo(null);
            targetRole.setDealBo(null);

            ServerHandler.notifyRole(targetId,"对方已拒绝交易",role.getId(),"你已拒绝交易");
            return;
        }
        role.getDealBo().setAgree(true);
        if(targetRole.getDealBo().isAgree()){
            exChangeGoods(targetRole,role);
            exChangeGoods(role,targetRole);

            shopSubject.notifyObserver(0,role);
            shopSubject.notifyObserver(0,role);

            ServerHandler.notifyRole(targetId,"交易双方都已同意，交易成功",role.getId(),"交易双方都已同意，交易成功");
            return;
        }
        ServerHandler.notifyRole(targetId,"对方已同意，正等待你的同意",role.getId(),"你已同意，请等待对方同意");
    }

    /**
     * 交易时对物品进行锁定，这些物品暂时不可以正常使用
     * @param equipId 装备id
     * @param potionId 药品id
     * @param num 药品数量
     * @param role 角色
     */
    private static void lockGoods(int equipId,int potionId,int num,Role role){
        role.getGoodsLockMap().put(equipId,1);
        role.getGoodsLockMap().put(potionId,num);
    }

    /**
     * 交易确认后对物品和金钱进行解锁，这些物品可以正常使用
     * @param equipId 装备id
     * @param potionId 药品id
     * @param num 药品数量
     * @param price 补价
     * @param role 角色
     */
    private static void unlockGoods(int equipId,int potionId,int num,int price,Role role){
        PackageService packageService = new PackageService();
        packageService.putIntoPackage(equipId,1,role);
        packageService.putIntoPackage(potionId,num,role);
        packageService.addMoney(price,role);
        role.getGoodsLockMap().clear();
    }

    /**
     * 面对面交易时双方都同意的情况下，双方物品交换
     * @param targetRole 目标角色
     * @param role 角色
     */
    private void exChangeGoods(Role targetRole,Role role){
        packageService.putIntoPackage(targetRole.getDealBo().getEquipmentId(),1,role);
        packageService.putIntoPackage(targetRole.getDealBo().getPotionId(),targetRole.getDealBo().getNum(),role);
        packageService.addMoney(targetRole.getDealBo().getPrice(),role);
        targetRole.getGoodsLockMap().clear();
        targetRole.setDealBo(null);
    }

    /**
     * 上架到商店，一口价出售
     * @param goodsId 物品id
     * @param price 出售价格
     * @param num 出售数量
     * @param role 角色
     * @return 信息提示
     */
    public String saleByStore(int goodsId,int price,int num,Role role){
        if(!packageService.getFromPackage(goodsId,num,role)){
            return "背包中没有这么多物品";
        }
        PlayerSaleBo playerSaleBo = new PlayerSaleBo(goodsId,price,num);
        playerSaleBoHashMap.put(role.getId(),playerSaleBo);
        return "上架成功";
    }

    /**
     * 撤销已上架的物品
     * @param role 角色
     * @return 信息提示
     */
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

    /**
     * 获取玩家售卖列表的方法
     * @return 信息提示
     */
    public String getPlayerSaleList(){
        StringBuilder stringBuilder = new StringBuilder("玩家上架的物品有：\n");
        for(Integer key : playerSaleBoHashMap.keySet()){
            stringBuilder.append("出售者:").append(key).append("，物品：").append(playerSaleBoHashMap.get(key).getGoodsId()).append("，价格：").append(playerSaleBoHashMap.get(key).getPrice()).append("，数量：").append(playerSaleBoHashMap.get(key).getNum()).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 购买玩家上架的一口价物品
     * @param goodsId 物品id
     * @param buyNum 购买数量
     * @param offerId 出售人id
     * @param role 角色
     */
    public void buyFromePlayer(int goodsId,int buyNum,int offerId,Role role){
        int price = playerSaleBoHashMap.get(offerId).getPrice();
        int saleNum = playerSaleBoHashMap.get(offerId).getNum();
        if(buyNum>saleNum){return;}
        if(!packageService.lostMoney(price*buyNum,role)){return;}
        packageService.putIntoPackage(goodsId,buyNum,role);
        packageService.addMoney(price*buyNum, GlobalInfo.getRoleHashMap().get(offerId));
        playerSaleBoHashMap.get(offerId).setNum(saleNum-buyNum);

        ServerHandler.notifyRole(offerId,"你上架的物品"+goodsId+"被购买了"+buyNum+"个",role.getId(),"");
    }

    /**
     * 拍卖某件物品，默认一件
     * @param goodsId 物品id
     * @param minPrice 起价
     * @param role 角色
     * @return 信息提示
     */
    public String auctionSale(int goodsId,int minPrice,Role role){
        if(role.getNowScenesId()!=Const.AUCTION_SCENE){
            return "需要移动到拍卖场进行拍卖";
        }
        if(!packageService.getFromPackage(goodsId,1,role)){
            return "背包中没有这么多物品";
        }
        String auctionId = UUID.randomUUID().toString();
        AuctionBo auctionBo = new AuctionBo(auctionId,goodsId,minPrice,role.getId());
        role.setAuctionBo(auctionBo);
        role.getAuctionBo().setTagTime(Instant.now());
        role.getAuctionBo().setLastPrice(minPrice);
        countdown(goodsId,role);
        ArrayList<Role> roles = GlobalInfo.getScenes().get(Const.AUCTION_SCENE).getRoleAll();
        ServerHandler.notifyGroupRoles(roles,role.getName()+"已经开始拍卖了，物品"+goodsId+"，起价："+minPrice);
        return "开始拍卖";
    }

    /**
     * 竞价购买被拍卖的物品
     * @param price 竞价价格
     * @param offerId 出售人id
     * @param role 角色
     * @return 信息提示
     */
    public String auctionBuy(int price,int offerId,Role role){
        Role offerRole = GlobalInfo.getRoleHashMap().get(offerId);
        if(role.getNowScenesId()!=Const.AUCTION_SCENE){return "需要移动到拍卖场进行拍卖";}
        if(offerRole.getAuctionBo().isIfEnding()){return "拍卖已经结束，不可以再进行操作";}
        if(price<=offerRole.getAuctionBo().getLastPrice()){return "请出更高的价格！";}
        if(isOutOfMoney(price,offerRole,role)){return "已经没有钱了";}

        offerRole.getAuctionBo().getRoleArrayList().add(role);
        offerRole.getAuctionBo().setBuyRoleId(role.getId());
        offerRole.getAuctionBo().setLastPrice(price);

        //通知其他玩家
        ArrayList<Role> roles = GlobalInfo.getScenes().get(Const.AUCTION_SCENE).getRoleAll();
        ServerHandler.notifyGroupRoles(roles,role.getName()+"出价："+price);
        return "出价为"+price;
    }

    /**
     * 对喊价人的出价资金进行锁定，判断是否还有足够的钱参与拍卖；拍卖结束后统一将钱退款给竞价失败的人
     * @param price 价格
     * @param offerRole 出售者
     * @param role 角色
     * @return 是否还有足够的钱参与拍卖
     */
    private boolean isOutOfMoney(int price,Role offerRole,Role role){
        HashMap<Integer, Integer> priceHashMap = offerRole.getAuctionBo().getPriceHashMap();
        if(priceHashMap.get(role.getId())==null){
            if(!packageService.lostMoney(price,role)){return true;}
        }else{
            if(!packageService.lostMoney(price-priceHashMap.get(role.getId()),role)){return true;}
        }
        priceHashMap.put(role.getId(),price);
        return false;
    }

    /**
     * 拍卖倒计时
     * @param goodsId 物品id
     * @param offerRole 拍卖者
     * @return 信息提示
     */
    private void countdown(int goodsId,Role offerRole){
        Timer timer = new Timer();
        CountDownTime countDownTime = new CountDownTime(timer,goodsId,offerRole);
        timer.schedule(countDownTime, Const.AUCTION_TIME, Const.AUCTION_TIME);
    }

    /**
     * 获取商店物品列表，默认为配置表中的所有物品
     * @return 信息提示
     */
    public String getStaticGoodsList(){
        StringBuilder stringBuilder = new StringBuilder("欢迎光临本商店，商店提供： ");
        for(Integer key : EquipmentResource.getEquipmentStaticHashMap().keySet()){
            stringBuilder.append(AssistService.getEquipmentName(key)).append(":").append(AssistService.getGoodsPrice(key)).append("银； ");
        }
        for(Integer key : PotionResource.getPotionStaticHashMap().keySet()){
            stringBuilder.append(AssistService.getPotionName(key)).append(":").append(AssistService.getGoodsPrice(key)).append("银； ");
        }
        return stringBuilder.toString();
    }

    /** 注册成就观察者 */
    Subject shopSubject = new Subject();
    private FsTradeOb fsTradeOb = new FsTradeOb(shopSubject);
}
