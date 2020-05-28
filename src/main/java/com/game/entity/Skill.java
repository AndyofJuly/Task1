package com.game.entity;

import com.game.entity.excel.SkillStatic;

import java.time.Instant;
import java.util.Date;

/**
 * @Author andy
 * @create 2020/5/28 10:51
 */
public class Skill {
    //private Date date = new Date(0);
    private Instant start;
    private Instant now;
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

    public Instant getNow() {
        return now;
    }

    public void setNow(Instant now) {
        this.now = now;
    }
/*    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }*/

    public SkillStatic getSkillStatic() {
        return skillStatic;
    }

    public void setSkillStatic(SkillStatic skillStatic) {
        this.skillStatic = skillStatic;
    }
}
