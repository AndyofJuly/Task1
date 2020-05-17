package com.game.common;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.*;
/**
 * 自定义注解类，用于根据输入命令自动调用指定方法
 * @author andy
 * @create 2020/5/16 19:30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAnnontation {

    String MethodName() default "none";

}