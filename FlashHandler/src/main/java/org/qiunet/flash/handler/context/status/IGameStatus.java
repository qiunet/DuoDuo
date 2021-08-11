package org.qiunet.flash.handler.context.status;

/**
 * 游戏的状态接口
 * Created by qiunet.
 * 17/12/9
 */
public interface IGameStatus {
	/**服务器异常*/
	IGameStatus EXCEPTION = BasicGameStatus.EXCEPTION;
	/**成功*/
	IGameStatus SUCCESS = BasicGameStatus.SUCCESS;
	IGameStatus FAIL = BasicGameStatus.FAIL;
	/**请求频繁*/
	IGameStatus FAST_REQUEST = BasicGameStatus.FAST_REQUEST;
	/**Token 错误*/
	IGameStatus TOKEN_ERROR = BasicGameStatus.TOKEN_ERROR;
	/**维护中 */
	IGameStatus MAINTENANCE = BasicGameStatus.MAINTENANCE;
	/**禁止登陆 封禁了*/
	IGameStatus BAN = BasicGameStatus.BAN;

	/** 参数错误 */
	IGameStatus PARAMS_ERROR = BasicGameStatus.PARAMS_ERROR;
	/**404*/
	IGameStatus HANDLER_NOT_FOUND = BasicGameStatus.HANdLER_NOT_FOUND;
	/***
	 * 得到状态
	 * @return
	 */
	int getStatus();

	/***
	 * 得到描述
	 * @return
	 */
	String getDesc();

	/**
	 * Created by qiunet.
	 * 17/12/9
	 */
	enum BasicGameStatus implements IGameStatus {
		SUCCESS(1, "成功"),
		TOKEN_ERROR(2, "TOKEN错误"),
		MAINTENANCE(3, "服务器维护中"),
		BAN(4, "被封号, 禁止登陆了"),
		FAST_REQUEST(5, "请求频繁"),
		FAIL(6, "失败"),

		PARAMS_ERROR(10, "参数错误"),

		HANdLER_NOT_FOUND(404, "没有Cmdid对应的RequestHandler"),
		EXCEPTION(500, "服务器出现问题了"),
		;
		private final int status;
		private final String desc;
		BasicGameStatus(int status, String desc) {
			this.status = status;
			this.desc = desc;
		}

		@Override
		public int getStatus() {
			return status;
		}

		@Override
		public String getDesc() {
			return desc;
		}
	}
}
