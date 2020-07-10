package com.game.service.assist;

import com.game.common.Const;
import com.game.entity.store.CareerResource;

import java.util.TimerTask;

/**
 * @Author andy
 * @create 2020/5/28 21:50
 */
public class MpRecover extends TimerTask {
    //定时方法参考博客：Java定时器Timer,TimerTask每隔一段时间随机生成数字，地址：https://www.cnblogs.com/stsinghua/p/6419357.html
    @Override
    public void run() {
        //mp恢复方法；每隔x秒恢复1点；假设这里只回复角色1的mp，待扩展；扩展可用循环读取所有的role，然后分别加mp
        for(Integer key : GlobalInfo.getRoleHashMap().keySet()){
            int mp= GlobalInfo.getRoleHashMap().get(key).getMp()+ Const.RECOVER_MP;
            int careerId = GlobalInfo.getRoleHashMap().get(key).getCareerId();
            if(mp <= CareerResource.getCareerStaticHashMap().get(careerId).getMp()){
                GlobalInfo.getRoleHashMap().get(key).setMp(mp);
            }
        }
    }
}