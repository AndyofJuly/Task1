package com.game.system.achievement.observer;

import com.game.common.Const;
import com.game.system.achievement.pojo.AchieveResource;
import com.game.system.role.pojo.Role;

/**
 * @Author andy
 * @create 2020/7/13 10:13
 */
public class SerialTaskOb implements Observer{

/*    public SerialTaskOb(SerialTaskSB subject) {
        subject.registerObserver(this);
    }

    public SerialTaskOb() {
    }

    public static void checkSerialTask(Role role){
        //系列任务检查
        SerialTaskOb serialTaskOB = new SerialTaskOb();
        serialTaskOB.checkAchievement(0,role);
    }*/

    @Override
    public void checkAchievement(int targetId, Role role){
        for(Integer achievId : AchieveResource.getAchieveStaticHashMap().keySet()){
            boolean staticSearch = Const.achieve.TASK_SERIA.equals(AchieveResource.getAchieveStaticHashMap().get(achievId).getDesc());
            if(staticSearch){
                int[] strings = AchieveResource.getAchieveStaticHashMap().get(achievId).getSerial();
                //System.out.println(Arrays.toString(strings));
                for(int i=0;i<strings.length;i++){
                    if(role.getAchievementBo().getAchievementHashMap().get(strings[i])==false){
                        return;
                    }
                }
                role.getAchievementBo().getAchievementHashMap().put(achievId,true);
            }
        }
    }

    @Override
    public void checkAchievement(String target, Role role) {

    }
}
