package com.game.service;

import com.game.common.Const;
import com.game.common.ExcelToJson;
import com.game.controller.FunctionService;

import java.util.TimerTask;

/**
 * @Author andy
 * @create 2020/5/28 21:50
 */
public class MpRecover extends TimerTask {
    //定时方法参考博客：Java定时器Timer,TimerTask每隔一段时间随机生成数字，地址：https://www.cnblogs.com/stsinghua/p/6419357.html
    @Override
    public void run() {
        //mp恢复方法；每隔x秒恢复1点
        int mp=FunctionService.role.getMp()+ Const.RECOVER_MP;
        if(mp>= FunctionService.role.getRoleStatic().getLevelMp()){
            System.out.println("测试用提示：mp恢复满了");
            return;
        }
        FunctionService.role.setMp(mp);
        System.out.println("现在mp为："+FunctionService.role.getMp());
    }
}
