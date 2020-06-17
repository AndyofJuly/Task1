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
    // 资源类中的静态id
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

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

}
