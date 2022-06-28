package org.qiunet.utils.math;
import org.qiunet.utils.exceptions.CustomException;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtil {
	/***游戏定为万分比.**/
	private static final long BASE_RATE = 10000;

	private MathUtil(){}
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
		int rt = start+random(end-start);
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
	 * 随机数计算 [start,end)
	 * @param start
	 * @param end
	 * @return
	 */
	public static double random(double start,double end){
		return ThreadLocalRandom.current().nextDouble(start, end);
	}
	/**
	 * 随机数计算 [0,i)
	 * @param i
	 * @return
	 */
	public static int random(int i){
		return ThreadLocalRandom.current().nextInt(i);
	}
	/**
	 * 随机一个float
	 * @param i
	 * @return
	 */
	public static float random(float i){
		return i * ThreadLocalRandom.current().nextFloat();
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

	/***
	 * 在list 里面根据权重随机个数据
	 * @param list
	 * @param <T>
	 * @return
	 */
	public static <T extends IWeightObj> T randByWeight(List<T> list) {
		int totalRandNum = list.stream().mapToInt(IWeightObj::weight).sum();
		return randByWeight(list, totalRandNum);
	}

	/***
	 * 随机一个[0, totalRandNum)值 返回对应的对象.
	 * @param list
	 * @param totalRandNum 随机总权重.
	 * @param <T>
	 * @return
	 */
	public static <T extends IWeightObj> T randByWeight(List<T> list, int totalRandNum) {
		int rand = MathUtil.random(totalRandNum) ,start = 0;
		for (T obj : list) {
			if (rand >= start && rand < start+obj.weight()) {
				return obj;
			}
			start += obj.weight();
		}
		return null;
	}

	/**
	 * 得到val的 万分之rate .
	 * (val * rate) / 10000;
	 * @param val 数值
	 * @param rate 万分比 整数 1% 表示为100
	 * @return
	 */
	public static long getByRate(long val, long rate) {
		return (val * rate)/BASE_RATE;
	}

	/**
	 * 万分比概率是否命中
	 * @param rate 概率值
	 * @return
	 */
	public static boolean isHit(int rate) {
		return random(10000) <= rate;
	}

	/**
	 * 安全的强转 long to  int
	 * @param val
	 * @return
	 */
	public static int toInt(long val) {
		int i = (int) val;
		if (i != val) {
			throw new CustomException("value {} out of int range!", val);
		}
		return i;
	}
}
