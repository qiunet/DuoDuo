package org.qiunet.function.consume;

/**
 * 消耗的结果
 */
public class ConsumeResult {
	public static final ConsumeResult SUCCESS = new ConsumeResult(true);
	public static final ConsumeResult FAIL = new ConsumeResult(false);

	/**
	 * 消耗是否成功
	 */
	private final boolean success;

	private ConsumeResult(boolean success) {
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public boolean isFail(){
		return !isSuccess();
	}
}
