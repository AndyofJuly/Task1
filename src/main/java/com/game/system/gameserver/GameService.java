package com.game.system.gameserver;

import com.game.common.Const;
import com.game.system.achievement.AchievementDao;
import com.game.system.bag.GoodsDao;
import com.game.system.union.UnionDao;
import com.game.system.role.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
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
    private GameDao gameDao;
    @Autowired
    private UnionDao unionDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private AchievementDao achievementDao;

    private static boolean saveBoolean = true;

    /**
     * 角色登录
     * @param roleId 角色id
     * @return 提示信息
     */
    public String loginRole(int roleId){
        Role role = gameDao.selectLoginRole(roleId);
        if(role.getCareerId()!=0){
            getData(role);
            GlobalInfo.getRoleHashMap().put(roleId,role);
            InitGame.setEnterSuccess(true);
            InitGame.init(role);
            if(saveBoolean){
                //saveAuto();
                saveBoolean=false;
            }
            return Const.Start.LOGIN_SUCCESS;
        }else {
            return Const.Start.LOGIN_FAILURE;
        }
    }

    /**
     * 角色注册
     * @param roleName 角色名
     * @param careerId 角色职业id
     * @return 是否注册成功
     */
    public int registerRole(String roleName,int careerId){
        if(gameDao.checkRoleName(roleName)){
            return 0;
        }
        return gameDao.insertRegisterRole(roleName,careerId);
    }

    /**
     * 数据持久化到数据库
     * @return 提示信息
     */
    public String saveDataBase(){
        return "已保存游戏"+saveData();
    }

    /** 加载数据库*/
    private void getData(Role role){
        achievementDao.selectAchievement(role);
        goodsDao.selectBodyEquipment(role);
        goodsDao.selectPackage(role);
    }

    /** 保存到数据库*/
    private boolean saveData(){
        for(Integer key : GlobalInfo.getRoleHashMap().keySet()){
            Role role = GlobalInfo.getRoleHashMap().get(key);
            //临时副本的场景id比普通场景id小，存库时判断可以避免卡bug
            if(role.getNowScenesId()<=Const.MAX_ID){
                role.setNowScenesId(Const.DUNGEONS_START_SCENE);
                return false;
            }
            gameDao.updateRoleInfo(role);
            achievementDao.updateAchievement(role);
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
            }
        }, 30000, 60000);
    }
}
