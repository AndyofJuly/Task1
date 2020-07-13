package com.game.test.selfevent;

/**
 * @Author andy
 * @create 2020/7/11 18:40
 */
public class TalkListener {

    //谈话事件监听器-观察者Observer-包括事件处理

/*    private MySource mySource;

    public TalkListener(MySource mySource) {
        this.mySource = mySource;
    }*/

    //可以遍历循环
    public void checkAchievement(MyEvent event){
        System.out.println("获得成就"+event.getDesc());
    }
}
