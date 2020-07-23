package com.game.system.role.entity;

/**
 * 宝宝
 * @Author andy
 * @create 2020/6/11 16:43
 */
public class Baby {

    /** 宝宝UUID，每个宝宝id唯一 */
    private String id;
    /** 宝宝攻击力 */
    private Integer babyAtk;
    /** 宝宝血量 */
    private Integer babyHp;
    /** 宝宝所在场景 */
    private Integer scenneId;
    /** 宝宝静态id */
    private Integer babyId;
    /** 宝宝是否被召唤出来 */
    private boolean ifCall;
    /** 测试-所属角色 */
    private Role role;

    public Baby(String id,Integer babyId,Role role) {
        this.id = id;
        this.babyId = babyId;
        this.role = role;
        this.babyHp= BabyResource.getBabyStatics().get(this.babyId).getHp();
        this.scenneId = role.getNowScenesId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getBabyAtk() {
        return babyAtk;
    }

    public Integer getBabyHp() {
        return babyHp;
    }

    public void setBabyHp(Integer babyHp) {
        this.babyHp = babyHp;
    }

    public void setScenneId(Integer scenneId) {
        this.scenneId = scenneId;
    }
}
