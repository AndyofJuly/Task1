package com.game.system.role;

import com.game.common.MyAnnontation;
import com.game.system.gameserver.GameController;
import com.game.system.role.pojo.Role;
import com.game.system.gameserver.GlobalInfo;
import com.game.common.ResponseInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

/**
 * 业务逻辑处理，根据不同的输入命令通过反射原理调用不同的方法，使用了spring中的自定义注解来实现
 * 用户模块调用方法入口
 * @Author andy
 * @create 2020/5/17 1:09
 */
@Controller("roleController")
public class RoleController {

    private ArrayList<Integer> intList = GameController.getIntList();
    private ArrayList<String> strList = GameController.getStrList();

    @Autowired
    private RoleService roleService;

    /** 查看角色的信息，使用方式：查看其他角色-getInfo roleId；查看自己-getInfo*/
    @MyAnnontation(MethodName = "getInfo")
    public ResponseInf getInfo(){
        if(!getRole().getId().equals(intList.get(0))){
            Role role = GlobalInfo.getRoleHashMap().get(intList.get(0));
            return ResponseInf.setResponse(roleService.getRoleInfo(role),getRole());
        }
        return ResponseInf.setResponse(roleService.getRoleInfo(getRole()),getRole());
    }

    /** 调试代码用的测试，使用方式：test */
    @MyAnnontation(MethodName = "test")
    public ResponseInf testCode(){

        return ResponseInf.setResponse(roleService.testCode(getRole()),getRole());
    }

    /** 升级-测试，使用方式：levelUp level */
    @MyAnnontation(MethodName = "levelUp")
    public ResponseInf levelUp(){
        roleService.levelUp(intList.get(0),getRole());
        return ResponseInf.setResponse("已升级",getRole());
    }

    /** 根据输入获得角色，输入参数最后一位为roleId */
    public Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
