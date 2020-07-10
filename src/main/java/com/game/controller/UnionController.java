package com.game.controller;

import com.game.entity.Role;
import com.game.service.IUnionService;
import com.game.service.assist.GlobalInfo;
import com.game.service.assist.ResponseInf;
import com.game.service.impl.UnionServiceImpl;
import org.springframework.stereotype.Controller;
import com.game.common.MyAnnontation;

import java.util.ArrayList;

/**
 * 公会
 * @Author andy
 * @create 2020/6/28 14:21
 */
@Controller
public class UnionController {
    private ArrayList<String> strList = RoleController.getStrList();
    private ArrayList<Integer> intList = RoleController.getIntList();

    private IUnionService iUnionService = new UnionServiceImpl();

    //创建公会 createUnion unionName (roleId)
    @MyAnnontation(MethodName = "createUnion")
    public ResponseInf createUnion(){
        String msg = "创建成功，公会id为"+iUnionService.createUnion(strList.get(1),getRole());
        return ResponseInf.setResponse(msg,getRole());
    }

    //解散公会 disband unionId (roleId)
    @MyAnnontation(MethodName = "disband")
    public ResponseInf disbandUnion(){
        iUnionService.disbandUnion(intList.get(0),getRole());
        return ResponseInf.setResponse("解散成功",getRole());
    }

    //任职 appoint unionId memberId authorityLevel (roleId)
    @MyAnnontation(MethodName = "appoint")
    public ResponseInf appointCareer(){
        iUnionService.appointCareer(intList.get(0),intList.get(1),intList.get(2),getRole());
        return ResponseInf.setResponse("已任命该角色",getRole());
    }

    //入会申请 apply unionId (roleId)
    @MyAnnontation(MethodName = "apply")
    public ResponseInf applyFor(){
        iUnionService.applyFor(intList.get(0),intList.get(1));
        return ResponseInf.setResponse("已申请入会，等待审批",getRole());
    }

    //获取申请列表

    //批准入会申请 agree unionId applyRoleId (roleId)
    @MyAnnontation(MethodName = "agree")
    public ResponseInf agreeApply(){
        iUnionService.agreeApply(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse("已批准该角色入会",getRole());
    }

    //开除 fire unionId memberId (roleId)
    @MyAnnontation(MethodName = "fire")
    public ResponseInf fireMember(){
        iUnionService.fireMember(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse("已开除该角色",getRole());
    }

    //自己离开公会等方法

    //捐款-钱 donate unionId money (roleId)
    @MyAnnontation(MethodName = "donateM")
    public ResponseInf donateMoney(){
        iUnionService.donateMoney(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse("已捐赠银两",getRole());
    }

    //捐款-道具 donate unionId goodsId (roleId)
    @MyAnnontation(MethodName = "donateG")
    public ResponseInf donateGoods(){
        iUnionService.donateGoods(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse("已捐赠该道具",getRole());
    }

    //拿物品 getGoods unionId goodsId (roleId)
    @MyAnnontation(MethodName = "getGoods")
    public ResponseInf getGoods(){
        iUnionService.getGoods(intList.get(0),intList.get(1),getRole());
        return ResponseInf.setResponse("已拿取该道具",getRole());
    }

    //获得公会成员信息 getUnionInfo (roleId)
    @MyAnnontation(MethodName = "getUnionInfo")
    public ResponseInf getUnionInfo(){
        return ResponseInf.setResponse(iUnionService.getUnionInfo(getRole()),getRole());
    }

    //获得角色，适用于输入参数最后一位为roleId的情况
    private Role getRole(){
        return GlobalInfo.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
