package com.game.test.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Task1{
	static int t = 0;
	//说明了time和主线程不是同一个线程，在学习一下这部分
	public static void main(String[] args) {
		long time = 1 * 1 * 10L;

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				//延时time秒后，开始执行
				System.out.println("开始计划任务。"+t);
				t++;
				if(t>=8){
					timer.cancel();
				}
			}
		}, 0, 1000);

		try {
			Thread.sleep(2000);
			t=5;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
/*		for(int i=1;i<1000;i++){
			System.out.println(i);
		}*/
	}

}