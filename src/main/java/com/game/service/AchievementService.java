package com.game.service;

import com.game.entity.Role;
import com.game.entity.excel.AchieveStatic;
import com.game.entity.store.AchieveResource;
import com.game.entity.store.EquipmentResource;
import com.game.entity.vo.AchievementVo;
import com.game.service.assis.GlobalResource;

import java.util.Arrays;
import java.util.HashMap;

/**
 * 调用该方法判断成就及设置成就
 * @Author andy
 * @create 2020/6/28 19:12
 */
public class AchievementService {

    //针对不同的类型，可以将不同成就放在不同表和集合中，可以减少遍历带来的性能影响

    //一个count方法，将击杀的怪物id进行计数的方法，该方法判断hashmap中是否有该id，有则加一，没有则put
    public static void countKilledMonster(int monsterId,Role role){
        //计数后顺便判断
        Object oldCount = role.getAchievementVo().getKillMonsterCountMap().get(monsterId);
        if(oldCount==null){
            role.getAchievementVo().getKillMonsterCountMap().put(monsterId,1);
        }else {
            role.getAchievementVo().getKillMonsterCountMap().put(monsterId,(int)oldCount+1);
        }
        //遍历检查是否完成成就系统中的某项成就：遍历成就静态表中的怪物id和对应的数量，如果目前大于该数量，则提示成就完成，设置到缓存中，如果小于，则没有完成
        ifSlayPartiMonster(monsterId,role);
    }

    private static void ifSlayPartiMonster(int monsterId,Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            if("killMonster".equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc())){
                int nowCount = role.getAchievementVo().getKillMonsterCountMap().get(monsterId);
                int targetCount = AchieveResource.getAchieveStaticHashMap().get(achievId).getCount();
                if(nowCount>=targetCount){
                    role.getAchievementVo().getAchievementHashMap().put(achievId,true);
                }
            }
        }
    }

    public static void countLevel(Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = "levelUp".equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean levelCompare = role.getLevel()>=AchieveResource.getAchieveStaticHashMap().get(achievId).getCount();
            if(staticSearch && levelCompare){
                role.getAchievementVo().getAchievementHashMap().put(achievId,true);
            }
        }
    }

    //和某个npc对话
    public static void talkToNpc(int npcId,Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = "talkNpc".equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean npcCompare = npcId==AchieveResource.getAchieveStaticHashMap().get(achievId).getTargetId();
            if(staticSearch && npcCompare){
                role.getAchievementVo().getAchievementHashMap().put(achievId,true);
            }
        }
    }

    //统计极品装备数量
    public static void countBestEquipment(int equipmentId,Role role){
        System.out.println("装备id是"+equipmentId);
        int quality = EquipmentResource.getEquipmentStaticHashMap().get(equipmentId).getQuality();
        if(quality==1){
            role.getMyPackage().setSumBestNum(1);
        }
        ifGetNBestEquipment(role);
    }

    //获得N件极品装备
    private static void ifGetNBestEquipment(Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = "getBessEquip".equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean euipCompare = role.getMyPackage().getBestNum()>=AchieveResource.getAchieveStaticHashMap().get(achievId).getCount();
            if(staticSearch && euipCompare){
                role.getAchievementVo().getAchievementHashMap().put(achievId,true);
            }
        }
    }

    //通关某个副本
    public static void ifPassPartiDungeons(int dungeonsId,Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = "passDungeons".equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean euipCompare =dungeonsId==AchieveResource.getAchieveStaticHashMap().get(achievId).getTargetId();
            if(staticSearch && euipCompare){
                role.getAchievementVo().getAchievementHashMap().put(achievId,true);
            }
        }
    }

    //计算穿戴装备总等级
    public static void countSumWearLevel(Role role){
        int sumLevel = 0;
        for(Integer equipmentId : role.getEquipmentHashMap().keySet()){
            sumLevel+=role.getEquipmentHashMap().get(equipmentId).getLevel();
        }
        ifSumEquipmentLevelTo(sumLevel,role);
    }

    //穿戴的装备等级总和达到XXX
    private static void ifSumEquipmentLevelTo(int sumLevel,Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = "sumEquipLevel".equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean euipCompare = sumLevel>=AchieveResource.getAchieveStaticHashMap().get(achievId).getCount();
            if(staticSearch && euipCompare){
                role.getAchievementVo().getAchievementHashMap().put(achievId,true);
            }
        }
    }

    //添加一个好友-两个人都判读
    public static void countAddFriend(int friendId,Role role){
        Role friendRole = GlobalResource.getRoleHashMap().get(friendId);
        friendRole.getAchievementVo().setCountFriend();
        role.getAchievementVo().setCountFriend();
        ifAddFriend(friendRole);
        ifAddFriend(role);
    }

    private static void ifAddFriend(Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = "addFriend".equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean euipCompare = role.getAchievementVo().getCountFriend()>AchieveResource.getAchieveStaticHashMap().get(achievId).getCount();
            if(staticSearch && euipCompare){
                role.getAchievementVo().getAchievementHashMap().put(achievId,true);
            }
        }

    }

    //第一次系列任务抽象
    private static void firstAchieve(String desc,Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            if(desc.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc())){
                role.getAchievementVo().getAchievementHashMap().put(achievId,true);
            }
        }
        ifAchievSerialTask(role);
    }


    //第一次组队
    public static void ifFirstJoinTeam(Role role){
        firstAchieve("firstJoinTeam",role);
    }

    //第一次加入公会
    public static void ifFirstJoinUnion(Role role){
        firstAchieve("firstJoinUnion",role);
    }

    //第一次与玩家交易
    public static void ifFirstTradeWithPlayer(Role role){
        firstAchieve("firstTrade",role);
    }

    //第一次在pk中战胜
    public static void ifFirstPkSuccess(Role role){
        firstAchieve("firstPkSuccess",role);
    }

    //当前金币达到xxxx
    public static void ifSumMoneyToThousand(int number,Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = "sumMoney".equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean euipCompare = role.getMoney()>=AchieveResource.getAchieveStaticHashMap().get(achievId).getCount();
            if(staticSearch && euipCompare){
                role.getAchievementVo().getAchievementHashMap().put(achievId,true);
            }
        }
    }

    //完成某一系列任务，例如首次成就的系列任务
    public static void ifAchievSerialTask(Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = "completeTask".equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            if(staticSearch){
                String[] strings = AchieveResource.getAchieveStaticHashMap().get(achievId).getSerial();
                System.out.println(Arrays.toString(strings));
                for(int i=0;i<strings.length;i++){
                    if(role.getAchievementVo().getAchievementHashMap().get(Integer.parseInt(strings[i]))==false){
                        return;
                    }
                }
                role.getAchievementVo().getAchievementHashMap().put(achievId,true);
            }
        }
    }

    //获得角色当前所有成就信息
    public static String getAchievmentList(Role role){
        String result="";
        for(Integer achieveId : AchieveResource.getAchieveStaticHashMap().keySet()){
            result+=AchieveResource.getAchieveStaticHashMap().get(achieveId).getDesc()+"："
                    +role.getAchievementVo().getAchievementHashMap().get(achieveId)+"\n";
        }
        return result;
    }
}
