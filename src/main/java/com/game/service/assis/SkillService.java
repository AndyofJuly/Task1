package com.game.service.assis;

import com.game.controller.FunctionService;
import com.game.entity.Monster;
import com.game.entity.store.SkillResource;

import java.util.ArrayList;
import java.util.List;

/**
 * 职业技能相关，目前是个小demo，后续将进行改动
 * @Author andy
 * @create 2020/6/8 12:12
 */
public class SkillService {

    public static String useSkill(String skillName){
        //根据技能名找到技能类型
        int type = AssistService.checkSkillType(skillName);
        int skillId = AssistService.checkSkillId(skillName);
        return "";
    }

    public static String skillEffect(int skillId,int type){
        if(type == 0){

        }else if(type == 1){

        }else if(type == 2){

        }else if(type == 3){

        }else if(type == 4){

        }else if(type == 5){

        }
        return "";
    }

    //普通单体攻击技能，角色和怪物均可以使用
    public static int normalAttackSkill(int skillId){
        int damage = SkillResource.skillStaticHashMap.get(skillId).getAtk();
        return damage;
    }

/*    //毒素类持续扣血技能，对目标造成持续扣血，因此传入参数要有目标
    public static void toxinSkill(){
        //调用Time类
    }

    //护盾类、*/

    // 回血类技能，使用者恢复血量，传入参数要有技能使用者
    public static void recoverSkill(ArrayList<Integer> roleList, int skillId){
        //对这个角色集合中所有角色回复血量，回复的血量根据skillId对应的属性来增加
        //暂不考虑上限问题
        for(Integer key : roleList){
            FunctionService.roleHashMap.get(key).setHp(FunctionService.roleHashMap.get(key).getHp()+SkillResource.skillStaticHashMap.get(skillId).getAddHp());
        }
        //使用者mp减少，进入cd冷却，可写在外部
    }

    //群体伤害技能，传入参数要有目标；
    public static void groupAttackSkill(int sceneId, ArrayList<Integer> monsterList, int skillId){
        //对这个场景下，怪物集合中所有怪物造成伤害，造成的伤害根据skillId对应的属性来增加
        //暂不考虑下限问题
        for(Integer key : monsterList){
            Monster monster = InitGame.scenes.get(sceneId).getMonsterHashMap().get(key);
            //这个怪物列表是开启副本时的场景中的怪物，或者任何场景下的怪物集合
            monster.setMonsterHp(monster.getMonsterHp()-SkillResource.skillStaticHashMap.get(skillId).getAtk());
        }
        //使用者mp减少，进入cd冷却，可写在外部
    }

    //可扩展：boss释放群体伤害技能
/*    public static void groupAttackSkill(List<Integer> roleList, int skillId){
        //对这个角色集合中所有角色造成伤害，造成的伤害根据skillId对应的属性来增加
        for(Integer key : roleList){
            FunctionService.roleHashMap.get(key).setHp(FunctionService.roleHashMap.get(key).getHp()-SkillResource.skillStaticHashMap.get(skillId).getAtk());
        }
    }*/

    //召唤类技能，传入参数要有技能使用者
    public static void summonSkill(int roleId){
        //测试用常量类，可扩展出一个宝宝类，在配置表中配置，宝宝属于怪物类
        Monster baby = new Monster("101",30017);
        //宝宝自动攻击boss或角色正在攻击的怪物，调用怪物的技能方法，对目标怪物造成伤害
    }

    //嘲讽技能，传入参数要有目标，技能使用者
    public static void tauntSkill(int monsterId){
        //调用怪物的技能方法，对角色造成伤害

    }

}
