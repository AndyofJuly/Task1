package com.game.service.assist;

import com.game.entity.Monster;
import com.game.entity.Role;

/**
 * @Author andy
 * @create 2020/7/8 20:35
 */
public class ResponseInf {
    private String msg;
    private Role role;
    //以及其他任意对象，均可写在此处，然后由服务端传至客户端。新增对象时，同时修改DataInfo.proto中的结构，并在ServerHandler修改要返回的对象。

    public ResponseInf() {
    }

    public ResponseInf(String msg) {
        this.msg = msg;
    }

    public ResponseInf(String msg, Role role) {
        this.msg = msg;
        this.role = role;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static ResponseInf setResponse(String msg,Role role){
        return new ResponseInf(msg,role);
    }

}
