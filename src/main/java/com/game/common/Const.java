package com.game.common;

/**
 * @Author andy
 * @create 2020/6/2 16:39
 */
public class Const {

    //角色第一次进入游戏时的初始场景
    public static final int INIT_SCENE = 10001;
    //初始赠送银两
    public static final int INIT_MONEY = 100;
    //初始背包大小
    public static final int PACKAGE_SIZE = 6;
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
    //装备耐久
    public static final int EQUIP_DURA = 100;

    //怪物死后，通知消息
    public static final String MONSTER_MESSEAGE = "系统提示：boss已被玩家击败！";

    //宝宝测试随机id
    public static final String BABY_RAND_ID = "101";
    //宝宝测试静态id
    public static final int BABY_ID = 6001;

    //副本出发点-统一在此处
    //public static final String DUNGEONS_START_SCENE = "副本传送点";
    public static final int DUNGEONS_START_SCENE = 10006;
    //Boss使用技能-测试
    public static final int BOSS_SKILL_ID = 1008;

    //pk收益与损失
    public static final int PK_GET_LOST = 50;
    //副本收益
    public static final int DUNGEONS_GAIN = 50;

    //限购数量：20
    public static final int GOODS_BUG_MAX = 20;

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
    //拍卖限定时间-秒
    public static final int AUCTION_TIME = 20000;
    //成就起始id
    public static final int ACHIEV_START = 100;

    //excel常量数据配置表地址
    public static final String SCENE_CONST_PATH = ".\\src\\main\\resources\\excel\\scenes.xls";
    public static final String NPC_CONST_PATH = ".\\src\\main\\resources\\excel\\npc.xls";
    public static final String MONSTER_CONST_PATH = ".\\src\\main\\resources\\excel\\monster.xls";
    public static final String SKILL_CONST_PATH = ".\\src\\main\\resources\\excel\\skill.xls";
    public static final String EQUIPMENT_CONST_PATH = ".\\src\\main\\resources\\excel\\equipment.xls";
    public static final String POTION_CONST_PATH = ".\\src\\main\\resources\\excel\\potion.xls";
    //public static final String ROLE_CONST_PATH = ".\\src\\main\\resources\\role.xls";
    public static final String CAREER_CONST_PATH = ".\\src\\main\\resources\\excel\\career.xls";
    public static final String DUNGEONS_CONST_PATH = ".\\src\\main\\resources\\excel\\dungeons.xls";
    public static final String BABY_CONST_PATH = ".\\src\\main\\resources\\excel\\baby.xls";
    public static final String JOB_CONST_PATH = ".\\src\\main\\resources\\excel\\job.xls";
    public static final String ACHIEVE_CONST_PATH = ".\\src\\main\\resources\\excel\\achievement.xls";

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
    //钢剑-测试
    public static final int WEPON = 3001;
    //战士职业id
    public static final int FIGHTER_CAREER_ID = 5001;
    //嘲讽技能id
    public static final int TAUNT_SKILL_ID = 1010;

    //视野单位网格长
    public static final int GRID_LENGTH = 16;
    //视野单位网格宽
    public static final int GRID_WIDTH = 8;
    //定制的攻击消息集合-暂时存在此处
    public interface Fight{
        String PK_SUCCESS = "恭喜pk成功，获得对方50银，2点攻击力!";
        String SUCCESS = "success";
        String DURA_LACK = "武器耐久不够，请先修理再战斗。";
        String MP_LACK = "角色蓝量不够，请先恢复再战斗";
        String SKILL_IN_CD = "该技能冷却中";
        String TARGET_HP ="攻击目标血量剩余";
        String DISTACNE_LACK = "距离不够";
        String SLAY_SUCCESS = "怪物血量为0，你已经打败该怪物！恭喜获得50银!获得2点攻击力加成!";
        String TEAM_LIST = "队伍列表：";
        String CREATE_SUCCESS = "已创建队伍：";
        String TEAM_ROLELIST = "当前队伍角色：";
        String START_DUNGEONS = "副本已开启";
        String SKILL_LIST = "可用技能：";
        String SUMMON_MSG = "怪物受到嘲讽";
        String GROUPATK_MSG = "使用群伤技能，场景怪物均受到攻击";
        String CURE_MSG = "使用群体治疗技能";
        String BABY_MSG = "召唤出宝宝，开始自动释放技能攻击怪物";
    }
    public static final String SEND_FAILURE = "数量不足，无法发送";
    public static final String SEND_SUCCESS = "发送邮件成功";

    //定制的购买物品消息集合-暂时存在此处
    public interface Shop{
        String LIMIT_MSG = "超过限购数量，不可以再购买了";
        String BUY_FAILURE = "您所携带的银两不够或背包已满，无法够买";
        String BUY_SUCCESS = "购买成功，目前该物品在背包中的数量为";
        String BAG_OUT_SPACE = "背包放不下了";
    }

    //登录的提示信息
    public interface start {
        String LOGIN_SUCCESS = "登陆成功，您进入到了游戏世界";
        String LOGIN_FAILURE = "您没有该角色，登陆失败";
        String UREGISTER_FAILURE = "注册失败，该用户名已有人使用";
        String UREGISTER_SUCCESS = "注册成功，请进行登录";
        String ULOGIN_SUCCESS = "登陆成功，请新增或登录角色以开始游戏";
        String ULOGIN_FAILURE = "用户名或密码错误，登陆失败";
        String REGISTER_SUCCESS = "注册成功，请进行登录";
        String REGISTER_FAILURE = "注册失败，该角色名称已有人使用";
    }
    //角色一些方法的提示信息
    public interface service {
        String MOVE_SUCCESS = "移动成功";
        String MOVE_FAILURE = "移动失败";
        String REPAIR_SUCCESS = "修理成功！当前武器耐久为：";
        String PUTON_SUCCESS = "你已成功装备该武器";
        String TAKEOFF_SUCCESS = "你已成功卸下该武器";
        String USE_SUCCESS = "使用成功";
        String USE_FAILURE = "物品已用完";
    }

    //公会参数
    public interface union {
        int FIRST_GRADE = 1;
        int SECOND_GRADE = 2;
        int THRID_GRADE = 3;
        int FOURTH_GRADE = 4;
    }

    //成就信息
    public interface achieve {
        String TASK_MONSTER = "killMonster";
        String TASK_LEVEL = "levelUp";
        String TASK_NPC = "talkNpc";
        String TASK_EQUIP = "getBestEquip";
        String TASK_DUNGEONS = "passDungeons";
        String TASK_EQUIP_LEVEL = "sumEquipLevel";
        String TASK_FRIEDN = "addFriend";
        String TASK_FIRST_TEAM = "firstJoinTeam";
        String TASK_FIRST_UNION = "firstJoinUnion";
        String TASK_FRIST_TRADE = "firstTrade";
        String TASK_PK_SUCCESS = "firstPkSuccess";
        String TASK_MONEY = "sumMoney";
        String TASK_SERIA = "completeTask";
    }
}
