package org.qiunet.flash.handler.context.status;

/**
 * 游戏的状态接口
 * Created by qiunet.
 * 17/12/9
 */
public interface IGameStatus {
	/**服务器异常*/
	IGameStatus EXCEPTION = SystemGameStatus.EXCEPTION;
	/**成功*/
	IGameStatus SUCCESS = SystemGameStatus.SUCCESS;
	IGameStatus FAIL = SystemGameStatus.FAIL;
	/**请求频繁*/
	IGameStatus FAST_REQUEST = SystemGameStatus.FAST_REQUEST;
	/**Token 错误*/
	IGameStatus TOKEN_ERROR = SystemGameStatus.TOKEN_ERROR;
	/**维护中 */
	IGameStatus MAINTENANCE = SystemGameStatus.MAINTENANCE;
	/**禁止登陆 封禁了*/
	IGameStatus BAN = SystemGameStatus.BAN;
	/**数值参数错误*/
	IGameStatus NUMBER_PARAM_ERROR = SystemGameStatus.NUMBER_PARAM_ERROR;
	/**字符串为空错误*/
	IGameStatus STRING_PARAM_EMPTY_ERROR = SystemGameStatus.STRING_PARAM_EMPTY_ERROR;
	/**字符串长度错误*/
	IGameStatus STRING_PARAM_LENGTH_ERROR = SystemGameStatus.STRING_PARAM_LENGTH_ERROR;
	/**字符串屏蔽词错误*/
	IGameStatus STRING_PARAM_BAD_WORD_ERROR = SystemGameStatus.STRING_PARAM_BAD_WORD_ERROR;
	/***通用cd中*/
	IGameStatus COMMON_PROTOCOL_CD_ING = SystemGameStatus.COMMON_PROTOCOL_CD_ING;
	/**请求cd中*/
	IGameStatus REQUEST_CD_ING = SystemGameStatus.REQUEST_CD_ING;


	/** 参数错误 */
	IGameStatus PARAMS_ERROR = SystemGameStatus.PARAMS_ERROR;
	/**404*/
	IGameStatus HANDLER_NOT_FOUND = SystemGameStatus.HANdLER_NOT_FOUND;
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
	 * 是否成功
	 * @return true 成功
	 */
	default boolean isSuccess() {
		return this == SUCCESS;
	}
	/***
	 * 是否失败
	 * @return true 失败
	 */
	default boolean isFail() {
		return !isSuccess();
	}

	/**
	 * Created by qiunet.
	 * 17/12/9
	 */
	enum SystemGameStatus implements IGameStatus {
		SUCCESS(1, "成功"),
		TOKEN_ERROR(2, "TOKEN错误"),
		MAINTENANCE(3, "服务器维护中"),
		BAN(4, "被封号, 禁止登陆了"),
		FAST_REQUEST(5, "请求频繁"),
		FAIL(6, "失败"),

		PARAMS_ERROR(10, "参数错误"),

		NUMBER_PARAM_ERROR(11, "数值参数错误"),
		STRING_PARAM_EMPTY_ERROR(12, "字符空错误"),
		STRING_PARAM_LENGTH_ERROR(13, "字符长度错误"),
		STRING_PARAM_BAD_WORD_ERROR(14, "字符含有屏蔽词"),

		REQUEST_CD_ING(15, "请求cd中"),
		COMMON_PROTOCOL_CD_ING(16, "通用请求{}cd中"),

		HANdLER_NOT_FOUND(404, "没有Cmdid对应的RequestHandler"),
		EXCEPTION(500, "服务器出现问题了"),
		;
		private final int status;
		private final String desc;
		SystemGameStatus(int status, String desc) {
			this.status = status;
			this.desc = desc;
		}

		public boolean isSuccess() {
			return this == SUCCESS;
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
