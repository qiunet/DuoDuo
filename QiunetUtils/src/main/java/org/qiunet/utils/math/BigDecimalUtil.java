package org.qiunet.utils.math;

import java.math.BigDecimal;

/**
 * 处理超大数据的工具类(+ - * /)
 */
public class BigDecimalUtil {
	public static BigDecimal add(double v1, double v2) {// v1 + v2
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return add(b1, b2);
	}

	public static BigDecimal sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return sub(b1, b2);
	}

	public static BigDecimal mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return mul(b1, b2);
	}

	public static BigDecimal div(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		// 2 = 保留小数点后两位   ROUND_HALF_UP = 四舍五入
		return div(b1, b2);
	}

	public static BigDecimal add(String v1, String v2) {// v1 + v2
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return add(b1, b2);
	}

	public static BigDecimal sub(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return sub(b1, b2);
	}

	public static BigDecimal mul(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return mul(b1, b2);
	}

	public static BigDecimal div(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		// 2 = 保留小数点后两位   ROUND_HALF_UP = 四舍五入
		return div(b1, b2);// 应对除不尽的情况
	}

	public static BigDecimal add(BigDecimal b1, long b2) {// v1 + v2
		return add(b1, new BigDecimal(b2));
	}

	public static BigDecimal add(String b1, BigDecimal b2) {// v1 + v2
		return add(new BigDecimal(b1), b2);
	}

	public static BigDecimal sub(BigDecimal b1, long b2) {
		return sub(b1, new BigDecimal(b2));
	}

	public static BigDecimal mul(BigDecimal b1, long b2) {
		return mul(b1, new BigDecimal(b2));
	}

	public static BigDecimal mul(BigDecimal... varr) {
		BigDecimal result = BigDecimal.ONE;
		for (BigDecimal v : varr) {
			result = mul(result, v);
		}
		return result;
	}

	public static BigDecimal mul(BigDecimal b, long... arr) {
		BigDecimal result = b;
		for (long v : arr) {
			result = mul(result, v);
		}
		return result;
	}

	public static BigDecimal mul(String b, double... arr) {
		BigDecimal result = new BigDecimal(b);
		for (double v : arr) {
			result = mul(result, new BigDecimal(String.valueOf(v)));
		}
		return result;
	}

	public static BigDecimal div(BigDecimal b1, long b2) {
		return div(b1, new BigDecimal(b2));
	}

	public static BigDecimal add(BigDecimal b1, BigDecimal b2) {// v1 + v2
		return b1.add(b2);
	}

	public static BigDecimal sub(BigDecimal b1, BigDecimal b2) {
		return b1.subtract(b2);
	}

	public static BigDecimal mul(BigDecimal b1, BigDecimal b2) {
		return b1.multiply(b2);
	}

	public static BigDecimal div(BigDecimal b1, BigDecimal b2) {
		// 2 = 保留小数点后两位   ROUND_HALF_UP = 四舍五入
		return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);// 应对除不尽的情况
	}

	public static BigDecimal add(String... arr) {
		BigDecimal result = BigDecimal.ZERO;
		for (String v : arr) {
			result = add(result, new BigDecimal(v));
		}
		return result;
	}

	public static BigDecimal mul(String... arr) {
		BigDecimal result = BigDecimal.ONE;
		for (String v : arr) {
			result = mul(result, new BigDecimal(v));
		}
		return result;
	}

	public static boolean moreThan(String b1, String b2) {
		return moreThan(new BigDecimal(b1), new BigDecimal(b2));
	}

	public static boolean lessThan(String b1, String b2) {
		return lessThan(new BigDecimal(b1), new BigDecimal(b2));
	}

	public static boolean equlsTo(String b1, String b2) {
		return equlsTo(new BigDecimal(b1), new BigDecimal(b2));
	}

	public static boolean moreThanOrEquls(String b1, String b2) {
		return moreThanOrEquls(new BigDecimal(b1), new BigDecimal(b2));
	}

	public static boolean moreThan(BigDecimal b1, double b2) {
		return moreThan(b1, new BigDecimal(String.valueOf(b2)));
	}

	public static boolean lessThan(BigDecimal b1, double b2) {
		return lessThan(b1, new BigDecimal(String.valueOf(b2)));
	}

	public static boolean equlsTo(BigDecimal b1, double b2) {
		return equlsTo(b1, new BigDecimal(String.valueOf(b2)));
	}

	public static boolean moreThanOrEquls(BigDecimal b1, double b2) {
		return moreThanOrEquls(b1, new BigDecimal(String.valueOf(b2)));
	}

	public static boolean moreThan(BigDecimal b1, BigDecimal b2) {
		return b1.compareTo(b2) == 1;
	}

	public static boolean lessThan(BigDecimal b1, BigDecimal b2) {
		return b1.compareTo(b2) == -1;
	}

	public static boolean equlsTo(BigDecimal b1, BigDecimal b2) {
		return b1.compareTo(b2) == 0;
	}

	public static boolean moreThanOrEquls(BigDecimal b1, BigDecimal b2) {
		return b1.compareTo(b2) > -1;
	}

	public static boolean lessThanOrEquls(BigDecimal b1, BigDecimal b2) {
		return b1.compareTo(b2) < 1;
	}

	public static boolean moreThanZero(BigDecimal b1) {
		return moreThan(b1, BigDecimal.ZERO);
	}

	public static boolean moreThanOrEqulsZero(BigDecimal b1) {
		return moreThanOrEquls(b1, BigDecimal.ZERO);
	}

	public static boolean lessThanZero(BigDecimal b1) {
		return lessThan(b1, BigDecimal.ZERO);
	}

	public static boolean lessThanOrEqulsZero(BigDecimal b1) {
		return lessThanOrEquls(b1, BigDecimal.ZERO);
	}

	public static void main(String[] args) {
		BigDecimal playerExp = new BigDecimal(100);
		BigDecimal lvCfgExp = new BigDecimal("4.53");
		BigDecimal sub = BigDecimalUtil.sub(playerExp, lvCfgExp);

		System.out.println(sub.toString());
		System.out.println(sub.setScale(0, BigDecimal.ROUND_DOWN).toString());
		System.out.println(sub.setScale(0, BigDecimal.ROUND_UP).toString());

		System.out.println(sub.setScale(1, BigDecimal.ROUND_DOWN).toString());
		System.out.println(sub.setScale(1, BigDecimal.ROUND_UP).toString());
	}
}
