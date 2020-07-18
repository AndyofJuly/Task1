package com.game.system.achievement.observer;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.achievement.subject.SerialTaskSB;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:04
 */
public class TalkNpcOb implements Observer{

    //AchievementService  implements ITalkNpcObserver ,IKillMonsterObserver

/*    public TalkNpcOb(TalkNpcSB subject) {
        subject.registerObserver(this);
    }*/

    @Override
    public void checkAchievement(int targetId, Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = Const.achieve.TASK_NPC.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean npcCompare = (targetId==AchieveResource.getAchieveStaticHashMap().get(achievId).getTargetId());
            if(staticSearch && npcCompare){
                role.getAchievementBo().getAchievementHashMap().put(achievId,true);
            }
        }

        SerialTaskSB.notifyObservers(0,role);
    }


    public void checkQuest(int targetId, Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = Const.achieve.TASK_NPC.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            boolean npcCompare = (targetId==AchieveResource.getAchieveStaticHashMap().get(achievId).getTargetId());
            if(staticSearch && npcCompare){
                role.getAchievementBo().getAchievementHashMap().put(achievId,true);
            }
        }

        SerialTaskSB.notifyObservers(0,role);
    }

    @Override
    public void checkAchievement(String target, Role role) {

    }

/*    @Override
    public void fireTalkNpc(Role role, int NpcId) {
        找出我们所有NPC成就
    }

    @Override
    public void fireKillMonster(Role role, int monsterId) {

    }*/
}
