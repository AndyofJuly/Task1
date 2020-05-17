package com.game.service;

import com.game.common.MyAnnontation;
import com.game.dao.ConnectSql;
import com.game.entity.Role;
import com.game.entity.User;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 业务逻辑处理，根据不同的输入命令通过反射原理调用不同的方法，使用了spring中的自定义注解来实现
 * @Author andy
 * @create 2020/5/17 1:09
 */
@Service
public class FunctionService {
    public static User user;
    public static Role role;
    UserService userService = new UserService();
    RoleService roleService = new RoleService();
    public static String[] strings = new String[]{};
    ConnectSql connectSql = new ConnectSql();
    public static String inputString;
    String getString;

    @MyAnnontation(MethodName = "register")
    public String registerMesseage() {
        return userService.register(strings[1],strings[2]);
    }

    @MyAnnontation(MethodName = "login")
    public String loginMesseage() {
        user = new User(strings[1],strings[2]);
        return userService.login(strings[1],strings[2]);
    }

    @MyAnnontation(MethodName = "registerR")
    public String registerRoleMesseage() {
        role = new Role(strings[1],1);
        user.setRole(role);
        role.setId(connectSql.selectRoleIdByName(strings[1]));
        return userService.registerRole(strings[1]);
    }

    @MyAnnontation(MethodName = "loginR")
    public String loginReleMesseage() {
        role = new Role(strings[1],connectSql.selectRoleScenesId(strings[1]));
        user.setRole(role);
        return userService.loginRole(strings[1]);
    }

    @MyAnnontation(MethodName = "move")
    public String moveMesseage() {
        return roleService.move(strings[1]);
    }

    @MyAnnontation(MethodName = "aoi")
    public String aoiMesseage() {
        return roleService.aoi();
    }

    @MyAnnontation(MethodName = "checkPlace")
    public String checkPlaceMesseage() {
        return roleService.checkPlace(strings[1]);
    }

    public String getMethod(String inputString) {
        Reflections reflections = new Reflections("com.game.service");
        //获取带Service注解的类
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Service.class);
        for (Class clazz : typesAnnotatedWith) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                //判断带自定义注解MyAnnontation的method
                if (method.isAnnotationPresent(MyAnnontation.class)) {
                    MyAnnontation annotation = method.getAnnotation(MyAnnontation.class);
                    if (null != annotation.MethodName() && annotation.MethodName().equals(inputString)) {
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
