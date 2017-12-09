package org.qiunet.flash.handler.context.status;

/**
 * 游戏的状态接口
 * Created by qiunet.
 * 17/12/9
 */
public interface IGameStatus {
	/**服务器异常*/
	IGameStatus EXCEPTION = BaseGameStatus.EXCEPTION;
	/**成功*/
	IGameStatus SUCCESS = BaseGameStatus.SUCCESS;
	/**请求频繁*/
	IGameStatus FAST_REQUEST = BaseGameStatus.FAST_REQUEST;
	/**Token 错误*/
	IGameStatus TOKEN_ERROR = BaseGameStatus.TOKEN_ERROR;
	/**维护中 */
	IGameStatus MAINTENANCE = BaseGameStatus.MAINTENANCE;
	/**禁止登陆 封禁了*/
	IGameStatus BAN = BaseGameStatus.BAN;

	/** 参数错误 */
	IGameStatus PARAMS_ERROR = BaseGameStatus.PARAMS_ERROR;
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
}
