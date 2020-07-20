package com.game.system.assist;

import com.game.common.Const;
import com.game.system.bag.GoodsDao;
import com.game.system.role.pojo.CareerResource;
import com.game.system.scene.pojo.ViewGridBo;
import com.game.system.shop.RecordDao;
import com.game.system.union.UnionDao;
import com.game.system.role.pojo.Role;
import com.game.system.scene.pojo.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 用户进入游戏和游戏中的业务逻辑处理
 * @Author andy
 * @create 2020/5/11 21:50
 */
@Service
public class GameService {

    @Autowired
    private PotralDao potralDao;// = new PotralDao();
    @Autowired
    private RecordDao recordDao;// = new RecordDao();
    @Autowired
    private UnionDao unionDao;// = new UnionDao();
    @Autowired
    private GoodsDao goodsDao;// = new GoodsDao();

    private static boolean saveBoolean = true;

    /**
     * 角色登录
     * @param roleId 角色id
     * @return 提示信息
     */
    public String loginRole(int roleId){
        Role role = potralDao.selectLoginRole(roleId);
        if(role.getCareerId()!=0){
            getData(role);
            GlobalInfo.getRoleHashMap().put(roleId,role);
            InitGame.setEnterSuccess(true);
            InitGame.init(role);
            if(saveBoolean){
                //saveAuto();
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
     * @return 是否注册成功
     */
    public int registerRole(String roleName,int careerId){
        if(potralDao.checkRoleName(roleName)){
            return 0;
        }
        return potralDao.insertRegisterRole(roleName,careerId);
    }

    /**
     * 数据持久化到数据库
     * @return 提示信息
     */
    public String saveDataBase(){
        return "保存游戏"+saveData();
    }

    /** 加载数据库*/
    private void getData(Role role){
        recordDao.selectAchievement(role);
        goodsDao.selectBodyEquipment(role);
        goodsDao.selectPackage(role);
    }

    /** 保存到数据库*/
    private boolean saveData(){
        for(Integer key : GlobalInfo.getRoleHashMap().keySet()){
            Role role = GlobalInfo.getRoleHashMap().get(key);
            //临时副本id比普通场景id小，存库时判断可以避免卡bug
            if(role.getNowScenesId()<=Const.MAX_ID){
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
        unionDao.updateUnionMember();
        goodsDao.updateEquipment();
        return true;
    }

    /** 定时器，自动保存数据到数据库*/
    private void saveAuto(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                saveData();
                System.out.println("已自动保存游戏-测试");
            }
        }, 30000, 60000);
    }
}
