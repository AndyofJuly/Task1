package com.game.entity.vo;

import com.game.entity.Monster;
import com.game.entity.Npc;
import com.game.entity.Role;

import java.util.HashMap;

/**
 * 九宫格的九个格子作为一个实体
 * @Author andy
 * @create 2020/6/20 10:23
 */
public class GridVo {
    //该九宫格id，默认为角色id
    private int roleId;
    //九宫格中的角色集合
    private HashMap<Integer,Role> gridRoleMap = new HashMap<>();
    //九宫格中的怪物集合
    private HashMap<String,Monster> gridMonsterMap = new HashMap<>();
    //九宫格中的Npc集合
    private HashMap<Integer,Npc> gridNpcMap = new HashMap<>();

/*    public GridVo() {
    }*/

    public GridVo(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public HashMap<Integer, Role> getGridRoleMap() {
        return gridRoleMap;
    }

    public void setGridRoleMap(HashMap<Integer, Role> gridRoleMap) {
        this.gridRoleMap = gridRoleMap;
    }

    public HashMap<String, Monster> getGridMonsterMap() {
        return gridMonsterMap;
    }

    public void setGridMonsterMap(HashMap<String, Monster> gridMonsterMap) {
        this.gridMonsterMap = gridMonsterMap;
    }

    public HashMap<Integer, Npc> getGridNpcMap() {
        return gridNpcMap;
    }

    public void setGridNpcMap(HashMap<Integer, Npc> gridNpcMap) {
        this.gridNpcMap = gridNpcMap;
    }
}
