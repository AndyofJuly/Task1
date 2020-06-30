package com.game.entity.vo;

import java.util.ArrayList;

/**
 * 社交
 * @Author andy
 * @create 2020/6/28 22:09
 */
public class FriendVo {
    //朋友列表
    private ArrayList<Integer> friendIdList = new ArrayList<>();
    //申请列表
    private ArrayList<Integer> applyIdList = new ArrayList<>();

    public FriendVo() {
    }

    public ArrayList<Integer> getFriendIdList() {
        return friendIdList;
    }

    public void setFriendIdList(ArrayList<Integer> friendIdList) {
        this.friendIdList = friendIdList;
    }

    public ArrayList<Integer> getApplyIdList() {
        return applyIdList;
    }

    public void setApplyIdList(ArrayList<Integer> applyIdList) {
        this.applyIdList = applyIdList;
    }
}
