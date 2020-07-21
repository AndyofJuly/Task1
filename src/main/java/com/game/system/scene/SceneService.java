package com.game.system.scene;

import com.game.common.Const;
import com.game.system.achievement.observer.TalkNpcOb;
import com.game.system.achievement.pojo.Subject;
import com.game.system.role.pojo.Role;
import com.game.system.scene.pojo.*;
import com.game.system.dungeons.pojo.DungeonsResource;
import com.game.system.gameserver.AssistService;
import com.game.system.gameserver.GlobalInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 场景模块的业务逻辑处理
 * @Author andy
 * @create 2020/6/12 10:32
 */
@Component
public class SceneService {

    /**
     * 移动切换场景-临时场景考虑在内
     * @param lastSceneId 目标场景id
     * @param role 角色
     * @return 是否成功移动
     */
    public boolean moveTo(int lastSceneId, Role role){
        int scenesId = GlobalInfo.getScenes().get(role.getNowScenesId()).getSceneId();
        String nowPlace = SceneResource.getScenesStatics().get(scenesId).getName();
        Integer[] arr = SceneResource.getPlaces().get(AssistService.checkSceneId(nowPlace));
        //判断是否能移动到目标场景
        boolean result = false;
        for (int value : arr) {
            int sceneId = GlobalInfo.getScenes().get(lastSceneId).getSceneId();
            String innTarget = SceneResource.getScenesStatics().get(sceneId).getName();
            if (innTarget.equals(SceneResource.getScenesStatics().get(value).getName())){
                result = true;
                break;
            }
        }
        if(result){
            sendToScene(lastSceneId, role);
        }
        return result;
    }

    /**
     * 移动切换场景，或传送至某地
     * @param lastSceneId 目标场景id
     * @param role 角色
     */
    public void sendToScene(int lastSceneId, Role role){
        Grid before = GlobalInfo.getScenes().get(role.getNowScenesId()).getGridHashMap().get(role.getCurGridId());
        before.getGridRoleMap().remove(role.getId());
        Grid after = GlobalInfo.getScenes().get(lastSceneId).getGridHashMap().get(role.getCurGridId());
        after.getGridRoleMap().put(role.getId(),role);

        GlobalInfo.getScenes().get(role.getNowScenesId()).getRoleAll().remove(role);
        GlobalInfo.getScenes().get(lastSceneId).getRoleAll().add(role);
        role.setNowScenesId(lastSceneId);

        if(role.getBaby()!=null){
            role.getBaby().setScenneId(lastSceneId);
        }
    }

    /**
     * 获取并设置场景所有实体，包括场景名、角色、怪物和npc集合
     * @param sceneId 场景id
     * @return 目标场景中的所有实体对象
     */
    private SceneDetailBo placeDetail(int sceneId){
        SceneDetailBo sceneDetailBo = new SceneDetailBo();
        Scene o = GlobalInfo.getScenes().get(sceneId);
        sceneDetailBo.setSceneName(o.getName());
        sceneDetailBo.setRoleArrayList(o.getRoleAll());
        sceneDetailBo.setMonsterHashMap(GlobalInfo.getScenes().get(sceneId).getMonsterHashMap());
        sceneDetailBo.setNpcHashMap(GlobalInfo.getScenes().get(sceneId).getNpcHashMap());
        return sceneDetailBo;
    }

    /**
     * 场景内移动到目标位置。约定：一次最多跨越一个格子，左右移动距离差不超过16，上下距离差不超过8；且每次移动只能改变其中一个坐标的位置；
     * @param x 水平方向移动距离
     * @param y 垂直方向移动距离
     * @param role 角色
     * @return 信息提示
     */
    public String walkTo(int x,int y,Role role){
        int oldX = role.getPosition()[0];
        int oldY = role.getPosition()[1];
        boolean xMove = Math.abs(oldX-x)>=Const.GRID_LENGTH;
        boolean yMove = Math.abs(oldY-y)>=Const.GRID_WIDTH;
        boolean xyMove = (oldX-x!=0 && oldY-y!=0);
        if(xMove || yMove || xyMove){
            return "can not move";
        }
        refleshGrid(x,y,role);
        role.getPosition()[0]=x;
        role.getPosition()[1]=y;
        return "["+role.getPosition()[0]+","+role.getPosition()[1]+"]";
    }

