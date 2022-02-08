package org.qiunet.utils.math;

/***
 * 一些位运算方法
 *
 * @author qiunet
 * 2020-04-01 17:32
 ***/
public final class BitUtil {

	private BitUtil(){}

	public static int writeBit(int ori, int val, int index) {
		return ori | (val << index);
	}

	public static int readBit(int ori, int index, int len) {
		int flag = 0;
		for (int i = 0; i < len; i++) {
			flag |= (1 << i);
		}
		ori >>= index;
		return ori & flag;
	}

	/**
	 * 给ori值设置index位值
	 * 超大值可以参考{@link java.util.BitSet}
	 *
	 * @param ori 原始值
	 * @param index index
	 * @return 最新值
	 */
	public static int setBit(int ori, int index) {
		return ori | (1 << index);
	}
	/**
	 * 给ori值取消设置index位值
	 * @param ori 原始值
	 * @param index index
	 * @return 最新值
	 */
	public static int removeBit(int ori, int index) {
		 return ori & (~(1 << index));
	}
	/**
	 * 判断ori值index位是否有值
	 * @param ori 原始值
	 * @param index index
	 * @return 最新值
	 */
	public static boolean isBitSet(int ori, int index) {
		return (ori & (1 << index)) != 0;
	}
}
