package com.game.system.assist;

import com.game.common.Const;
import com.game.system.role.pojo.Role;
import com.game.system.scene.MonsterWalk;
import com.game.system.scene.pojo.Grid;
import com.game.system.scene.pojo.Monster;
import com.game.system.scene.pojo.Scene;
import com.game.system.skill.pojo.Skill;
import com.game.system.union.IUnionDao;
import com.game.system.union.UnionDaoImpl;
import com.game.system.scene.pojo.SceneResource;
import com.game.system.skill.pojo.SkillResource;
import com.game.system.role.MpRecover;
import com.game.system.role.RoleServiceImpl;

import java.time.Instant;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author andy
 * @create 2020/6/5 10:48
 */
public class InitGame {

    static {
        //场景初始化-创建的临时场景同样要考虑初始化网格
        for(Integer keyScene : SceneResource.getScenesStatics().keySet()){
            String sceneName = SceneResource.getScenesStatics().get(keyScene).getName();
            GlobalInfo.getScenes().put(keyScene,new Scene(keyScene,sceneName,keyScene));
            //对每个场景初始化64个网格
            for(int i=1;i<=64;i++){
                GlobalInfo.getScenes().get(keyScene).getGridHashMap().put(i,new Grid(i));
            }
        }

        //线程池
        ExecutorService pool = Executors.newCachedThreadPool();
        //场景中生成少量怪物-以下可拆分到其他类例如场景初始化类
        //两重循环，n个场景，每个场景遍历含有的怪物，实例化这些怪物
        for(Integer i : SceneResource.getScenesStatics().keySet()){
            for(int j=0;j<SceneResource.getScenesStatics().get(i).getMonsterId().length;j++) {
                //静态资源的怪物id
                int key = SceneResource.getScenesStatics().get(i).getMonsterId()[j];
                //此处每个场景生成一个对应静态的怪
                String monsterId = UUID.randomUUID().toString();
                Monster monster = new Monster(monsterId,key);
                GlobalInfo.getScenes().get(i).getMonsterHashMap().put(monsterId, monster);
                //将怪物放在单个网格中
                int gridId = RoleServiceImpl.getGridId(monster.getPosition()[0],monster.getPosition()[1]);
                Scene scene = GlobalInfo.getScenes().get(i);
                scene.getGridHashMap().get(gridId).getGridMonsterMap().put(monsterId,monster);
                //System.out.println(monster.getPosition()[0]+","+monster.getPosition()[1]);

                //怪物随机移动-测试某场景，如果要所有场景的所有怪物都移动，删除此判断条件即可
                if(i==10002){
                    pool.submit(new MonsterWalk(monster));
                }
            }
        }
        System.out.println("静态代码块执行");
        //游戏通用共享资源-启动游戏时从数据库中读取
        IUnionDao iUnionDao = new UnionDaoImpl();
        iUnionDao.selectUnion();
        iUnionDao.selectUnionMemb();
        iUnionDao.selectUnionStore();
    }

    //InitRole  角色进入游戏后，初始化技能等，待修改
    private static boolean enterSuccess = false;

    public static void init(Role role){
        Instant start = Instant.now();
        //目前角色拥有四个技能，全都初始化给角色
        for (Integer key : SkillResource.getSkillStaticHashMap().keySet()) {
            role.getSkillHashMap().put(key,new Skill(key));
            role.getSkillHashMap().get(key).setStart(start);
        }

        //确定进入到游戏中
        if(enterSuccess){
            run();
        }
        enterSuccess = false;
    }

    private static void run() {
        Timer timer = new Timer();
        MpRecover mpRecover = new MpRecover();
        //程序运行后立刻执行任务，每隔10000ms执行一次
        timer.schedule(mpRecover, Const.DELAY_TIME, Const.GAP_TIME_POTION);
    }

    public static boolean isEnterSuccess() {
        return enterSuccess;
    }

    public static void setEnterSuccess(boolean enterSuccess) {
        InitGame.enterSuccess = enterSuccess;
    }
}
