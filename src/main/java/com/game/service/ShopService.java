package com.game.service;

import com.game.common.Const;
import com.game.dao.RoleMapper;
import com.game.entity.Role;
import com.game.entity.store.EquipmentResource;
import com.game.entity.store.PotionResource;
import com.game.entity.vo.PlayerSaleVo;
import com.game.service.assis.AssistService;
import com.game.service.assis.GlobalResource;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author andy
 * @create 2020/6/15 15:00
 */
public class ShopService {

    static RoleMapper roleMapper = new RoleMapper();
    public static ArrayList<PlayerSaleVo> saleArrayList = new ArrayList<>();
    public static ArrayList<PlayerSaleVo> auctionArrayList = new ArrayList<>();
    public static HashMap<Integer,Integer> auctionHashMap = new HashMap<>();

    public static String buyGoods(String goods,int number,int roleId){
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        int key = AssistService.checkGoodsId(goods);
        //药品的情况
        if(String.valueOf(key).startsWith(Const.POTION_HEAD)){
            if(!ifLimitBuy(key,number,roleId)){
                return Const.Shop.LIMIT_MSG;
            }
            int cost = PotionResource.getPotionStaticHashMap().get(key).getPrice()*number;
            if(!ifCanbuy(role,cost)){
                return Const.Shop.BUY_FAILURE;
            }
            PackageService.putIntoPackage(key,number,roleId);
            return Const.Shop.BUY_SUCCESS+ role.getMyPackage().getGoodsHashMap().get(key);
        }else {  //装备的情况
            int cost = EquipmentResource.getEquipmentStaticHashMap().get(key).getPrice()*number;
            if(!ifCanbuy(role,cost)){
                return Const.Shop.BUY_FAILURE;
            }
            PackageService.putIntoPackage(key,number,roleId);
            return Const.Shop.BUY_SUCCESS+ role.getMyPackage().getGoodsHashMap().get(key);
        }
    }

    //限购代码
    public static boolean ifLimitBuy(int goodsId,int number,int roleId){
        HashMap<Integer,HashMap<Integer,Integer>> buyRecord = new HashMap<>();
        buyRecord = roleMapper.selectBuyRecord(roleId);
        if(buyRecord.get(roleId).get(goodsId)==null){
            buyRecord.get(roleId).put(goodsId,0);
        }
        int sumNumber = number + buyRecord.get(roleId).get(goodsId);
        System.out.println(sumNumber);
        if(sumNumber> Const.GOODS_BUG_MAX){
            return false;
        }else {
            roleMapper.insertBuyRecord(roleId,goodsId,sumNumber);
        }
        return true;
    }

    public static boolean ifCanbuy(Role role,int cost){
        if(role.getMoney()<cost){
            return false;
        }
        role.setMoney(role.getMoney()-cost);
        return true;
    }

    public static boolean tradeWithPlayer(String result,int roleId){
        //约定条件：在玩家视野下看到某个角色，点击该角色，提供一个交易按钮，获得传参
        if(result.equals("no")){ //对方不接受，不扣钱
            return false;//无论成功与否应该给发起交易的人回一个信息
        }
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        //对方接受，自己物品没了，钱到手；-假设只考虑药品
        Role sendRole = GlobalResource.getRoleHashMap().get(role.getDealVo().getSendRoleId());
        sendRole.setMoney(sendRole.getMoney()+role.getDealVo().getPrice());
        int key = role.getDealVo().getGoodsId();
        sendRole.getMyPackage().getGoodsHashMap().put(key,sendRole.getMyPackage().getGoodsHashMap().get(key)-1);
        //对方钱没了，物品到手
        role.setMoney(role.getMoney()-role.getDealVo().getPrice());
        PackageService.putIntoPackage(key,1,roleId);
        return true;
    }

    //saleToPlayer  goods price id  将该商品上架到店中
    public static void saleToPlayer(int goodsId,int price,int roleId){
        //商店列表中增加一个集合，存放玩家放在商店卖的所有信息，并将该新信息添加到集合中，包括提供的角色id，物品id和价格
        //其他玩家可以通过查看该集合组成的列表来直到目前的一口价商品 todo 与前面的buy方法相结合
        saleArrayList.add(new PlayerSaleVo(goodsId,price,roleId));
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        role.getMyPackage().getGoodsHashMap().put(goodsId,role.getMyPackage().getGoodsHashMap().get(goodsId)-1);
    }

    //在商品列表中购买，购买后商品进行下架
    public static void buyFromePlayer(int goodsId,int price,int offerId,int roleId){
        Role offerRole = GlobalResource.getRoleHashMap().get(offerId);//提供者
        Role role = GlobalResource.getRoleHashMap().get(roleId);//购买者
        PackageService.putIntoPackage(goodsId,1,roleId);
        role.setMoney(role.getMoney()-price);
        offerRole.setMoney(offerRole.getMoney()+price);
    }

    public static void auctionSale(int goodsId,int minPrice,int roleId){
        PlayerSaleVo playerSaleVo = new PlayerSaleVo(goodsId,minPrice,roleId);
        auctionArrayList.add(playerSaleVo);
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        role.setPlayerSaleVo(playerSaleVo);
        role.getPlayerSaleVo().setTagTime(Instant.now());
        role.getMyPackage().getGoodsHashMap().put(goodsId,role.getMyPackage().getGoodsHashMap().get(goodsId)-1);
    }

    public static String auctionBuy(int goodsId,int price,int offerId,int roleId){
        //将该角色提供的（最高）价格插入到集合中
        //向所有目前参与的拍卖者，显示出目前最高价者与剩余拍卖时间（就是当前调用该方法的这个角色）-可以自己调用方法来获取（存在线程安全问题）
        //根据offerId，从集合中找到这个PlayerSaleVo
        Role offerRole = GlobalResource.getRoleHashMap().get(offerId);//提供者
        Role role = GlobalResource.getRoleHashMap().get(roleId);//出价者

        //时间结束，记录此刻集合中的最高价者
        Duration between = Duration.between(offerRole.getPlayerSaleVo().getTagTime(), Instant.now());
        long l = between.toMillis()/Const.TO_MS; // 嘲讽持续时间-秒
        System.out.println(l);
        if(l>Const.AUCTION_TIME){
            //进行结算
            int lastBuyRoleId = offerRole.getPlayerSaleVo().getBuyRoleId();
            Role lastRole = GlobalResource.getRoleHashMap().get(lastBuyRoleId);
            auctionSummary(goodsId,offerRole.getPlayerSaleVo().getLastPrice(),offerRole,role);
            return "时间到，竞价结束";
        }
        offerRole.getPlayerSaleVo().setBuyRoleId(roleId);
        //将该角色信息和价格放入集合中-或者某个变量中
        offerRole.getPlayerSaleVo().setLastPrice(price);
        long restTime = Const.AUCTION_TIME-l;
        return "有人出价，目前最高价为"+offerRole.getPlayerSaleVo().getLastPrice()+"，剩余时间："+restTime;
    }

    public static void auctionSummary(int goodsId,int maxPrice,Role offerRole,Role role){
        offerRole.setMoney(offerRole.getMoney()+maxPrice);
        role.setMoney(role.getMoney()-maxPrice);
        PackageService.putIntoPackage(goodsId,1,role.getId());
    }
}
