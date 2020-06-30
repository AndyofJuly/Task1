package com.game.test.springtest;

import org.springframework.stereotype.Repository;

@Repository(value="customerDao")
public class CustomerDaoImpl implements CustomerDao {

	@Override
	public void save() {
		System.out.println("执行了CustomerDaoImpl的save()方法");
	}
	
}