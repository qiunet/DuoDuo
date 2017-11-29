package org.qiunet.utils.math;

import java.util.Random;

public class MathUtil {
	private MathUtil(){}
	/**
	 * 随机对象
	 */
	private static Random random = new Random(System.currentTimeMillis());
	public enum RandomType{
		/** 半开半闭*/
		K,
		/** 必区间*/
		B
	}
	/**
	 * 随机数计算 [start,end ) or ]
	 * @param start
	 * @param end
	 * @param randomType
	 * @return
	 */
	public static int random(int start,int end,RandomType randomType){
		if(end-start<0){
			int smaller = end;
			end = start;
			start = smaller;
		}else if(end-start==0){
			return start;
		}
		if(randomType==RandomType.B){
			end++;
		}
		int rt = start+random.nextInt(end-start);
		return rt;
	}
	/**
	 * 随机数计算 [start,end)
	 * @param start
	 * @param end
	 * @return
	 */
	public static int random(int start,int end){
		return random(start, end, RandomType.K);
	}
	/**
	 * 随机数计算 [0,i)
	 * @param i
	 * @return
	 */
	public static int random(int i){
		return random(0, i);
	}
	/**
	 * 随机一个float
	 * @param i
	 * @return
	 */
	public static float random(float i){
		return i * random.nextFloat();
	}

	/***
	 * 是否是2的次幂数
	 * @param val
	 * @return
	 */
	public static boolean isPowerOfTwo(int val) {
		return (val & -val) == val;
	}
}
