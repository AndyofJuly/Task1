package com.game.common;

/**
 * @Author andy
 * @create 2020/6/2 16:39
 */
public class Const {

    //角色第一次进入游戏时的初始场景
    public static final int INIT_SCENE = 10001;
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
    //MP的持续回复药物，回复的定时间隔时间
    public static final int GAP_TIME_POTION = 2000;
    //技能定时时间
    public static final int GAP_TIME_SKILL = 1000;
    //测试怪物攻击定时时间
    public static final int GAP_TIME_BOSS = 4000;
    //测试宝宝攻击定时时间
    public static final int GAP_TIME_BABY = 4000;
    //延时时间
    public static final int DELAY_TIME = 0;

    //怪物死后，通知消息
    public static final String MONSTER_MESSEAGE = "系统提示：boss已被玩家击败！";

    //宝宝测试随机id
    public static final String BABY_RAND_ID = "101";
    //宝宝测试静态id
    public static final int BABY_ID = 6001;

    //副本出发点-统一在此处
    public static final String DUNGEONS_START_SCENE = "副本传送点";
    //public static final String DUNGEONS_START_SCENEID = "10006";
    //Boss使用技能-测试
    public static final int BOSS_SKILL_ID = 1008;

    //pk收益与损失
    public static final int PK_GET_LOST = 50;
    //副本收益
    public static final int DUNGEONS_GAIN = 50;

    //限购数量：3
    public static final int GOODS_BUG_MAX = 3;

    //角色矩形视野，矩形长的一半
    public static final int VIEW_X = 20;
    //角色矩形视野，矩形宽的一半
    public static final int VIEW_Y = 10;

    //生成id的最大范围
    public static final int Max_ID = 10000;

    //与其他实体互动的最大半径距离
    public static final int Max_OPT_DISTANCE = 10;

    //药品id头
    public static final String POTION_HEAD = "2";
    //装备id头
    public static final String EQUIPMENT_HEAD = "3";
    //药品最大叠加数
    public static final int POTION_MAX_NUM = 99;
    //装备最大叠加数
    public static final int EQUIPMENT_MAX_NUM = 1;

    //零值
    public static final int ZERO = 0;


    //excel常量数据配置表地址
    public static final String SCENE_CONST_PATH = ".\\src\\main\\resources\\scenes.xls";
    public static final String NPC_CONST_PATH = ".\\src\\main\\resources\\npc.xls";
    public static final String MONSTER_CONST_PATH = ".\\src\\main\\resources\\monster.xls";
    public static final String SKILL_CONST_PATH = ".\\src\\main\\resources\\skill.xls";
    public static final String EQUIPMENT_CONST_PATH = ".\\src\\main\\resources\\equipment.xls";
    public static final String POTION_CONST_PATH = ".\\src\\main\\resources\\potion.xls";
    //public static final String ROLE_CONST_PATH = ".\\src\\main\\resources\\role.xls";
    public static final String CAREER_CONST_PATH = ".\\src\\main\\resources\\career.xls";
    public static final String DUNGEONS_CONST_PATH = ".\\src\\main\\resources\\dungeons.xls";
    public static final String BABY_CONST_PATH = ".\\src\\main\\resources\\baby.xls";

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

    //角色职业id，测试用
    public static final int CAREER_ID = 5001;
    //战士职业id
    public static final int FIGHTER_CAREER_ID = 5001;
    //嘲讽技能id
    public static final int TAUNT_SKILL_ID = 1010;

}
