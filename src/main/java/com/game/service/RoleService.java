package com.game.service;

import com.game.common.InitStaticResource;
import com.game.controller.FunctionService;
import com.game.dao.ConnectSql;
import com.game.entity.Scene;
import com.game.entity.excel.EquipmentStatic;


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
        String scenesName = InitStaticResource.scenes.get(FunctionService.role.getNowScenesId()).getSceneStatic().getName();
        return placeDetail(scenesName);
    }

    public String checkPlace(String scenesName){
        return placeDetail(scenesName);
    }

    public String placeDetail(String scenesName){
        StringBuilder stringBuilder = new StringBuilder("要查看的场景为："+scenesName+"；");
        for(int j = InitStaticResource.initSceneId; j< InitStaticResource.initSceneId+InitStaticResource.scenes.size(); j++){
            if(InitStaticResource.scenes.get(j).getSceneStatic().getName().equals(scenesName)){
                Scene o = InitStaticResource.scenes.get(j);
                stringBuilder.append("角色：");
                for(int i=0;i<o.getRoleAll().size();i++) {
                    stringBuilder.append(o.getRoleAll().get(i).getName()).append(" ");
                }
                stringBuilder.append("。 ");
                stringBuilder.append("NPC：");
                for(int i=0;i<o.getSceneStatic().getNpcId().length;i++) {
                    stringBuilder.append(InitStaticResource.npcs.get(Integer.valueOf(o.getSceneStatic().getNpcId()[i])).getNpcStatic().getName()).append(" ");
                }
                stringBuilder.append("。 ");
                stringBuilder.append("怪物：");
                for(int i=0;i<o.getSceneStatic().getMonsterId().length;i++) {
                    stringBuilder.append(InitStaticResource.monsters.get(Integer.valueOf(o.getSceneStatic().getMonsterId()[i])).getMonsterStatic().getName()).append(" ");
                }
                stringBuilder.append("。 ");
            }
        }
        return stringBuilder.toString();
    }

    public boolean moveTo(String moveTarget){
        //获得角色当前所在场景的坐标-通过角色的nowSceneId属性获取，在登录注册时已经获得
        String nowPlace = InitStaticResource.scenes.get(FunctionService.role.getNowScenesId()).getSceneStatic().getName();
        //将当前场景坐标与要移动的场景的坐标进行对比
        String[] arr;
        arr = InitStaticResource.places.get(nowPlace);
        result = false;

        String temp = null;
        for (String value : arr) {
            if (moveTarget.equals(InitStaticResource.scenes.get(Integer.valueOf(value)).getSceneStatic().getName())) {
                result = true;
                temp = value;
                break;
            }
        }
        //如果移动成功，当前场景剔除该角色，目标场景加入该角色
        if(result){
            InitStaticResource.scenes.get(FunctionService.role.getNowScenesId()).getRoleAll().remove(FunctionService.role);
            InitStaticResource.scenes.get(Integer.valueOf(temp)).getRoleAll().add(FunctionService.role);
            //移动后角色属性的场景id改变
            FunctionService.role.setNowScenesId(Integer.valueOf(temp));
            connectSql.insertRoleScenes(FunctionService.role.getNowScenesId());
        }
        return result;
    }

    public String getNpcReply(String NpcName){
        //根据名字从集合npcs中获取到需要的Npc的id
        //遍历，如果该id对应的名字与传参相同，则找出对应id的words返回
        //同时需要保证，玩家只能与当前场景内的npc对话，不可以与其他场景的npc对话
        String replyWords = "该场景没有这个npc";
        for (Integer key : InitStaticResource.npcsStatics.keySet()) {
            if(InitStaticResource.npcsStatics.get(key).getName().equals(NpcName) && FunctionService.role.getNowScenesId()==InitStaticResource.npcsStatics.get(key).getSceneId()){
                replyWords = InitStaticResource.npcsStatics.get(key).getWords();
            }
        }
        //返回words字段
        return replyWords;
    }

}
