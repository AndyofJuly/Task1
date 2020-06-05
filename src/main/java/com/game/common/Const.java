package com.game.common;

/**
 * @Author andy
 * @create 2020/6/2 16:39
 */
public class Const {

    //角色第一次进入游戏时的初始场景
    public static final int INIT_SCENE = 10001;
    //角色等级id，测试用
    public static final int TYPE_ID = 1;
    //MP每秒自动回复值
    public static final int RECOVER_MP = 1;
    //技能每次释放，武器耐久下降值
    public static final int DURA_MINUS = 1;
    //持有武器时，技能伤害加成
    public static final int WEAPON_BUFF = 2;
    //击杀怪物时获得2点攻击力加成
    public static final int ABTAIN_ATK = 2;
    //s转换为ms
    public static final int TO_MS = 1000;
    //MP的持续回复药物，回复的间隔时间
    public static final int GAP_TIME_POTION = 10000;
    //MP的持续回复药物，回复的间隔时间
    public static final int GAP_TIME_SKILL = 1000;
    //延时时间
    public static final int DELAY_TIME = 0;

    //怪物死后，通知消息
    public static final String MONSTER_MESSEAGE = "the monster is dead. \n";

    //excel常量数据配置表地址
    public static final String SCENE_CONST_PATH = ".\\src\\main\\resources\\scenes.xls";
    public static final String NPC_CONST_PATH = ".\\src\\main\\resources\\npc.xls";
    public static final String MONSTER_CONST_PATH = ".\\src\\main\\resources\\monster.xls";
    public static final String SKILL_CONST_PATH = ".\\src\\main\\resources\\skill.xls";
    public static final String EQUIPMENT_CONST_PATH = ".\\src\\main\\resources\\equipment.xls";
    public static final String POTION_CONST_PATH = ".\\src\\main\\resources\\potion.xls";
    public static final String ROLE_CONST_PATH = ".\\src\\main\\resources\\role.xls";

    //数据库连接信息
    public static final String URL = "jdbc:mysql://localhost:3306/test?&useSSL=false&serverTimezone=UTC";
    public static final String USER = "root";
    public static final String PW = "123456";
    public static final String DRIVER = "com.mysql.jdbc.Driver";

    //扩展测试-蓝药缓慢回复
    public static final int MP_ID = 2001;
    //扩展测试-技能类型
    public static final int POISON_TYPE = 1;
    public static final int SHIELD_TYPE = 2;

}
