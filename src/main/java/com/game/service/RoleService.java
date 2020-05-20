package com.game.service;

import com.game.common.ExcelToJson;
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
        String scenesName = ExcelToJson.scenes.get(FunctionService.role.getNowScenesId()).getName();
        return placeDetail(scenesName);
    }

    public String checkPlace(String scenesName){
        return placeDetail(scenesName);
    }

    public String placeDetail(String scenesName){
        StringBuilder stringBuilder = new StringBuilder("要查看的场景为："+scenesName+"；");
        for(int j = ExcelToJson.initSceneId; j< ExcelToJson.initSceneId+ExcelToJson.scenes.size(); j++){
            if(ExcelToJson.scenes.get(j).getName().equals(scenesName)){
                Scene o = ExcelToJson.scenes.get(j);
                stringBuilder.append("角色：");
                for(int i=0;i<o.getRoleAll().size();i++) {
                    stringBuilder.append(o.getRoleAll().get(i).getName()).append(" ");
                }
                stringBuilder.append("。 ");
                stringBuilder.append("NPC：");
                for(int i=0;i<o.getNpcId().length;i++) {
                    stringBuilder.append(ExcelToJson.npcs.get(Integer.valueOf(o.getNpcId()[i])).getName()).append(" ");
                }
                stringBuilder.append("。 ");
                stringBuilder.append("怪物：");
                for(int i=0;i<o.getMonsterId().length;i++) {
                    stringBuilder.append(ExcelToJson.monsters.get(Integer.valueOf(o.getMonsterId()[i])).getName()).append(" ");
                }
                stringBuilder.append("。 ");
            }
        }
        return stringBuilder.toString();
    }

    public boolean moveTo(String moveTarget){
        //获得角色当前所在场景的坐标-通过角色的nowSceneId属性获取，在登录注册时已经获得
        String nowPlace = ExcelToJson.scenes.get(FunctionService.role.getNowScenesId()).getName();
        //将当前场景坐标与要移动的场景的坐标进行对比
        String[] arr;
        arr = ExcelToJson.places.get(nowPlace);
        result = false;

        String temp = null;
        for (String value : arr) {
            if (moveTarget.equals(ExcelToJson.scenes.get(Integer.valueOf(value)).getName())) {
                result = true;
                temp = value;
                break;
            }
        }
        //如果移动成功，当前场景剔除该角色，目标场景加入该角色
        if(result){
            ExcelToJson.scenes.get(FunctionService.role.getNowScenesId()).getRoleAll().remove(FunctionService.role);
            ExcelToJson.scenes.get(Integer.valueOf(temp)).getRoleAll().add(FunctionService.role);
            //移动后角色属性的场景id改变
            FunctionService.role.setNowScenesId(Integer.valueOf(temp));
            connectSql.insertRoleScenes(FunctionService.role.getNowScenesId());
        }
        return result;
    }
}
