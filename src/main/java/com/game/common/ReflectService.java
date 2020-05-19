package com.game.common;

import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 反射类，能根据输入去查找对应的方法
 * @Author andy
 * @create 2020/5/18 18:10
 */
public class ReflectService {
    String getString;
    //反射
    public String getMethod(String inputString) {
        Reflections reflections = new Reflections("com.game.controller");
        //获取带Service注解的类
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Service.class);
        for (Class clazz : typesAnnotatedWith) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                //判断带自定义注解MyAnnontation的method
                if (method.isAnnotationPresent(MyAnnontation.class)) {
                    MyAnnontation annotation = method.getAnnotation(MyAnnontation.class);
                    annotation.MethodName();
                    if (annotation.MethodName().equals(inputString)) {
                        try {
                            //执行method
                            getString=(String) method.invoke(clazz.newInstance());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return getString;
    }
}
