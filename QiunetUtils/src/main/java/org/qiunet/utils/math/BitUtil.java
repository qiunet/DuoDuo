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


	public static int setBit(int ori, int index) {
		return ori | (1 << index);
	}

	public static boolean isBitSet(int ori, int index) {
		return (ori & (1 << index)) != 0;
	}
}