    /**
     * 对移动前后的小网格进行数据更新，包括移动前后小网格中的实体进行增删和更新
     * @param x 水平方向移动距离
     * @param y 垂直方向移动距离
     * @param role 角色
     */
    private void refleshGrid(int x,int y,Role role){
        int oldGridId = getGridId(role.getPosition()[0],role.getPosition()[1]);
        int newGridId = getGridId(x,y);
        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        if(oldGridId==newGridId){return;}
        scene.getGridHashMap().get(oldGridId).getGridRoleMap().remove(role.getId());
        scene.getGridHashMap().get(newGridId).getGridRoleMap().put(role.getId(),role);
        role.setCurGridId(newGridId);
    }

    /**
     * 根据坐标位置计算小网格id
     * @param x 水平方向移动距离
     * @param y 垂直方向移动距离
     * @return 小网格id
     */
    public static int getGridId(int x,int y){
        return x/Const.GRID_LENGTH+1+y/Const.GRID_WIDTH*Const.GRID_WIDTH;
    }

    /**
     * 将角色周围的九个小网格封装成一个九宫格视野对象
     * @param role 角色
     * @return 九个小网格组成的视野对象
     */
    private ViewGridBo getGridVo(Role role){
        int curGridId = role.getCurGridId();
        HashMap<Integer,Grid> viewGridHashMap = new HashMap<>();
        //清理和更新
        role.getViewGridBo().getGridRoleMap().clear();
        role.getViewGridBo().getGridMonsterMap().clear();
        role.getViewGridBo().getGridNpcMap().clear();
        //角色周围的九个网格，对三行三列的每个格子进行统计
        for(int k=0;k<3;k++){
            for(int i=curGridId-Const.GRID_WIDTH-1;i<=curGridId-Const.GRID_WIDTH+1;i++){
                Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
                viewGridHashMap.put(i,scene.getGridHashMap().get(i));
                for(Integer key : viewGridHashMap.get(i).getGridRoleMap().keySet()){
                    role.getViewGridBo().getGridRoleMap().put(key, GlobalInfo.getRoleHashMap().get(key));
                }
                for(String key : viewGridHashMap.get(i).getGridMonsterMap().keySet()){
                    Monster monster = scene.getMonsterHashMap().get(key);
                    role.getViewGridBo().getGridMonsterMap().put(key,monster);
                }
                for(Integer key : viewGridHashMap.get(i).getGridNpcMap().keySet()){
                    Npc npc = scene.getNpcHashMap().get(key);
                    role.getViewGridBo().getGridNpcMap().put(key,npc);
                }
            }
            curGridId+=Const.GRID_WIDTH;
        }
        return role.getViewGridBo();
    }

