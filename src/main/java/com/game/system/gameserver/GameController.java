package com.game.system.gameserver;

import com.game.common.Const;
import com.game.common.MyAnnontation;
import com.game.common.ResponseInf;
import com.game.system.role.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

/**
 * @Author andy
 * @create 2020/7/21 12:37
 */
@Controller("gameController")
public class GameController {

    private static ArrayList<Integer> intList  = new ArrayList<>();
    private static ArrayList<String> strList  = new ArrayList<>();
/*    private ArrayList<Integer> intList = RoleController.getIntList();
    private ArrayList<String> strList = RoleController.getStrList();*/
    @Autowired
    private GameService gameService;
    @Autowired
    private GameDao gameDao;

    /** 用户注册，使用方式：register userName password */
    @MyAnnontation(MethodName = "register")
    public ResponseInf registerMesseage() {
        if(gameDao.insertRegister(strList.get(1),strList.get(2))){
            return  ResponseInf.setResponse(Const.start.UREGISTER_FAILURE,null);
        }else {
            return ResponseInf.setResponse(Const.start.UREGISTER_SUCCESS,null);
        }
    }

    /** 用户登录，使用方式：login userName password */
    @MyAnnontation(MethodName = "login")
    public ResponseInf loginMesseage() {
        int userId = gameDao.selectLogin(strList.get(1),strList.get(2));
        if(userId!=0){
            String roleMsg = "，用户id："+userId+"，你目前的角色有：";
            ArrayList<Integer> roleList = gameDao.selectRole(userId);
            for(Integer roleId : roleList){
                roleMsg += roleId+" ";
            }
            return ResponseInf.setResponse(Const.start.ULOGIN_SUCCESS+roleMsg,null);
        }else {
            return ResponseInf.setResponse(Const.start.ULOGIN_FAILURE,null);
        }
    }

    /** 角色注册，使用方式：registerR roleName careerId */
    @MyAnnontation(MethodName = "registerR")
    public ResponseInf registerRoleMesseage() { //注册时没有id，id从数据库中拿
        int result = gameService.registerRole(strList.get(1),intList.get(0));
        if(result!=0){
            gameDao.insertUserId(intList.get(1),result);
            return ResponseInf.setResponse(Const.start.REGISTER_SUCCESS+"，你的角色id为："+result,null);
        }else {
            return ResponseInf.setResponse(Const.start.REGISTER_FAILURE,null);
        }
    }

    /** 角色登录，使用方式：loginR roleId */
    @MyAnnontation(MethodName = "loginR")
    public ResponseInf loginRoleMesseage() {
        String msg = gameService.loginRole(intList.get(0));
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 手动保存数据至数据库，使用方式：save */
    @MyAnnontation(MethodName = "save")
    public ResponseInf saveDataBase() {
        return ResponseInf.setResponse(gameService.saveDataBase(),getRole());
    }

    //封装
    public static ArrayList<Integer> getIntList() {
        return intList;
    }

    public static void setIntList(ArrayList<Integer> intList) {
        GameController.intList = intList;
    }

    public static ArrayList<String> getStrList() {
        return strList;
    }

    public static void setStrList(ArrayList<String> strList) {
        GameController.strList = strList;
    }

    /** 根据输入获得角色，输入参数最后一位为roleId */
    public Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
