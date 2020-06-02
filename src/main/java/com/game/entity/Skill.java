package com.game.entity;

import com.game.entity.excel.SkillStatic;

import java.time.Instant;
import java.util.Date;

/**
 * @Author andy
 * @create 2020/5/28 10:51
 */
public class Skill {
    private Instant start;
    private SkillStatic skillStatic;

    public Skill(SkillStatic skillStatic) {
        this.skillStatic = skillStatic;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public SkillStatic getSkillStatic() {
        return skillStatic;
    }

    public void setSkillStatic(SkillStatic skillStatic) {
        this.skillStatic = skillStatic;
    }
}