    /**
     * 打印场景详细信息，打印的怪物id为静态资源id，而非其UUID
     * @param sceneId 场景id
     * @return 信息提示
     */
    public String printSceneDetail(int sceneId){
        SceneDetailBo sceneDetailBo = placeDetail(sceneId);
        StringBuilder stringBuilder = new StringBuilder("地点："+ sceneDetailBo.getSceneName()+"\n"+"角色：");
        for(int i = 0; i< sceneDetailBo.getRoleArrayList().size(); i++) {
            stringBuilder.append(sceneDetailBo.getRoleArrayList().get(i).getName()).append(" ");
        }
        if(sceneDetailBo.getNpcHashMap()!=null){
            stringBuilder.append("\nNPC：");
            for(Integer key : sceneDetailBo.getNpcHashMap().keySet()) {
                stringBuilder.append(sceneDetailBo.getNpcHashMap().get(key).getName()).append(" ");
            }
        }
        stringBuilder.append("\n怪物：");
        for(String key: sceneDetailBo.getMonsterHashMap().keySet()){
            if(sceneDetailBo.getMonsterHashMap().get(key).getAlive()==1){
                Monster monster = sceneDetailBo.getMonsterHashMap().get(key);
                int hp = sceneDetailBo.getMonsterHashMap().get(key).getMonsterHp();
                stringBuilder.append(monster.getMonsterName()).append("-").append(monster.getMonsterId()).append("-血量：").append(hp).append(" ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 获得当前视野信息，包括其他角色、怪物和NPC
     * @param myRole 角色
     * @return 信息提示
     */
    public String printViewDetail(Role myRole){
        ViewGridBo viewGridBo = getGridVo(myRole);
        StringBuilder stringBuilder = new StringBuilder("角色：");
        for(Integer key : viewGridBo.getGridRoleMap().keySet()){
            Role role = GlobalInfo.getRoleHashMap().get(key);
            stringBuilder.append(role.getName()).
                    append(" 位置：").append(role.getPosition()[0]).append(",").append(role.getPosition()[1]).
                    append(" 网格id：").append(role.getCurGridId()).append("；");
        }
        stringBuilder.append("\n怪物：");
        for(String key : viewGridBo.getGridMonsterMap().keySet()){
            Monster monster = viewGridBo.getGridMonsterMap().get(key);
            stringBuilder.append(monster.getMonsterName()).append("-").
                    append(monster.getMonsterId()).append("-位置：").append(monster.getPosition()[0]).
                    append(",").append(monster.getPosition()[1]).append("-血量：").append(monster.getMonsterHp()).append("； ");
        }
        stringBuilder.append("\nNPC：");
        for(Integer key : viewGridBo.getGridNpcMap().keySet()){
            Npc npc = viewGridBo.getGridNpcMap().get(key);
            stringBuilder.append(npc.getName()).append("-").
                    append(npc.getNpcId()).append("-位置：").append(npc.getPosition()[0]).append(",").append(npc.getPosition()[1]).append("； ");
        }
        return stringBuilder.toString();
    }

    /**
     * 与Npc对话
     * @param npcId npcId
     * @param role  角色
     * @return String
     */
    public String getNpcReply(int npcId,Role role){
        talkNpcSubject.notifyObserver(npcId,role);
        Scene scene = GlobalInfo.getScenes().get(role.getNowScenesId());
        Npc npc = scene.getNpcHashMap().get(npcId);
        if(AssistService.isNotInView(role, npc) || AssistService.isNotInScene(npcId,role)){
            return Const.NPC_NOTICE;
        }
        return npc.getWords();
    }

    /**
     * 获得某个怪物信息
     * @param monsterId 怪物id
     * @param role 角色
     * @return 信息提示
     */
    public String getMonsterInfo(int monsterId,Role role){
        int nowScenesId = role.getNowScenesId();
        String monsterUUID = AssistService.checkMonsterId(monsterId,role);
        Monster nowMonster = GlobalInfo.getScenes().get(nowScenesId).getMonsterHashMap().get(monsterUUID);
        return "hp：" + nowMonster.getMonsterHp() + "，状态："+ nowMonster.getAlive();
    }

    /**
     * 创建临时场景
     * @param dungeonsId 副本id
     * @return 返回该临时场景id
     */
    public int createTempScene(int dungeonsId){
        int tempSceneId = AssistService.generateSceneId();
        String tempSceneName = UUID.randomUUID().toString();
        String monsterId = UUID.randomUUID().toString();

        //临时场景
        int staticSceneId = DungeonsResource.getDungeonsStaticHashMap().get(dungeonsId).getSceneId();
        Scene tempScene = new Scene(tempSceneId,tempSceneName,staticSceneId);
        GlobalInfo.getScenes().put(tempSceneId,tempScene);

        //场景怪物
        int monsterStaticId = SceneResource.getScenesStatics().get(staticSceneId).getMonsterId()[0];
        Monster monster = new Monster(monsterId,monsterStaticId);
        GlobalInfo.getScenes().get(tempSceneId).getMonsterHashMap().put(monsterId, monster);
        GlobalInfo.getTempIdHashMap().put(tempSceneId,monsterId);
        monster.setSceneId(tempSceneId);

        //怪物自由移动和普通攻击
        ExecutorService pool = Executors.newCachedThreadPool();
        pool.submit(new MonsterWalk(monster));

        initSceneGrid(tempSceneId,monster);
        return tempSceneId;
    }

    /**
     * 初始化小网格并放入怪物
     * @param tempSceneId 临时场景id
     */
    private void initSceneGrid(int tempSceneId,Monster monster){
        for(int i=1;i<=64;i++){
            GlobalInfo.getScenes().get(tempSceneId).getGridHashMap().put(i,new Grid(i));
        }
        int gridId = getGridId(monster.getPosition()[0],monster.getPosition()[1]);
        Scene scene = GlobalInfo.getScenes().get(tempSceneId);
        scene.getGridHashMap().get(gridId).getGridMonsterMap().put(monster.getId(),monster);
    }

    /**
     * 回收临时场景，解散队伍
     * @param tempSceneId 副本id
     * @param teamId 队伍id
     */
    public void deleteTempScene(int tempSceneId,String teamId){
        System.out.println("对id为"+tempSceneId+"的临时场景进行回收");
        GlobalInfo.getScenes().remove(tempSceneId);
        GlobalInfo.getTeamList().remove(teamId);
        System.gc();
    }

    /**
     * 在怪物移动导致跨越格子时更新格子
     * @param x x坐标
     * @param y y坐标
     * @param monster 怪物
     * @return 返回信息提示
     */
    public static String refleshGrid(int x,int y,Monster monster){
        int oldGridId = getGridId(monster.getPosition()[0],monster.getPosition()[1]);
        int newGridId = getGridId(x,y);
        Scene scene = GlobalInfo.getScenes().get(monster.getSceneId());
        if(oldGridId==newGridId){return "原格子Monster";}
        scene.getGridHashMap().get(oldGridId).getGridMonsterMap().remove(monster.getId());
        scene.getGridHashMap().get(newGridId).getGridMonsterMap().put(monster.getId(),monster);
        return "==怪物跨格子移动了！过去格子"+oldGridId+"。现在格子"+newGridId;
    }

    /**
     * 在Npc移动导致跨越格子时更新格子
     * @param x x坐标
     * @param y y坐标
     * @param npc npc
     * @return 返回信息提示
     */
    public static String refleshGrid(int x, int y, Npc npc){
        int oldGridId = getGridId(npc.getPosition()[0],npc.getPosition()[1]);
        int newGridId = getGridId(x,y);
        Scene scene = GlobalInfo.getScenes().get(npc.getSceneId());
        if(oldGridId==newGridId){return "原格子NPC";}
        scene.getGridHashMap().get(oldGridId).getGridNpcMap().remove(npc.getNpcId());
        scene.getGridHashMap().get(newGridId).getGridNpcMap().put(npc.getNpcId(),npc);
        return "过去格子"+oldGridId+"。现在格子"+newGridId;
    }

    /**
     * 检查并设置怪物血量，使其不超过上下界
     * @param hp 角色血量
     * @param monster 怪物
     */
    public static void checkAndSetMonsterHp(int hp,Monster monster){
        if(hp<0){
            monster.setMonsterHp(0);
            monster.setAlive(0);
        }else{
            monster.setMonsterHp(hp);
        }
    }

    /** 注册成就观察者 */
    Subject talkNpcSubject = new Subject();
    TalkNpcOb talkNpcOb = new TalkNpcOb(talkNpcSubject);
}
