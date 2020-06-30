package com.game.service;

import com.game.entity.Role;
import com.game.service.assis.GlobalResource;

/**
 * 调用该方法判断成就及设置成就
 * @Author andy
 * @create 2020/6/28 19:12
 */
public class AchievementService {

    //优化：一些方法可以考虑只执行一次

    //和某个npc对话
    public static void ifTalkToOldMan(int npcId,Role role){
        if(npcId==20001){
            role.getAchievementVo().setFirstTalkToNpcOleMan(true);
        }
    }

    //通关某个副本
    public static void ifPassPartiDungeons(int dungeonsId,Role role){
        if(dungeonsId==4001){
            role.getAchievementVo().setPassPartiDungeons(true);
        }
    }

    //添加一个好友-两个人都判读
    public static void ifFirstAddOneFriend(int friendId,Role role){
        Role friendRole = GlobalResource.getRoleHashMap().get(friendId);
        friendRole.getAchievementVo().setFirstAddOneFriend(true);
        role.getAchievementVo().setFirstAddOneFriend(true);
        //task
        ifAchievSerialTask(friendRole);
        ifAchievSerialTask(role);
    }

    //第一次组队
    public static void ifFirstJoinTeam(Role role){
        role.getAchievementVo().setFirstJoinTeam(true);
        //task
        ifAchievSerialTask(role);
    }

    //第一次加入公会
    public static void ifFirstJoinUnion(Role role){
        //Role role = GlobalResource.getRoleHashMap().get(applyRoleId);
        role.getAchievementVo().setFirstJoinUnion(true);
        //task
        ifAchievSerialTask(role);
    }

    //第一次与玩家交易
    public static void ifFirstTradeWithPlayer(Role role){
        role.getAchievementVo().setFirstTradeWithPlayer(true);
    }

    //第一次在pk中战胜
    public static void ifFirstPkSuccess(Role role){
        role.getAchievementVo().setFirstPkSuccess(true);
    }

    //累积型；计数并判断是否满足数量要求，满足则设为true;
    //击杀特定小怪N-1-5只（小偷）；考虑可能同时杀死多只怪物的情况
    public static void ifSlayPartiMonster(int monsterId,Role role){
        //判断角色的vo中的slayConut
        if(monsterId==30003){
            role.getAchievementVo().setSlayConut(1);
        }
        if(role.getAchievementVo().getSlayConut()>=1){
            role.getAchievementVo().setSlayPartiMonster(true);
        }
    }

    //等级提升到N-20级
    public static void ifLevelUpToTwenty(Role role){
        //判断角色的vo中的level
        //role.getAchievementVo().setLevel(1);
        if(role.getLevel()>=20){
            role.getAchievementVo().setLevelUpToTwenty(true);
        }
    }

    //获得N-2件极品装备？目前还没有对装备进行分类-在放入背包时统计检查即可（放入背包方法），注意背包中查，不是累积
    public static void ifGetNBestEquipment(Role role){
        //查背包
        //role.getAchievementVo().setBestEquipmentNum(1);
        if(role.getMyPackage().getBestNum()>=2){
            role.getAchievementVo().setGetNBestEquipment(true);
        }
    }

    //穿戴的装备等级总和达到XXX-20？注意当前穿戴！
    public static void ifSumEquipmentLevelToFourty(Role role){
        //判断角色的vo中的equipmentLevel
        //role.getAchievementVo().setEquipmentLevel(level);
        System.out.println("sumlevel="+RoleService.sumWearLevel(role));
        if(RoleService.sumWearLevel(role)>=20){
            role.getAchievementVo().setSumEquipmentLevelToFourty(true);
        }
    }

    //当前金币达到xxxx-将所有涉及到增减金币的方法进行修改，抽象出一个增加和减少角色金币的方法，在该方法中调用此方法addMoney
    public static void ifSumMoneyToThousand(int number,Role role){
        //判断角色的vo中的money-舍弃-当前金币！
        //role.getAchievementVo().setMoney(number);
        if(role.getMoney()>=110){
            role.getAchievementVo().setSumMoneyToThousand(true);
        }
    }

    //完成某一系列任务?例如社交任务：加一个好友+组队+加入公会
    public static void ifAchievSerialTask(Role role){
        //判断角色的vo中的taskNum-舍弃
        if(role.getAchievementVo().isFirstAddOneFriend() && role.getAchievementVo().isFirstJoinTeam() && role.getAchievementVo().isFirstJoinUnion()){
            role.getAchievementVo().setAchievSerialTask(true);
        }
    }

    //获得角色当前所有成就信息
    public static String getAchievmentList(Role role){
        return  "添加一个好友："+role.getAchievementVo().isFirstAddOneFriend()+"\n"+
                "第一次组队："+role.getAchievementVo().isFirstJoinTeam()+"\n"+
                "第一次加入公会："+role.getAchievementVo().isFirstJoinUnion()+"\n"+
                "第一次在pk中战胜："+role.getAchievementVo().isFirstPkSuccess()+"\n"+
                "和某个npc对话："+role.getAchievementVo().isFirstTalkToNpcOleMan()+"\n"+
                "第一次与玩家交易："+role.getAchievementVo().isFirstTradeWithPlayer()+"\n"+
                "通关某个副本："+role.getAchievementVo().isPassPartiDungeons()+"\n"+

                "等级提升到N级："+role.getAchievementVo().isLevelUpToTwenty()+"\n"+
                "击杀特定小怪："+role.getAchievementVo().isSlayPartiMonster()+"\n"+
                "获得N件极品装备："+role.getAchievementVo().isGetNBestEquipment()+"\n"+
                "穿戴的装备等级总和达到："+role.getAchievementVo().isSumEquipmentLevelToFourty()+"\n"+
                "当前金币达到："+role.getAchievementVo().isSumMoneyToThousand()+"\n"+
                "完成某一系列任务-前三任务："+role.getAchievementVo().isAchievSerialTask();
    }
}
