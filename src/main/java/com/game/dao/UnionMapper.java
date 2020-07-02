package com.game.dao;

import com.game.dao.sql.UnionSql;
import com.game.entity.Role;

/**
 * @Author andy
 * @create 2020/7/1 17:36
 */
public class UnionMapper {

    UnionSql sql = new UnionSql();

    public void updateUnion(Role role){
        sql.updateUnion(role);
    }

    public void updateUnionStore(Role role){
        sql.updateUnionStore(role);
    }

}
