package com.game.system.role;

import com.game.system.gameserver.GlobalInfo;
import com.game.system.achievement.observer.LevelOb;
import com.game.system.achievement.entity.Subject;
import com.game.system.bag.PackageService;
import com.game.system.role.entity.Role;
import org.springframework.stereotype.Service;


/**
 * 角色模块的业务逻辑处理
 * @Author andy
 * @create 2020/5/13 18:18
 */
@Service
public class RoleService {

    /**
     * 升级-测试成就用
     * @param level 等级
     * @param role 角色
     */
    public void levelUp(int level,Role role){
        role.setLevel(level);
        levelSubject.notifyObserver(0,role);
    }

    /**
     * 获得自己或他人角色信息
     * @param role 角色
     * @return 信息提示
     */
    public String getRoleInfo(Role role){
        return  role.getName()+"的信息：\n"+"id:"+role.getId()+"\n"+"hp："+role.getHp()+"\n"+
                "mp："+role.getMp()+"\n"+"atk："+role.getAtk()+"\n"+"def："+role.getDef()+"\n"+"money："+role.getMoney()+"\n"+
                "nowScenesId:"+role.getNowScenesId()+"\n"+"careerId:"+role.getCareerId()+"\n"+
                "unionId:"+role.getUnionId()+"\n"+"level:"+role.getLevel()+"\n"+
                "背包物品："+PackageService.getPackage(role)+"\n"+"身上装备："+PackageService.getBodyEquip(role)+"\n";
    }

    /**
     * 测试用命令
     * @param role 角色
     * @return 信息提示
     */
    public String testCode(Role role){
        for(Integer key : GlobalInfo.getEquipmentHashMap().keySet()){
            System.out.println("全局装备有"+key+" ");
        }
        return "hi";
    }

    /**
     * 检查并设置角色血量，使其不超过上下界
     * @param hp 角色血量
     * @param role 角色
     */
    public static void checkAndSetHp(int hp,Role role){
        if(hp<0){
            role.setHp(0);
        }else if(hp>role.getMaxHp()){
            role.setHp(role.getMaxHp());
        }else {
            role.setHp(hp);
        }
    }

    /**
     * 检查并设置角色蓝量，使其不超过上下界
     * @param mp 角色蓝量
     * @param role 角色
     */
    public static void checkAndSetMp(int mp,Role role){
        if(mp<0){
            role.setMp(0);
        }else if(mp>role.getMaxMp()){
            role.setMp(role.getMaxMp());
        }else {
            role.setMp(mp);
        }
    }

    /** 注册成就观察者，预先注册好相应的监听器，当触发事件时，对已注册的监听器进行通知处理 */
    Subject levelSubject = new Subject();
    LevelOb levelOb = new LevelOb(levelSubject);
}


