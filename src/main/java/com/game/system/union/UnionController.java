package com.game.system.union;

import com.game.system.gameserver.GameController;
import com.game.system.role.pojo.Role;
import com.game.system.gameserver.GlobalInfo;
import com.game.common.ResponseInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.game.common.MyAnnontation;

import java.util.ArrayList;

/**
 * 公会模块调用方法入口
 * @Author andy
 * @create 2020/6/28 14:21
 */
@Controller("unionController")
public class UnionController {
    private ArrayList<String> strList = GameController.getStrList();
    private ArrayList<Integer> intList = GameController.getIntList();

    @Autowired
    private UnionService unionService;

    /** 创建公会，使用方式：createUnion unionName */
    @MyAnnontation(MethodName = "createUnion")
    public ResponseInf createUnion(){
        String msg = "创建成功，公会id为"+unionService.createUnion(strList.get(1),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 解散公会，使用方式：disband unionId */
    @MyAnnontation(MethodName = "disband")
    public ResponseInf disbandUnion(){
        String msg = unionService.disbandUnion(intList.get(0),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 任职某位公会成员，使用方式：appoint unionId memberId authorityLevel */
    @MyAnnontation(MethodName = "appoint")
    public ResponseInf appointCareer(){
        String msg = unionService.appointCareer(intList.get(0),intList.get(1),intList.get(2),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 入会申请，使用方式：apply unionId */
    @MyAnnontation(MethodName = "apply")
    public ResponseInf applyFor(){
        unionService.applyFor(intList.get(0),getRole());
        return ResponseInf.setResponse("已申请入会，等待审批",getRole());
    }

    //获取申请列表

    /** 批准入会申请，使用方式：agree unionId applyRoleId */
    @MyAnnontation(MethodName = "agree")
    public ResponseInf agreeApply(){
        String msg = unionService.agreeApply(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 开除某位公会成员，使用方式：fire unionId memberId */
    @MyAnnontation(MethodName = "fire")
    public ResponseInf fireMember(){
        String msg = unionService.fireMember(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    //todo 自己离开公会等方法

    /** 捐款-钱，使用方式： donate unionId money */
    @MyAnnontation(MethodName = "donateM")
    public ResponseInf donateMoney(){
        String msg = unionService.donateMoney(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 捐款-道具，使用方式：donate unionId goodsId number */
    @MyAnnontation(MethodName = "donateG")
    public ResponseInf donateGoods(){
        String msg = unionService.donateGoods(intList.get(0),intList.get(1),intList.get(2),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 拿物品，使用方式：getGoods unionId goodsId */
    @MyAnnontation(MethodName = "getGoods")
    public ResponseInf getGoods(){
        String msg = unionService.getGoods(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    /** 获得公会成员信息，使用方式：getUnionInfo */
    @MyAnnontation(MethodName = "getUnionInfo")
    public ResponseInf getUnionInfo(){
        return ResponseInf.setResponse(unionService.getUnionInfo(getRole()),getRole());
    }

    /** 根据输入获得角色，输入参数最后一位为roleId */
    private Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
