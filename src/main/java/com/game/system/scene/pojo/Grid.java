package com.game.system.scene.pojo;

import com.game.system.role.pojo.Role;

import java.util.HashMap;

/**
 * 网格实体，每个网格大小为角色的视野
 * 参考：CSDN博客：https://blog.csdn.net/weixin_38278878/article/details/105757545，给出了九宫格算法的思路
 * @Author andy
 * @create 2020/6/19 20:57
 */
public class Grid {

    //网格id
    private int gridId;
    //该网格中的角色
    private HashMap<Integer, Role> gridRoleMap = new HashMap<>();
    //该网格中的怪物
    private HashMap<String, Monster> gridMonsterMap = new HashMap<>();
    //该网格中的NPC
    private HashMap<Integer, Npc> gridNpcMap = new HashMap<>();

    public Grid(int gridId) {
        this.gridId = gridId;
    }

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
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
