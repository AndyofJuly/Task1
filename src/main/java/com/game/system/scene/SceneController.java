package com.game.system.scene;

import com.game.system.gameserver.GameController;
import com.game.system.gameserver.GlobalInfo;
import com.game.common.Const;
import com.game.common.MyAnnontation;
import com.game.common.ResponseInf;
import com.game.system.role.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

/**
 * 场景模块调用方法入口
 * @Author andy
 * @create 2020/7/21 11:42
 */
@Controller("sceneController")
public class SceneController {

    private ArrayList<Integer> intList = GameController.getIntList();

    @Autowired
    private SceneService sceneService;

    /** 角色移动&场景切换，使用方式：move sceneId */
    @MyAnnontation(MethodName = "move")
    public ResponseInf moveMesseage() {
        if(sceneService.moveTo(intList.get(0),getRole())){
            return ResponseInf.setResponse(Const.service.MOVE_SUCCESS,getRole());
        }
        return ResponseInf.setResponse(Const.service.MOVE_FAILURE,getRole());
    }

    /** 传送，使用方式：sendTo sceneId */
    @MyAnnontation(MethodName = "sendTo")
    public ResponseInf sendToScene() {
        sceneService.sendToScene(intList.get(0),getRole());
        return ResponseInf.setResponse("传送成功",getRole());
    }

    /** 获取当前整个场景的信息，使用方式：aoi */
    @MyAnnontation(MethodName = "aoi")
    public ResponseInf aoiMesseage() {
        String msg = sceneService.printSceneDetail(getRole().getNowScenesId());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 查找任意场景信息，使用方式：：checkPlace sceneId */
    @MyAnnontation(MethodName = "checkPlace")
    public ResponseInf checkPlaceMesseage() {
        String msg = sceneService.printSceneDetail(intList.get(0));
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 与NPC对话，使用方式：talkTo npcId */
    @MyAnnontation(MethodName = "talkTo")
    public ResponseInf talkToNpc(){
        String msg = sceneService.getNpcReply(intList.get(0),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 返回怪物当前状态，使用方式：getMonster monsterId */
    @MyAnnontation(MethodName = "getMonster")
    public ResponseInf getMonster(){
        String msg = sceneService.getMonsterInfo(intList.get(0),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 获得当前视野范围内实体信息，使用方式：view */
    @MyAnnontation(MethodName = "view")
    public ResponseInf getView(){
        return ResponseInf.setResponse(sceneService.printViewDetail(getRole()),getRole());
    }

    /** 角色在场景内移动，使用方式：walk x y */
    @MyAnnontation(MethodName = "walk")
    public ResponseInf walkTo(){
        String msg = sceneService.walkTo(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 根据输入获得角色，输入参数最后一位为roleId */
    public Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
