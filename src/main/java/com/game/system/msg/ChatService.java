package com.game.system.msg;

import com.game.system.bag.PackageService;
import com.game.system.bag.pojo.MyPackageBo;
import com.game.system.role.pojo.Role;
import com.game.system.assist.GlobalInfo;
import com.game.system.shop.pojo.DealBo;

import java.util.HashMap;
import java.util.UUID;

/**
 * @Author andy
 * @create 2020/6/15 14:56
 */
public class ChatService {
    //全服聊天、私人聊天可以再netty服务端直接实现

    /**
     * 向目标发送邮件
     * @param targetRoleId 目标角色id
     * @param words 留言
     * @param goodsId 物品id
     * @param num 物品数量
     * @param money 邮寄金钱
     * @param role 角色
     * @return String
     */
    public static boolean emailToPlayer (int targetRoleId, String words, int goodsId, int num, int money, Role role){
        MyPackageBo myPackageBo = role.getMyPackageBo();
        PackageService packageService = new PackageService();
        if(!packageService.getFromPackage(goodsId,num,role)){
            return false;
        }

        Role targetRole = GlobalInfo.getRoleHashMap().get(targetRoleId);
        packageService.addMoney(money,targetRole);
        packageService.lostMoney(money,role);
        packageService.putIntoPackage(goodsId,num,targetRole);
        return true;
    }

}
