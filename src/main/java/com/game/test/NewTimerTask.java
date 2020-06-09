package com.game.test;

import java.util.Random;
import java.util.TimerTask;  
  
/** 
 * @author sandy https://www.cnblogs.com/stsinghua/p/6419357.html
 * TimerTask类是一个抽象类 
 */  
public class NewTimerTask extends TimerTask {

    public static int sum = 0;
    @Override  
    public void run() {  
        createRandomNumber();  
        createRandomNumberFromMathRandom();
    }
    //用纯Math中的方法来随机生成1-10之间的随机数  
    private void createRandomNumberFromMathRandom() {  
        int j=(int)(Math.round(Math.random()*10+1));
        sum = sum + j;
        System.out.println("随机生成的数字为:"+j);
        System.out.println(sum);
    }
    //用Random类的方式来随机生成1-10之间的随机数  
    private void createRandomNumber(){  
         Random random=new Random(System.currentTimeMillis());  
         int value=random.nextInt();  
         value=Math.abs(value);  
         value=value%10+1;
         sum = sum + value;
         System.out.println("新生成的数字为:"+value);
         System.out.println(sum);
    }  
} 