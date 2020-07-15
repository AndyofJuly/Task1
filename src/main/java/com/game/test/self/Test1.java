package com.game.test.self;

/**
 * @Author andy
 * @create 2020/7/15 17:22
 */
public class Test1 {

    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            int getEquipId = (int) (3001 + Math.floor(Math.random()*4));
            System.out.println(getEquipId);
        }
    }
}
