package com.game.test.springtest;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Demo1 {
	
	@Test
	public void test1(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		CustomerService customerService = (CustomerService)ac.getBean("customerService");
		customerService.save();
	}
}