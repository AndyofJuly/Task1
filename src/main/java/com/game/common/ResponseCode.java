package com.game.common;
/**
 * @Author andy
 * @create 2020/7/2 17:38
 */
public enum  ResponseCode {

    /**
     * 枚举并封装调用方法
     */
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;

    ResponseCode(int code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public int getCode(){
        return code;
    }

    public String getDesc(){
        return desc;
    }
}