package org.qiunet.function.formula;

/***
 * 公式符号枚举
 *
 * @author qiunet
 * 2020-12-01 17:47
 */
public enum Sign {
	/**
	 * 加
	 */
	add('+') {
		@Override
		public double cal(double left, double right) {
			return left + right;
		}
	},
	/**
	 * 减
	 */
	sub('-'){
		@Override
		public double cal(double left, double right) {
			return left - right;
		}
	},
	/**
	 * 除
	 */
	div('/'){
		@Override
		public double cal(double left, double right) {
			return left / right;
		}
	},
	/**
	 * 乘
	 */
	multi('*'){
		@Override
		public double cal(double left, double right) {
			return left * right;
		}
	};


	private char symbol;
	Sign(char symbol) {
		this.symbol = symbol;
	}

	public static final Sign [] values = values();

	/**
	 * 计算数值
	 * @param left 左值
	 * @param right 右值
	 * @return 最终结果.
	 */
	public abstract double cal(double left, double right);

	/**
	 * 得到符号
	 * @return
	 */
	public char getSymbol() {
		return symbol;
	}

}
