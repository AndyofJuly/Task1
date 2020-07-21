package com.game.system.role;

import com.game.system.gameserver.GlobalInfo;
import com.game.common.Const;
import com.game.system.role.pojo.Role;

import java.util.TimerTask;

/**
 * 角色蓝量自动恢复，Java定时器Timer，参考https://www.cnblogs.com/stsinghua/p/6419357.html
 * @Author andy
 * @create 2020/5/28 21:50
 */
public class MpRecover extends TimerTask {
    @Override
    public void run() {
        //mp恢复方法；每隔x秒恢复1点；假设这里只回复角色1的mp，待扩展；扩展可用循环读取所有的role，然后分别加mp
        for(Integer key : GlobalInfo.getRoleHashMap().keySet()){
            Role role = GlobalInfo.getRoleHashMap().get(key);
            int mp= role.getMp()+ Const.RECOVER_MP;
            if(mp <= role.getMaxMp()){
                RoleService.checkAndSetMp(mp,role);
            }
        }
    }
}
