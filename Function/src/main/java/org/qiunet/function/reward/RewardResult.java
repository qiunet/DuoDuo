package org.qiunet.function.reward;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.context.status.StatusResult;

import java.util.Map;

/**
 * 消耗的结果
 */
public class RewardResult extends StatusResult {
	public static final RewardResult SUCCESS = valueOf(IGameStatus.SUCCESS);
	private static final Map<IGameStatus, RewardResult> cached = Maps.newConcurrentMap();
	private RewardResult(IGameStatus status, Object... params) {
		super(status, params);
	}

	public static RewardResult valueOf(IGameStatus status, Object ... params) {
		if (params == null || params.length == 0) {
			return cached.computeIfAbsent(status, RewardResult::new);
		}
		return new RewardResult(status, params);
	}

}
