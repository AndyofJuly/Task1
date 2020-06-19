package com.game.service.assis;

import com.game.common.Const;
import com.game.controller.RoleController;
import com.game.entity.MyPackage;
import com.game.entity.Role;
import com.game.entity.Skill;
import com.game.entity.store.EquipmentResource;
import com.game.entity.store.PotionResource;
import com.game.entity.store.SkillResource;
import com.game.service.RoleService;

import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;

/**
 * 角色进入游戏后，初始化技能，该类目前只是用于测试，后续将进行改进
 * @Author andy
 * @create 2020/5/28 11:29
 */
public class InitRole {
    private static boolean enterSuccess = false;

    public static void init(int roleId){
        Instant start = Instant.now();
        Role role = GlobalResource.getRoleHashMap().get(roleId);
        //目前角色拥有四个技能，全都初始化给角色
        for (Integer key : SkillResource.getSkillStaticHashMap().keySet()) {
            role.getSkillHashMap().put(key,new Skill(key));
            role.getSkillHashMap().get(key).setStart(start);
        }

        //本类可以在游戏开始时调用数据库的一些信息，还原角色当前状态，例如角色背包中的物品等
        //目前所有装备和药物都初始化给角色，用于代码测试
        HashMap<Integer,Integer> goods = new HashMap<Integer,Integer>();
        //如果不改变值，可以考虑简单引用（浅拷贝）静态资源的地址
        for (Integer key : PotionResource.getPotionStaticHashMap().keySet()) {
            goods.put(key,2);
        }

        //装备要改变耐久，因此需要深拷贝，改变值了；先通过手动赋值的方式简单实现
        //HashMap<Integer, Integer> equipmentHashMap = new HashMap<Integer,Integer>();
        for (Integer key : EquipmentResource.getEquipmentStaticHashMap().keySet()) {
            //Equipment equipment = new Equipment(key, EquipmentResource.equipmentStaticHashMap.get(key).getDurability());
            //equipment.setDura(InitStaticResource.equipmentStaticHashMap.get(key).getDurability());
            goods.put(key,1);
        }
        role.setMyPackage( new MyPackage(100,goods));

        if(enterSuccess){
            //注意前提条件：角色登录成功，开始自动恢复mp
            run();
            //自动告知当前位置，自动装上装备，便于测试
            RoleService roleService = new RoleService();
            roleService.putOnEquipment(Const.WEPON,roleId);
        }
        enterSuccess = false;

    }

    private static void run() {
        Timer timer = new Timer();
        MpRecover mpRecover = new MpRecover();
        //程序运行后立刻执行任务，每隔10000ms执行一次
        timer.schedule(mpRecover, Const.DELAY_TIME, Const.GAP_TIME_POTION);
    }

    public static boolean isEnterSuccess() {
        return enterSuccess;
    }

    public static void setEnterSuccess(boolean enterSuccess) {
        InitRole.enterSuccess = enterSuccess;
    }
}
