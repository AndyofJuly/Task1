package com.game.system.achievement;

/**
 * 成就-枚举；新增成就时要对应修改数据库表和数据访问层，observer包中新增成就观察者类，Service中进行注册和触发
 * @Author andy
 * @create 2020/7/21 0:21
 */
public enum Achievement {
    /** 枚举 */
    killMonsterThief(100,"killMonsterThief",30003,1,null),
    levelUpA(101,"levelUp",0,20,null),
    talkNpc(102,"talkNpc",20001,1,null),
    getBestEquip(103,"getBestEquip",0,2,null),
    passDungeons(104,"passDungeons",4001,1,null),
    sumEquipLevel(105,"sumEquipLevel",0,20,null),
    addFriend(106,"addFriend",0,2,null),
    firstJoinTeam(107,"firstJoinTeam",0,1,null),
    firstJoinUnion(108,"firstJoinUnion",0,1,null),
    firstTrade(109,"firstTrade",0,1,null),
    firstPkSuccess(110,"firstPkSuccess",0,1,null),
    sumMoney(111,"sumMoney",0,150,null),
    completeTask(112,"completeTask",0,1, new Integer[]{102, 107, 108, 109}),
    killMonsterWicked(113,"killMonsterWicked",30004,2,null),
    levelUpB(114,"levelUp",0,10,null);

    private final Integer id;
    private final String desc;
    private final Integer targetId;
    private final Integer count;
    private final Integer[] serial;

    Achievement(Integer id, String desc, Integer targetId, Integer count, Integer[] serial) {
        this.id = id;
        this.desc = desc;
        this.targetId = targetId;
        this.count = count;
        this.serial = serial;
    }

    public Integer getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public Integer getCount() {
        return count;
    }

    public Integer[] getSerial() {
        return serial;
    }

}
