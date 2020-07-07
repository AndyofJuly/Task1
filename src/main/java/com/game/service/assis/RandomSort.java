package com.game.service.assis;
import java.util.Random;

public class RandomSort {

	public static int[] randSort(int size){
		int[] arr = new int[size];
		for(int i=0;i<size;i++){
			arr[i] = i;
		}
		Random random = new Random();
		for(int i=0;i<arr.length;i++){
			int p = random.nextInt(i+1);
			int tmp = arr[i];
			arr[i] = arr[p];
			arr[p] = tmp;
		}
		return arr;
	}
}