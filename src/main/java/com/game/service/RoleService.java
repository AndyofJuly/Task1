package com.game.service;

import com.game.common.ConfigMapUtil;
import com.game.controller.FunctionService;
import com.game.dao.ConnectSql;
import com.game.entity.Scene;


/**
 * 角色一些功能的方法实现
 * @Author andy
 * @create 2020/5/13 18:18
 */
public class RoleService {
    ConnectSql connectSql = new ConnectSql();
    public boolean result;

    public String move(String moveTarget){
        if(moveTo(moveTarget)){
            return "移动成功";
        }else {
            return "不可以从这里去这个地方";
        }
    }

    public String aoi(){
        String scenesName = ConfigMapUtil.scenes.get(FunctionService.role.getNowScenesId()).getName();
        return placeDetail(scenesName);
    }

    public String checkPlace(String scenesName){
        return placeDetail(scenesName);
    }

    public String placeDetail(String scenesName){
        StringBuilder stringBuilder = new StringBuilder("要查看的场景为："+scenesName+"；");
        for(int j = 10001; j< 10001+ConfigMapUtil.sceneInfo.size(); j++){
            if(ConfigMapUtil.scenes.get(j).getName().equals(scenesName)){
                Scene o = ConfigMapUtil.scenes.get(j);
                stringBuilder.append("角色：");
                for(int i=0;i<o.getRoleAll().size();i++) {
                    stringBuilder.append(o.getRoleAll().get(i).getName()).append(" ");
                }
                stringBuilder.append("。 ");
                stringBuilder.append("NPC：");
                for(int i=0;i<o.getNpcAll().length;i++) {
                    stringBuilder.append(o.getNpcAll()[i].getName()).append(" ");
                }
                stringBuilder.append("。 ");
                stringBuilder.append("怪物：");
                for(int i=0;i<o.getMonsterAll().length;i++) {
                    stringBuilder.append(o.getMonsterAll()[i].getName()).append(" ");
                }
                stringBuilder.append("。 ");
            }
        }
        return stringBuilder.toString();
    }

    public boolean moveTo(String moveTarget){
        //获得角色当前所在场景的坐标-通过角色的nowSceneId属性获取，在登录注册时已经获得
        String nowPlace = ConfigMapUtil.scenes.get(FunctionService.role.getNowScenesId()).getName();
        System.out.println(nowPlace);
        //将当前场景坐标与要移动的场景的坐标进行对比
        int[] list;
        list = ConfigMapUtil.places.get(nowPlace);
        result = false;

        int temp = 0;
        for (int value : list) {
            System.out.println(value);
            if (moveTarget.equals(ConfigMapUtil.scenes.get(value).getName())) {
                result = true;
                temp = value;
                break;
            }
        }
        System.out.println(result);
        System.out.println(temp);
        //如果移动成功，当前场景剔除该角色，目标场景加入该角色
        if(result){
            ConfigMapUtil.scenes.get(FunctionService.role.getNowScenesId()).getRoleAll().remove(FunctionService.role);
            ConfigMapUtil.scenes.get(temp).getRoleAll().add(FunctionService.role);
            //移动后角色属性的场景id改变
            FunctionService.role.setNowScenesId(temp);
            connectSql.insertRoleScenes(FunctionService.role.getNowScenesId());
        }
        return result;
    }
}
