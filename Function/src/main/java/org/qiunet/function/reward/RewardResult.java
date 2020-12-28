package org.qiunet.function.reward;

/**
 * 消耗的结果
 */
public class RewardResult {
	public static final RewardResult SUCCESS = new RewardResult(true);
	public static final RewardResult FAIL = new RewardResult(false);

	/**
	 * 消耗是否成功
	 */
	private final boolean success;

	private RewardResult(boolean success) {
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public boolean isFail(){
		return !isSuccess();
	}
}
