package com.game.common;

import com.game.service.assist.ResponseInf;
import org.reflections.Reflections;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 反射类，能根据输入去查找对应有@Service-Controller注解的类，执行对应方法，这些注解类均写在controller包下
 * @Author andy
 * @create 2020/5/18 18:10
 */
public class ReflectService {
    private ResponseInf getString;
    //反射
    public ResponseInf getMethod(String inputString) {
        Reflections reflections = new Reflections("com.game.controller");
        //获取带Service注解的类
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
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
                            getString= (ResponseInf)method.invoke(clazz.newInstance());
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
