package com.game.common;

import com.game.netty.server.NettyServer;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 反射类，能根据输入去查找对应有@Controller注解的类，执行对应方法
 * @Author andy
 * @create 2020/5/18 18:10
 */
public class ReflectService {

    private ResponseInf object;
    private ApplicationContext springContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    /**
     * 反射
     * @param inputString 经过服务端处理的字符串命令
     * @return 封装的消息及对象
     */
    public ResponseInf getMethod(String inputString) {
        Reflections reflections = new Reflections("com.game.system");
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
                            object= (ResponseInf)method.invoke(springContext.getBean(lowerFirst(clazz.getSimpleName())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return object;
    }

    private String lowerFirst(String string){
        char[] chars = string.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

}