package com.game.service;

import com.game.common.Const;
import com.game.dao.RecordMapper;
import com.game.entity.Role;
import com.game.entity.store.EquipmentResource;
import com.game.entity.store.PotionResource;
import com.game.entity.vo.PlayerSaleVo;
import com.game.service.assis.AssistService;
import com.game.service.assis.GlobalResource;
import com.game.service.helper.EquipmentHelper;
import com.game.service.helper.PotionHelper;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/6/15 15:00
 */
public class ShopService {

    private RecordMapper recordMapper = new RecordMapper();
    private PackageService packageService = new PackageService();
    public static ArrayList<PlayerSaleVo> saleArrayList = new ArrayList<>();
    public static ArrayList<PlayerSaleVo> auctionArrayList = new ArrayList<>();

    // here
    public String buyGoods(int goodsId,int number,Role role){
        //int key = AssistService.checkGoodsId(goods);
        //药品的情况
        if(String.valueOf(goodsId).startsWith(Const.POTION_HEAD)){
            if(!ifLimitBuy(goodsId,number,role.getId())){
                return Const.Shop.LIMIT_MSG;
            }
            int cost = PotionHelper.getPotionPrice(goodsId) *number;
            if(!ifCanbuy(role,cost)){
                return Const.Shop.BUY_FAILURE;
            }
            packageService.putIntoPackage(goodsId,number,role);
            return Const.Shop.BUY_SUCCESS+ role.getMyPackage().getGoodsHashMap().get(goodsId);
        }else {  //装备的情况
            int cost = EquipmentHelper.getEquipmentPrice(goodsId)*number;
            if(!ifCanbuy(role,cost)){
                return Const.Shop.BUY_FAILURE;
            }
            packageService.putIntoPackage(goodsId,number,role);
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
        role.setMoney(role.getMoney()-cost);
        return true;
    }

    public boolean tradeWithPlayer(int result,Role role){
        if(result==0){
            return false;
        }
        Role saleRole = GlobalResource.getRoleHashMap().get(role.getDealVo().getSendRoleId());
        PackageService.addMoney(role.getDealVo().getPrice(),saleRole);
        int key = role.getDealVo().getGoodsId();
        int num = saleRole.getMyPackage().getGoodsHashMap().get(key)-1;
        saleRole.getMyPackage().getGoodsHashMap().put(key,num);
        role.setMoney(role.getMoney()-role.getDealVo().getPrice());
        packageService.putIntoPackage(key,1,role);
        AchievementService.ifFirstTradeWithPlayer(role);
        return true;
    }

    //saleToPlayer  goods price id  将该商品上架到店中
    public void saleToPlayer(int goodsId,int price,Role role){
        //商店列表中增加一个集合，存放玩家放在商店卖的所有信息，并将该新信息添加到集合中，包括提供的角色id，物品id和价格
        //其他玩家可以通过查看该集合组成的列表来直到目前的一口价商品 todo 与前面的buy方法相结合
        saleArrayList.add(new PlayerSaleVo(goodsId,price,role.getId()));
        int num = role.getMyPackage().getGoodsHashMap().get(goodsId)-1;
        role.getMyPackage().getGoodsHashMap().put(goodsId,num);
    }

    //在商品列表中购买，购买后商品进行下架
    public void buyFromePlayer(int goodsId,int price,int offerId,Role role){
        Role offerRole = GlobalResource.getRoleHashMap().get(offerId);//提供者
        packageService.putIntoPackage(goodsId,1,role);
        role.setMoney(role.getMoney()-price);
        PackageService.addMoney(price,offerRole);
    }

    public void auctionSale(int goodsId,int minPrice,Role role){
        PlayerSaleVo playerSaleVo = new PlayerSaleVo(goodsId,minPrice,role.getId());
        auctionArrayList.add(playerSaleVo);
        role.setPlayerSaleVo(playerSaleVo);
        role.getPlayerSaleVo().setTagTime(Instant.now());
        int num = role.getMyPackage().getGoodsHashMap().get(goodsId)-1;
        role.getMyPackage().getGoodsHashMap().put(goodsId,num);
    }

    public String auctionBuy(int goodsId,int price,int offerId,Role role){
        //将该角色提供的（最高）价格插入到集合中
        //向所有目前参与的拍卖者，显示出目前最高价者与剩余拍卖时间（就是当前调用该方法的这个角色）-可以自己调用方法来获取（存在线程安全问题）
        Role offerRole = GlobalResource.getRoleHashMap().get(offerId);//提供者
        Duration between = Duration.between(offerRole.getPlayerSaleVo().getTagTime(), Instant.now());
        long l = between.toMillis()/Const.TO_MS; // 拍卖已进行的时间
        if(l>Const.AUCTION_TIME){
            //进行结算
            int lastBuyRoleId = offerRole.getPlayerSaleVo().getBuyRoleId();
            Role lastRole = GlobalResource.getRoleHashMap().get(lastBuyRoleId);
            auctionSummary(goodsId,offerRole.getPlayerSaleVo().getLastPrice(),offerRole,lastRole);
            return "时间到，竞价结束";
        }
        offerRole.getPlayerSaleVo().setBuyRoleId(role.getId());
        offerRole.getPlayerSaleVo().setLastPrice(price);
        long restTime = Const.AUCTION_TIME-l;
        return "有人出价，目前最高价为"+offerRole.getPlayerSaleVo().getLastPrice()+"，剩余时间："+restTime;
    }

    public void auctionSummary(int goodsId,int maxPrice,Role offerRole,Role lastRole){
        PackageService.addMoney(maxPrice,offerRole);
        lastRole.setMoney(lastRole.getMoney()-maxPrice);
        packageService.putIntoPackage(goodsId,1,lastRole);
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
