package com.game.system.social;

import com.game.netty.server.ServerHandler;
import com.game.system.achievement.observer.FriendOb;
import com.game.system.achievement.pojo.Subject;
import com.game.system.bag.PackageService;
import com.game.system.role.pojo.Role;
import com.game.system.gameserver.GlobalInfo;
import org.springframework.stereotype.Service;

/**
 * 通信模块的业务逻辑处理
 * @Author andy
 * @create 2020/6/15 14:56
 */
@Service
public class SocialService {

    /**
     * 申请添加好友
     * @param friendId 申请目标id
     * @param role 角色
     */
    public void askFriend(int friendId,Role role){
        ServerHandler.notifyRole(friendId,"收到好友添加申请",role.getId(),"请求已发送");
        role.getFriendBo().getApplyIdList().add(friendId);
    }

    /**
     * 同意添加好友
     * @param friendId 申请人id
     * @param role 角色
     */
    public void addFriend(Integer friendId,Role role){
        role.getFriendBo().getFriendIdList().add(role.getId());
        role.getFriendBo().getApplyIdList().remove(friendId);
        friendSubject.notifyObserver(0,role);
        Role friendRole = GlobalInfo.getRoleHashMap().get(friendId);
        friendSubject.notifyObserver(0,friendRole);
        ServerHandler.notifyRole(friendId,role.getName()+"已同意你的好友申请",role.getId(),"已添加好友");
    }

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

    /** 注册成就观察者 */
    Subject friendSubject = new Subject();
    FriendOb friendOb = new FriendOb(friendSubject);
}
