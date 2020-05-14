package com.scenes;

import com.dao.ConnectSql;
import com.entity.Scene;

import java.util.ArrayList;

/**
 * 场景初始化，将数据库中的场景、NPC和怪物加载到服务器中
 * @Author andy
 * @create 2020/5/12 11:47
 *
 */
public class ScenesConstruct {

    public static String[][] place = new String[4][6];

    public static ArrayList<Scene> scenesAll;

    public ScenesConstruct() {
        ConnectSql connectSql = new ConnectSql();
        scenesAll=connectSql.selectScenesAll();

        //给每个场景加入相应的属性
        for (Scene scene : scenesAll) {
            scene.setNpcAll(connectSql.selectNpcAll(scene.getName()));
            scene.setMonsterAll(connectSql.selectMonsterAll(scene.getName()));
            place[scene.getX()][scene.getY()] = scene.getName();
        }
    }
}
