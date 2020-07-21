package com.game.system.gameserver;

import com.game.common.Const;
import com.game.system.bag.GoodsDao;
import com.game.system.role.pojo.CareerResource;
import com.game.system.role.pojo.Role;
import com.game.system.scene.MonsterWalk;
import com.game.system.scene.NpcWalk;
import com.game.system.scene.SceneService;
import com.game.system.scene.pojo.*;
import com.game.system.skill.pojo.Skill;
import com.game.system.union.UnionDao;
import com.game.system.skill.pojo.SkillResource;
import com.game.system.role.MpRecover;
import com.game.system.role.RoleService;

import java.time.Instant;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 启动服务器后初始化游戏
 * @Author andy
 * @create 2020/6/5 10:48
 */
public class InitGame {

    static {
        //场景初始化-创建的临时场景时初始化小网格
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
        //场景中生成少量怪物-两重循环，i个场景，每个场景遍历含有的怪物，实例化这些怪物
        for(Integer i : SceneResource.getScenesStatics().keySet()){
            if(SceneResource.getScenesStatics().get(i).getMonsterId()==null){continue;}
            for(int j=0;j<SceneResource.getScenesStatics().get(i).getMonsterId().length;j++) {
                //静态资源的怪物id
                int key = SceneResource.getScenesStatics().get(i).getMonsterId()[j];
                //此处每个场景生成一个对应静态的怪
                String monsterId = UUID.randomUUID().toString();
                Monster monster = new Monster(monsterId,key);
                GlobalInfo.getScenes().get(i).getMonsterHashMap().put(monsterId, monster);
                //将怪物放在单个网格中
                int gridId = SceneService.getGridId(monster.getPosition()[0],monster.getPosition()[1]);
                Scene scene = GlobalInfo.getScenes().get(i);
                scene.getGridHashMap().get(gridId).getGridMonsterMap().put(monsterId,monster);
                monster.setSceneId(i);

                //怪物随机移动-测试某场景，如果要所有场景的所有怪物都移动，删除此判断条件即可
                if(i==10006){
                    pool.submit(new MonsterWalk(monster));

                    //再次生成一些已生成过的怪物；//bug:如果要观察怪物的格子移动，需要休眠才可能创建出第二个线程；不观察则可以直接创建出第二个线程
/*                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    String monsterId2 = UUID.randomUUID().toString();
                    Monster monster2 = new Monster(monsterId2,key);
                    GlobalInfo.getScenes().get(i).getMonsterHashMap().put(monsterId2, monster2);
                    int gridId2 = SceneService.getGridId(monster2.getPosition()[0],monster2.getPosition()[1]);
                    scene.getGridHashMap().get(gridId2).getGridMonsterMap().put(monsterId2,monster2);
                    pool.submit(new MonsterWalk(monster2));
                }
            }
        }

        //实体化npc
        for(Integer i : SceneResource.getScenesStatics().keySet()){
            if(SceneResource.getScenesStatics().get(i).getNpcId()==null){continue;}
            for(int j=0;j<SceneResource.getScenesStatics().get(i).getNpcId().length;j++) {
                int key = SceneResource.getScenesStatics().get(i).getNpcId()[j];
                Npc npc = new Npc(key);
                GlobalInfo.getScenes().get(i).getNpcHashMap().put(key,npc);

                //将npc放在单个网格中
                int gridId = SceneService.getGridId(npc.getPosition()[0],npc.getPosition()[1]);
                Scene scene = GlobalInfo.getScenes().get(i);
                scene.getGridHashMap().get(gridId).getGridNpcMap().put(key,npc);

                //npc随机移动-测试某一场景的npc，如果要所有场景的所有npc都移动，删除此判断条件即可
                if(i==10006){
                    pool.submit(new NpcWalk(npc));
                }
            }
        }

        //游戏通用共享资源-启动游戏时从数据库中读取
        UnionDao unionDao = new UnionDao();
        unionDao.selectUnion();
        unionDao.selectUnionMemb();
        unionDao.selectUnionStore();
        GoodsDao goodsDao = new GoodsDao();
        goodsDao.getEquipment();
    }


    /** 是否成功进入游戏，是则对角色进行初始化*/
    private static boolean enterSuccess = false;

    public static void init(Role role){
        Instant start = Instant.now();

        //技能初始化
        for (Integer key : SkillResource.getSkillStaticHashMap().keySet()) {
            role.getSkillHashMap().put(key,new Skill(key));
            role.getSkillHashMap().get(key).setStart(start);
        }

        //场景中增加该角色
        GlobalInfo.getScenes().get(role.getNowScenesId()).getRoleAll().add(role);
        //在场景小网格中增加该角色
        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        scene.getGridHashMap().get(role.getCurGridId()).getGridRoleMap().put(role.getId(),role);
        //对角色的九宫格视野进行初始化
        role.setViewGridBo(new ViewGridBo(role.getId()));

        //初始化最大血量和蓝量
        role.setMaxHp(CareerResource.getCareerStaticHashMap().get(role.getCareerId()).getHp());
        role.setMaxMp(CareerResource.getCareerStaticHashMap().get(role.getCareerId()).getMp());
        RoleService.checkAndSetHp(role.getMaxHp(),role);
        RoleService.checkAndSetMp(role.getMaxMp(),role);

        //确定进入到游戏中
        if(enterSuccess){
            run();
        }
        enterSuccess = false;
    }

    private static void run() {
        Timer timer = new Timer();
        MpRecover mpRecover = new MpRecover();
        timer.schedule(mpRecover, Const.DELAY_TIME, Const.GAP_TIME_POTION);
    }

    public static boolean isEnterSuccess() {
        return enterSuccess;
    }

    public static void setEnterSuccess(boolean enterSuccess) {
        InitGame.enterSuccess = enterSuccess;
    }
}
