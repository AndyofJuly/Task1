package com.game.system.bag;

import com.game.system.gameserver.GameController;
import com.game.system.gameserver.GlobalInfo;
import com.game.common.MyAnnontation;
import com.game.common.ResponseInf;
import com.game.system.role.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

/**
 * 背包模块调用方法入口
 * @Author andy
 * @create 2020/7/21 11:40
 */
@Controller("packageController")
public class PackageController {

    private ArrayList<Integer> intList = GameController.getIntList();

    private PackageService packageService = PackageService.getInstance();

    /** 修理装备，使用方式：repair equipmentId */
    @MyAnnontation(MethodName = "repair")
    public ResponseInf repair(){
        String msg = packageService.repairEquipment(intList.get(0),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 穿戴装备，使用方式：putOn equipmentId */
    @MyAnnontation(MethodName = "putOn")
    public ResponseInf putOn(){
        String msg = packageService.putOnEquipment(intList.get(0),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 获取当前武器耐久，使用方式：dura */
    @MyAnnontation(MethodName = "dura")
    public ResponseInf getWeaponDura(){
        return ResponseInf.setResponse(packageService.getWeaponDura(getRole()),getRole());
    }

    /** 卸下装备，使用方式：takeOff wearPosition */
    @MyAnnontation(MethodName = "takeOff")
    public ResponseInf takeOff(){
        String msg = packageService.takeOffEquipment(intList.get(0),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 使用药品，使用方式：use potionId */
    @MyAnnontation(MethodName = "use")
    public ResponseInf use(){
        return ResponseInf.setResponse(packageService.useDrug(intList.get(0),getRole()),getRole());
    }

    /** 背包整理&获取背包信息，使用方式：getOrderPackage-bag */
    @MyAnnontation(MethodName = "bag")
    public ResponseInf getOrderPackage(){
        return ResponseInf.setResponse(packageService.orderPackage(getRole()),getRole());
    }

    /** 根据输入获得角色，输入参数最后一位为roleId */
    public Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }

}
