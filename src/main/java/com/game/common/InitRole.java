package com.game.common;

import com.game.controller.FunctionService;
import com.game.entity.Equipment;
import com.game.entity.MyPackage;
import com.game.entity.Potion;
import com.game.entity.Skill;
import com.game.entity.excel.EquipmentStatic;

import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;

/**
 * 角色进入游戏后，初始化技能，该类目前只是用于测试，后续将进行改进
 * @Author andy
 * @create 2020/5/28 11:29
 */
public class InitRole {
    static {

        //目前角色拥有两个技能
        Skill skillA = new Skill(InitStaticResource.skillStaticHashMap.get(1001));
        Skill skillB = new Skill(InitStaticResource.skillStaticHashMap.get(1002));

        //放入角色的技能集合中
        FunctionService.role.getSkillHashMap().put(1001,skillA);
        FunctionService.role.getSkillHashMap().put(1002,skillB);

        //初始化技能的使用时间戳
        Instant start = Instant.now();
        FunctionService.role.getSkillHashMap().get(1001).setStart(start);
        FunctionService.role.getSkillHashMap().get(1002).setStart(start);

        //角色静态数据信息注入，这里是角色当前等级的血量和蓝量
        FunctionService.role.setRoleStatic(InitStaticResource.roleStaticHashMap.get(101));

        //本类可以在游戏开始时调用数据库的一些信息，还原角色当前状态

        //此处初始化背包物品，给角色99瓶红药，集合最大容量为99，给角色其他装备放在背包里
        //角色背包
        // TODO: 2020/5/29 后面改为从数据库中读取

        HashMap<Integer,Potion> potionHashMap = new HashMap<Integer,Potion>();
        //如果不改变值，可以考虑简单引用（浅拷贝）静态资源的地址
        potionHashMap.put(2001,new Potion(2,InitStaticResource.potionStaticHashMap.get(2001)));
        potionHashMap.put(2002,new Potion(2,InitStaticResource.potionStaticHashMap.get(2002)));

        //装备要改变耐久，因此需要深拷贝，改变值了；注意这个问题即可！这里先通过手动赋值的方式简单实现
        HashMap<Integer, Equipment> equipmentHashMap = new HashMap<Integer,Equipment>();
        Equipment s1 = new Equipment(new EquipmentStatic(3001,"钢剑",10,50));
        Equipment s2 = new Equipment(new EquipmentStatic(3002,"青釭剑",30,100));
        equipmentHashMap.put(3001,s1);
        equipmentHashMap.put(3002,s2);
        FunctionService.role.setMyPackage( new MyPackage(120,potionHashMap,equipmentHashMap));
        //开始自动恢复mp
        run();

    }

    private static void run() {
        Timer timer = new Timer();
        MpRecover mpRecover = new MpRecover();
        //程序运行后立刻执行任务，每隔10000ms执行一次
        timer.schedule(mpRecover, 0, 10000);
    }

}
