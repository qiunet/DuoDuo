package org.qiunet.flash.handler.context.status;

import com.google.common.collect.Maps;

import java.util.Map;

/***
 * 状态结果的异常.
 * 在逻辑中抛出. 会发送给客户端.
 *
 * @author qiunet
 * 2021-01-14 15:41
 */
public class StatusResultException extends RuntimeException {

	private final IGameStatus status;

	private final Object [] args;

	private static final Map<IGameStatus, StatusResultException> cached = Maps.newConcurrentMap();

	private StatusResultException(IGameStatus status, Object... args) {
		super(status.getDesc());
		this.status = status;
		this.args = args;
	}

	public static StatusResultException valueOf(StatusResult result) {
		return valueOf(result.getStatus(), result.getParams());
	}

	public static StatusResultException valueOf(IGameStatus status, Object... args) {
		if (args == null || args.length == 0) {
			return cached.computeIfAbsent(status, StatusResultException::new);
		}

		return new StatusResultException(status, args);
	}

	public IGameStatus getStatus() {
		return status;
	}

	public Object[] getArgs() {
		return args;
	}
}
