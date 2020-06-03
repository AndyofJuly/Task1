package com.game.entity;

import com.game.entity.excel.SkillStatic;

import java.time.Instant;
import java.util.Date;

/**
 * @Author andy
 * @create 2020/5/28 10:51
 */
public class Skill {
    // 技能释放的时间戳-用于计算是否在CD
    private Instant start;
    //private SkillStatic skillStatic;
    // 技能id-静态属性
    private int skillId;

    public Skill(int skillId) {
        this.skillId = skillId;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }
    /*    public Skill(SkillStatic skillStatic) {
        this.skillStatic = skillStatic;
    }*/

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

/*    public SkillStatic getSkillStatic() {
        return skillStatic;
    }

    public void setSkillStatic(SkillStatic skillStatic) {
        this.skillStatic = skillStatic;
    }*/
}
