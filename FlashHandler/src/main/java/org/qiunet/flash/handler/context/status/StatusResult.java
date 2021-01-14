package org.qiunet.flash.handler.context.status;

import com.google.common.collect.Maps;

import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2021-01-14 10:38
 */
public class StatusResult {
	public static final StatusResult SUCCESS = valueOf(IGameStatus.SUCCESS);

	/**
	 * 消耗是否成功
	 */
	private IGameStatus status;
	/**
	 * 其它参数
	 */
	private Object [] params;


	private static final Map<IGameStatus, StatusResult> cached = Maps.newConcurrentMap();
	protected StatusResult(IGameStatus status, Object... params) {
		this.status = status;
		this.params = params;
	}

	public static StatusResult valueOf(IGameStatus status, Object ... params) {
		if (params == null || params.length == 0) {
			return cached.computeIfAbsent(status, StatusResult::new);
		}
		return new StatusResult(status, params);
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
