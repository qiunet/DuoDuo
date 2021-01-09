package org.qiunet.function.consume;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.context.status.IGameStatus;

import java.util.Map;

/**
 * 消耗的结果
 */
public class ConsumeResult {
	public static final ConsumeResult SUCCESS = valueOf(IGameStatus.SUCCESS);

	/**
	 * 消耗是否成功
	 */
	private IGameStatus status;
	/**
	 * 其它参数
	 */
	private Object [] params;


	private static final Map<IGameStatus, ConsumeResult> cached = Maps.newConcurrentMap();
	private ConsumeResult(IGameStatus status, Object... params) {
		this.status = status;
		this.params = params;
	}

	public static ConsumeResult valueOf(IGameStatus status, Object ... params) {
		if (params == null || params.length == 0) {
			return cached.computeIfAbsent(status, ConsumeResult::new);
		}
		return new ConsumeResult(status, params);
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
