package com.game.entity.excel;

/**
 * 角色类的一些静态属性，属性与excel中的字段一一对应
 * @Author andy
 * @create 2020/5/21 10:09
 */
public class RoleStatic {
    //待扩展；例如每升一级所需要的经验，获得的基础属性加成等等，属于静态数据，可在excel表中读取
    // 角色等级
    private int levelId;
    // 角色当前等级的Hp
    private int levelHp;
    // 角色当前等级的Mp
    private int levelMp;

    public RoleStatic(int levelId, int levelHp, int levelMp) {
        this.levelId = levelId;
        this.levelHp = levelHp;
        this.levelMp = levelMp;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getLevelHp() {
        return levelHp;
    }

    public void setLevelHp(int levelHp) {
        this.levelHp = levelHp;
    }

    public int getLevelMp() {
        return levelMp;
    }

    public void setLevelMp(int levelMp) {
        this.levelMp = levelMp;
    }
}
