package com.game.service.impl;

import com.game.common.Const;
import com.game.dao.IGoodsDao;
import com.game.dao.IRecordDao;
import com.game.dao.IRoleDao;
import com.game.dao.IUnionDao;
import com.game.dao.impl.GoodsDaoImpl;
import com.game.dao.impl.RecordDaoImpl;
import com.game.dao.impl.RoleDaoImpl;
import com.game.dao.impl.UnionDaoImpl;
import com.game.entity.Role;
import com.game.entity.Scene;
import com.game.entity.bo.GridBo;
import com.game.service.IGameService;
import com.game.service.assist.GlobalInfo;
import com.game.service.assist.InitGame;
import com.game.service.assist.ResponseInf;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 用户一些功能的方法实现
 * @Author andy
 * @create 2020/5/11 21:50
 */

public class GameServiceImpl implements IGameService {

    private IRoleDao iRoleDao = new RoleDaoImpl();
    private IRecordDao iRecordDao = new RecordDaoImpl();
    private IUnionDao iUnionDao = new UnionDaoImpl();
    private IGoodsDao iGoodsDao = new GoodsDaoImpl();
    private static boolean saveBoolean = true;

    //角色登录
    @Override
    public String loginRole(int roleId){
        Role role = iRoleDao.selectLoginRole(roleId);
        if(role.getCareerId()!=0){
            getData(role);
            GlobalInfo.getRoleHashMap().put(roleId,role);
            InitGame.setEnterSuccess(true);
            InitGame.init(role);
            GlobalInfo.getScenes().get(role.getNowScenesId()).getRoleAll().add(role);
            //网格中增加该角色
            Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
            scene.getGridHashMap().get(role.getCurGridId()).getGridRoleMap().put(roleId,role);
            //九宫格初始化
            role.setGridBo(new GridBo(roleId));

            if(saveBoolean){
                saveAuto();
                saveBoolean=false;
            }
            return Const.start.LOGIN_SUCCESS;
        }else {
            return Const.start.LOGIN_FAILURE;
        }
    }

    @Override
    public boolean registerRole(String roleName,int careerId){
        if(iRoleDao.checkRoleName(roleName)){
            return false;
        }
        iRoleDao.insertRegisterRole(roleName,careerId);
        return true;
    }

    @Override
    public String saveDataBase(){
        saveData();
        return "已保存游戏";
    }


    private void getData(Role role){
        iRecordDao.selectAchievement(role);
        iGoodsDao.selectBodyEquipment(role);
        iGoodsDao.selectPackage(role);
    }

    private void saveData(){
        for(Integer key : GlobalInfo.getRoleHashMap().keySet()){
            Role role = GlobalInfo.getRoleHashMap().get(key);
            iRoleDao.updateRoleInfo(role);
            iRecordDao.updateAchievement(role);
            iGoodsDao.updateRoleBodyEquipment(role);
            iGoodsDao.updatePackage(role);
            iUnionDao.updateUnion(role);
            iUnionDao.updateUnionStore(role);
        }
        iUnionDao.updateUnionMemb();
    }

    private void saveAuto(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //延时time秒后，开始执行
                saveData();
                System.out.println("已自动保存游戏");
            }
        }, 30000, 60000);//暂定一分钟保存一次
    }
}