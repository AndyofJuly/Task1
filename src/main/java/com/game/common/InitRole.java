package com.game.common;

import com.game.controller.FunctionService;
import com.game.entity.Equipment;
import com.game.entity.MyPackage;
import com.game.entity.Potion;
import com.game.entity.Skill;
import com.game.entity.excel.EquipmentStatic;
import com.game.service.MpRecover;

import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;

/**
 * 角色进入游戏后，初始化技能，该类目前只是用于测试，后续将进行改进
 * @Author andy
 * @create 2020/5/28 11:29
 */
public class InitRole {

    public static void init(int roleId){
        Instant start = Instant.now();
        //目前角色拥有四个技能，全都初始化给角色
        for (Integer key : InitStaticResource.skillStaticHashMap.keySet()) {
            FunctionService.roleHashMap.get(roleId).getSkillHashMap().put(key,new Skill(key));
            FunctionService.roleHashMap.get(roleId).getSkillHashMap().get(key).setStart(start);
        }

        //角色静态数据信息注入，这里是角色当前等级的血量和蓝量；set后方便get
        FunctionService.roleHashMap.get(roleId).setLevelId(Const.TYPE_ID);

        //本类可以在游戏开始时调用数据库的一些信息，还原角色当前状态，例如角色背包中的物品等
        //目前所有装备和药物都初始化给角色，用于代码测试
        HashMap<Integer,Potion> potionHashMap = new HashMap<Integer,Potion>();
        //如果不改变值，可以考虑简单引用（浅拷贝）静态资源的地址
        for (Integer key : InitStaticResource.potionStaticHashMap.keySet()) {
            potionHashMap.put(key,new Potion(2,key));
        }

        //装备要改变耐久，因此需要深拷贝，改变值了；注意这个问题即可！这里先通过手动赋值的方式简单实现
        HashMap<Integer, Equipment> equipmentHashMap = new HashMap<Integer,Equipment>();
        for (Integer key : InitStaticResource.equipmentStaticHashMap.keySet()) {
            Equipment equipment = new Equipment(key,InitStaticResource.equipmentStaticHashMap.get(key).getDurability());
            //equipment.setDura(InitStaticResource.equipmentStaticHashMap.get(key).getDurability());
            System.out.println(InitStaticResource.equipmentStaticHashMap.get(key).getDurability());
            equipmentHashMap.put(key,equipment);
        }
        FunctionService.roleHashMap.get(roleId).setMyPackage( new MyPackage(100,potionHashMap,equipmentHashMap));

        //开始自动恢复mp
        run();
    }
    /*static {
        //初始化技能的使用时间戳
        Instant start = Instant.now();

        //目前角色拥有四个技能，全都初始化给角色
        for (Integer key : InitStaticResource.skillStaticHashMap.keySet()) {
            FunctionService.role.getSkillHashMap().put(key,new Skill(key));
            FunctionService.role.getSkillHashMap().get(key).setStart(start);
        }

        //角色静态数据信息注入，这里是角色当前等级的血量和蓝量；set后方便get
        FunctionService.role.setLevelId(Const.TYPE_ID);
//                setRoleStatic(InitStaticResource.roleStaticHashMap.get(Const.TYPE_ID));

        //本类可以在游戏开始时调用数据库的一些信息，还原角色当前状态，例如角色背包中的物品等
        //目前所有装备和药物都初始化给角色，用于代码测试
        HashMap<Integer,Potion> potionHashMap = new HashMap<Integer,Potion>();
        //如果不改变值，可以考虑简单引用（浅拷贝）静态资源的地址
        for (Integer key : InitStaticResource.potionStaticHashMap.keySet()) {
            potionHashMap.put(key,new Potion(2,key));
        }
*//*        potionHashMap.put(2001,new Potion(2,InitStaticResource.potionStaticHashMap.get(2001)));
        potionHashMap.put(2002,new Potion(2,InitStaticResource.potionStaticHashMap.get(2002)));*//*

        //装备要改变耐久，因此需要深拷贝，改变值了；注意这个问题即可！这里先通过手动赋值的方式简单实现
        HashMap<Integer, Equipment> equipmentHashMap = new HashMap<Integer,Equipment>();
        for (Integer key : InitStaticResource.equipmentStaticHashMap.keySet()) {
            Equipment equipment = new Equipment(key,InitStaticResource.equipmentStaticHashMap.get(key).getDurability());
            //equipment.setDura(InitStaticResource.equipmentStaticHashMap.get(key).getDurability());
            System.out.println(InitStaticResource.equipmentStaticHashMap.get(key).getDurability());
            equipmentHashMap.put(key,equipment);
        }
*//*        Equipment s1 = new Equipment(new EquipmentStatic(3001,"钢剑",10,50));
        Equipment s2 = new Equipment(new EquipmentStatic(3002,"青釭剑",30,100));
        equipmentHashMap.put(3001,s1);
        equipmentHashMap.put(3002,s2);*//*
        FunctionService.role.setMyPackage( new MyPackage(100,potionHashMap,equipmentHashMap));




        //开始自动恢复mp
        run();
    }*/

    private static void run() {
        Timer timer = new Timer();
        MpRecover mpRecover = new MpRecover();
        //程序运行后立刻执行任务，每隔10000ms执行一次
        timer.schedule(mpRecover, 0, 10000);
    }

}
