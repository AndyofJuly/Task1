package com.game.system.assist;

import com.game.common.Const;
import com.game.system.bag.GoodsDao;
import com.game.system.shop.RecordDao;
import com.game.system.union.UnionDao;
import com.game.system.role.pojo.Role;
import com.game.system.scene.pojo.Scene;
import com.game.system.scene.pojo.GridBo;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 用户一些功能的方法实现
 * @Author andy
 * @create 2020/5/11 21:50
 */

public class GameService {

    private PotralDao potralDao = new PotralDao();
    private RecordDao recordDao = new RecordDao();
    private UnionDao unionDao = new UnionDao();
    private GoodsDao goodsDao = new GoodsDao();
    private static boolean saveBoolean = true;

    /**
     * 角色登录
     * @param roleId 角色id
     * @return String
     */
    //角色登录
    public String loginRole(int roleId){
        Role role = potralDao.selectLoginRole(roleId);
/*        //防止进入临时副本后无法移动出来的bug，临时副本id比普通场景id小
        if(role.getNowScenesId()<=Const.Max_ID){
            role.setNowScenesId(Const.DUNGEONS_START_SCENE);
        }*/
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

    /**
     * 角色注册
     * @param roleName 角色名
     * @param careerId 角色职业id
     * @return boolean
     */
    public boolean registerRole(String roleName,int careerId){
        if(potralDao.checkRoleName(roleName)){
            return false;
        }
        potralDao.insertRegisterRole(roleName,careerId);
        return true;
    }

    /**
     * 数据持久化到数据库
     * @return String
     */
    public String saveDataBase(){
        return "保存游戏"+saveData();
    }


    private void getData(Role role){
        recordDao.selectAchievement(role);
        goodsDao.selectBodyEquipment(role);
        goodsDao.selectPackage(role);
    }

    private boolean saveData(){

        for(Integer key : GlobalInfo.getRoleHashMap().keySet()){
            Role role = GlobalInfo.getRoleHashMap().get(key);
            //防止进入临时副本后无法移动出来的bug，临时副本id比普通场景id小
            if(role.getNowScenesId()<=Const.Max_ID){
                role.setNowScenesId(Const.DUNGEONS_START_SCENE);
                return false;
            }
            potralDao.updateRoleInfo(role);
            recordDao.updateAchievement(role);
            goodsDao.updateRoleBodyEquipment(role);
            goodsDao.updatePackage(role);
            unionDao.updateUnion(role);
            unionDao.updateUnionStore(role);
        }
        unionDao.updateUnionMemb();
        return true;
    }

    private void saveAuto(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //延时time秒后，开始执行
                saveData();
                System.out.println("已自动保存游戏-测试");
            }
        }, 30000, 60000);//暂定一分钟保存一次
    }
}
