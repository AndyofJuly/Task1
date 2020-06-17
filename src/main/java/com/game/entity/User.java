package com.game.entity;

/**
 * 用户类
 * @Author andy
 * @create 2020/5/13 17:09
 */
public class User {
    // 用户id
    private int id;
    // 用户名
    private String username;
    // 用户密码
    private String password;
    // 用户的游戏角色-可改为存储id
    private Role role;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
