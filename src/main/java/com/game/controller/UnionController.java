package com.game.controller;

import com.game.entity.Role;
import com.game.service.UnionService;
import com.game.service.assis.GlobalResource;
import com.game.service.assis.InitGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
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

    private UnionService unionService = new UnionService();

    //创建公会 createUnion unionName (roleId)
    @MyAnnontation(MethodName = "createUnion")
    public String createUnion(){
        return "创建成功，公会id为"+unionService.createUnion(strList.get(1),getRole());
    }

    //解散公会 disband unionId (roleId)
    @MyAnnontation(MethodName = "disband")
    public String disbandUnion(){
        unionService.disbandUnion(intList.get(0),getRole());
        return "解散成功";
    }

    //任职 appoint unionId memberId authorityLevel (roleId)
    @MyAnnontation(MethodName = "appoint")
    public String appointCareer(){
        unionService.appointCareer(intList.get(0),intList.get(1),intList.get(2),getRole());
        return "已任命该角色";
    }

    //入会申请 apply unionId (roleId)
    @MyAnnontation(MethodName = "apply")
    public String applyFor(){
        unionService.applyFor(intList.get(0),intList.get(1));
        return "已申请入会，等待审批";
    }

    //获取申请列表

    //批准入会申请 agree unionId applyRoleId (roleId)
    @MyAnnontation(MethodName = "agree")
    public String agreeApply(){
        unionService.agreeApply(intList.get(0),intList.get(1),getRole());
        return "已批准该角色入会";
    }

    //开除 fire unionId memberId (roleId)
    @MyAnnontation(MethodName = "fire")
    public String fireMember(){
        unionService.fireMember(intList.get(0),intList.get(1),getRole());
        return "已开除该角色";
    }

    //自己离开公会等方法

    //捐款-钱 donate unionId money (roleId)
    @MyAnnontation(MethodName = "donateM")
    public String donateMoney(){
        unionService.donateMoney(intList.get(0),intList.get(1),getRole());
        return "已捐赠银两";
    }

    //捐款-道具 donate unionId goodsId (roleId)
    @MyAnnontation(MethodName = "donateG")
    public String donateGoods(){
        unionService.donateGoods(intList.get(0),intList.get(1),getRole());
        return "已捐赠该道具";
    }

    //拿物品 getGoods unionId goodsId (roleId)
    @MyAnnontation(MethodName = "getGoods")
    public String getGoods(){
        unionService.getGoods(intList.get(0),intList.get(1),getRole());
        return "已拿取该道具";
    }

    //获得公会成员信息 getUnionInfo (roleId)
    @MyAnnontation(MethodName = "getUnionInfo")
    public String getUnionInfo(){
        return unionService.getUnionInfo(getRole());
    }

    //获得角色，适用于输入参数最后一位为roleId的情况
    public Role getRole(){
        return GlobalResource.getRoleHashMap().get(intList.get(intList.size()-1));
    }
}
