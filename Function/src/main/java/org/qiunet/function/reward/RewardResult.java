package org.qiunet.function.reward;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.context.status.IGameStatus;

import java.util.Map;

/**
 * 消耗的结果
 */
public class RewardResult {
	public static final RewardResult SUCCESS = valueOf(IGameStatus.SUCCESS);

	/**
	 * 消耗是否成功
	 */
	private IGameStatus status;
	/**
	 * 其它参数
	 */
	private Object [] params;


	private static final Map<IGameStatus, RewardResult> cached = Maps.newConcurrentMap();
	private RewardResult(IGameStatus status, Object... params) {
		this.status = status;
		this.params = params;
	}

	private static RewardResult valueOf(IGameStatus status, Object ... params) {
		if (params == null || params.length == 0) {
			return cached.computeIfAbsent(status, RewardResult::new);
		}
		return new RewardResult(status, params);
	}

	public <T extends IGameStatus> T getStatus() {
		return (T) status;
	}

	public Object[] getParams() {
		return params;
	}

	public boolean isSuccess(){
		return status == IGameStatus.SUCCESS;
	}

	public boolean isFail(){
		return !isSuccess();
	}
}
