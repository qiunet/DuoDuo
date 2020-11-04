package org.qiunet.profile;

/***
 * 策略
 *
 * @author qiunet
 * 2020-11-04 11:15
 */
public enum ProStrategy {

	sum {
		@Override
		public long reAdd(long oldVal, long newVal, long totalVal, int count) {
			return oldVal + newVal;
		}
	},

	max {
		@Override
		public long reAdd(long oldVal, long newVal, long totalVal, int count) {
			return Math.max(oldVal, newVal);
		}
	},

	min {
		@Override
		public long reAdd(long oldVal, long newVal, long totalVal, int count) {
			return Math.min(oldVal, newVal);
		}
	},

	avg {
		@Override
		public long reAdd(long oldVal, long newVal, long totalVal, int count) {
			return totalVal / count;
		}
	},
	;

	/**
	 * 重新添加数据后, 计算值
	 * @param oldVal 旧值
	 * @param newVal 新值
	 * @param totalVal 总值
	 * @param count 添加次数
	 * @return 返回计算后的值
	 */
	public abstract long reAdd(long oldVal, long newVal, long totalVal, int count);
}
