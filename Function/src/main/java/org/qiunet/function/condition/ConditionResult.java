package org.qiunet.function.condition;

/**
 * 条件的结果
 */
public class ConditionResult {
	public static final ConditionResult SUCCESS = new ConditionResult(true);
	public static final ConditionResult FAIL = new ConditionResult(false);

	/**
     *  条件是否成功
	 */
	private final boolean success;

	private ConditionResult(boolean success) {
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public boolean isFail(){
		return !isSuccess();
	}
}
