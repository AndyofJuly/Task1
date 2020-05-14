package com.service;

import com.dao.ConnectSql;
import com.entity.Scene;
import com.netty.server.ServerHandler;

import static com.scenes.ScenesConstruct.scenesAll;

/**
 * 角色的相关方法
 * @Author andy
 * @create 2020/5/13 18:18
 */
public class RoleService {
    ConnectSql connectSql = new ConnectSql();

    public String move(String moveTarget){
        if(connectSql.moveTo(moveTarget)){
            return "移动成功";
        }else {
            return "不可以从这里去这个地方";
        }
    }

    public String aoi(){
        String s = connectSql.selectRoleScenes(ServerHandler.role.getName());
        StringBuilder stringBuilder = new StringBuilder("当前场景为："+s+"；");
        for(Scene o:scenesAll) {
            if(o.getName().equals(s)){
                stringBuilder.append("角色：");
                for(int i=0;i<o.getRoleAll().size();i++) {
                    stringBuilder.append(o.getRoleAll().get(i).getName()).append(" ");
                }
                stringBuilder.append("。 ");
                stringBuilder.append("NPC：");
                for(int i=0;i<o.getNpcAll().size();i++) {
                    stringBuilder.append(o.getNpcAll().get(i).getName()).append(" ");
                }
                stringBuilder.append("。 ");
                stringBuilder.append("怪物：");
                for(int i=0;i<o.getMonsterAll().size();i++) {
                    stringBuilder.append(o.getMonsterAll().get(i).getName()).append(" ");
                }
            }
        }
        return stringBuilder.toString();
    }

    public String checkPlace(String scenesName){
        StringBuilder stringBuilder = new StringBuilder("要查看的场景为："+scenesName+"；");
        for(Scene o:scenesAll) {
            if(o.getName().equals(scenesName)){
                stringBuilder.append("角色：");
                for(int i=0;i<o.getRoleAll().size();i++) {
                    stringBuilder.append(o.getRoleAll().get(i).getName()+" ");
                }
                stringBuilder.append("。 ");
                stringBuilder.append("NPC：");
                for(int i=0;i<o.getNpcAll().size();i++) {
                    stringBuilder.append(o.getNpcAll().get(i).getName()).append(" ");
                }
                stringBuilder.append("。 ");
                stringBuilder.append("怪物：");
                for(int i=0;i<o.getMonsterAll().size();i++) {
                    stringBuilder.append(o.getMonsterAll().get(i).getName()).append(" ");
                }
            }
        }
        return stringBuilder.toString();
    }
}
