package com.game.system.msg;

import com.game.system.bag.PackageService;
import com.game.system.role.pojo.Role;
import com.game.system.assist.GlobalInfo;

/**
 * 通信模块的业务逻辑处理
 * @Author andy
 * @create 2020/6/15 14:56
 */
public class ChatService {

    /**
     * 向目标发送邮件
     * @param targetRoleId 目标角色id
     * @param goodsId 物品id
     * @param num 物品数量
     * @param money 邮寄金钱
     * @param role 角色
     * @return String
     */
    public static boolean emailToPlayer(int targetRoleId, int goodsId, int num, int money, Role role){
        PackageService packageService = new PackageService();
        if(!packageService.getFromPackage(goodsId,num,role)){return false;}
        if(!packageService.lostMoney(money,role)){return false;}
        Role targetRole = GlobalInfo.getRoleHashMap().get(targetRoleId);
        packageService.addMoney(money,targetRole);
        packageService.putIntoPackage(goodsId,num,targetRole);
        return true;
    }

}
