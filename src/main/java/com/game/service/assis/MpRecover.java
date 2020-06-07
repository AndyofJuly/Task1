package com.game.service.assis;

import com.game.common.Const;
import com.game.common.ExcelToJson;
//import com.game.common.InitStaticResource;
import com.game.controller.FunctionService;
import com.game.entity.store.RoleResource;

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
        for(Integer key : FunctionService.roleHashMap.keySet()){
            int mp=FunctionService.roleHashMap.get(key).getMp()+ Const.RECOVER_MP;
            if(mp >= RoleResource.roleStaticHashMap.get(FunctionService.roleHashMap.get(key).getLevelId()).getLevelMp()){
                //getRoleStatic().getLevelMp()){
                System.out.println("测试用提示：角色"+FunctionService.roleHashMap.get(key).getName()+"的mp恢复满了");
                return;
            }
            FunctionService.roleHashMap.get(key).setMp(mp);
            System.out.println("现在"+FunctionService.roleHashMap.get(key).getName()+"的mp为："+FunctionService.roleHashMap.get(key).getMp());
        }
    }
}
