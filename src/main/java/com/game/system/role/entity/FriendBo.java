package com.game.system.role.entity;

import java.util.ArrayList;

/**
 * 社交好友对象-业务对象-OB(Business object)
 * @Author andy
 * @create 2020/6/28 22:09
 */
public class FriendBo {
    /** 朋友列表-roleId */
    private ArrayList<Integer> friendIdList = new ArrayList<>();
    /** 申请列表-roleId */
    private ArrayList<Integer> applyIdList = new ArrayList<>();

    public FriendBo() {
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
