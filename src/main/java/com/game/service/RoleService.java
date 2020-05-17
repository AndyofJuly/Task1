package com.game.service;

import com.game.common.Const;
import com.game.dao.ConnectSql;
import com.game.entity.Scene;

import static com.game.common.Const.SCENE_NAME;

/**
 * 角色一些功能的方法实现
 * @Author andy
 * @create 2020/5/13 18:18
 */
public class RoleService {
    ConnectSql connectSql = new ConnectSql();
    public int x;
    public int y;
    public boolean result;

    public String move(String moveTarget){
        if(moveTo(moveTarget)){
            return "移动成功";
        }else {
            return "不可以从这里去这个地方";
        }
    }

    public String aoi(){
        String s = Const.SCENE_NAME[FunctionService.role.getNowScenesId()-1];
        StringBuilder stringBuilder = new StringBuilder("当前场景为："+s+"；");
        for(Scene o: Const.scenes) {
            if(o.getName().equals(s)){
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

    public String checkPlace(String scenesName){
        StringBuilder stringBuilder = new StringBuilder("要查看的场景为："+scenesName+"；");
        for(Scene o: Const.scenes) {
            if(o.getName().equals(scenesName)){
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

    //角色移动的逻辑处理
    public boolean moveTo(String moveTarget){
        //获得角色当前所在场景的坐标-通过角色的nowSceneId属性获取，在登录注册时已经获得
        x=Const.SCENE_POSITION[FunctionService.role.getNowScenesId()-1][0];
        y=Const.SCENE_POSITION[FunctionService.role.getNowScenesId()-1][1];
        //将当前场景坐标与要移动的场景的坐标进行对比

        result=moveSuccess(x,y,moveTarget);
        //如果移动成功，当前场景剔除该角色，目标场景加入该角色
        if(result){
            for(Scene scene:Const.scenes) {
                if(scene.getName().equals(SCENE_NAME[FunctionService.role.getNowScenesId()-1])){
                    scene.getRoleAll().remove(FunctionService.role);
                }
            }
            for(Scene scene:Const.scenes) {
                if(scene.getName().equals(moveTarget)){
                    int sceneId = scene.getId();
                    scene.getRoleAll().add(FunctionService.role);
                    //移动后角色属性的场景id改变
                    FunctionService.role.setNowScenesId(sceneId);
                }
            }
            //并在数据库中更新？todo 后续可以改为在退出游戏时才更新插入
            connectSql.insertRoleScenes(FunctionService.role.getNowScenesId());

        }
        return result;
    }

    public static boolean moveSuccess(int x, int y,String moveTarget){
        return moveTarget.equals(Const.place[x+1][y]) ||
                moveTarget.equals(Const.place[x][y+1]) ||
                moveTarget.equals(Const.place[x-1][y]) ||
                moveTarget.equals(Const.place[x][y-1]);
    }
}
