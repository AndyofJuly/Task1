package com.game.system.skill.pojo;


import java.time.Instant;

/**
 * @Author andy
 * @create 2020/5/28 10:51
 */
public class Skill {
    /** 技能释放的时间戳-是否在CD */
    private Instant start;
    /** 资源类中的静态id */
    private Integer skillId;

    public Skill(Integer skillId) {
        this.skillId = skillId;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

}
