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
	private static final Map<IGameStatus, StatusResult> cached = Maps.newConcurrentMap();
	public static final StatusResult SUCCESS = valueOf(IGameStatus.SUCCESS);
	/**
	 * 这个失败仅仅是服务器判断fail用. 如果涉及客户端需要. 请自定义详细的GameStatus.
	 */
	public static final StatusResult FAIL = valueOf(IGameStatus.FAIL);

	/**
	 * 消耗是否成功
	 */
	private final IGameStatus status;
	/**
	 * 其它参数
	 */
	private final Object [] params;


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
