package org.qiunet.utils.math;

import java.util.Random;

public class MathUtil {
	private MathUtil(){}
	/**
	 * 随机对象 随机因子使用空闲内存. 保证跟别的机器有差异.
	 */
	private static Random random = new Random(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());
	public enum RandomType{
		/** 前闭后开 [start,end) */
		K,
		/** 闭区间 [start,end] */
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

	/***
	 * 数据的十六进制
	 * @param val
	 * @return
	 */
	public static String getHexVal(long val){
		return String.format("%x", val).toUpperCase();
	}
	/***
	 * 数据的十六进制
	 * @param val
	 * @param count 显示位数 右侧0补齐
	 * @return
	 */
	public static String getHexVal(long val, int count){
		return String.format("%0"+count+"x", val).toUpperCase();
	}
	/***
	 * 得到一个数据的二进制表示
	 * @param val
	 * @return
	 */
	public static String getBinaryVal(int val) {
		return Integer.toBinaryString(val);
	}
}
