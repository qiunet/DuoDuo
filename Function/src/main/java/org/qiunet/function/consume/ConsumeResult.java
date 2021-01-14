package org.qiunet.function.consume;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.context.status.StatusResult;

import java.util.Map;

/**
 * 消耗的结果
 */
public class ConsumeResult extends StatusResult {
	public static final ConsumeResult SUCCESS = valueOf(IGameStatus.SUCCESS);
	private static final Map<IGameStatus, ConsumeResult> cached = Maps.newConcurrentMap();
	private ConsumeResult(IGameStatus status, Object... params) {
		super(status, params);
	}

	public static ConsumeResult valueOf(IGameStatus status, Object ... params) {
		if (params == null || params.length == 0) {
			return cached.computeIfAbsent(status, ConsumeResult::new);
		}
		return new ConsumeResult(status, params);
	}
}
