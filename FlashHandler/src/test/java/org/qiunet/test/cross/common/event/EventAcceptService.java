package org.qiunet.test.cross.common.event;

import org.qiunet.cross.rpc.RpcManager;
import org.qiunet.test.cross.common.Constants;
import org.qiunet.test.cross.common.rpc.TestRpcHandler;
import org.qiunet.test.cross.common.rpc.TestRpcReq;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 22:26
 */
public enum EventAcceptService {
	instance;

	@EventListener
	public void login(PlayerLoginEvent eventData) {
		LoggerType.DUODUO_CROSS.info("Logic服: PlayerId: {},登录事件触发", eventData.getPlayer().getId());
	}

	@EventListener
	public void crossLogin(CrossPlayerLoginEvent eventData) {
		LoggerType.DUODUO_CROSS.info("Cross服: 第一次EquipIndexRequest: PlayerId: {},跨服登录事件", eventData.getPlayer().getId());

		RpcManager.rpcCall(Constants.LOGIC_SERVER_ID, TestRpcHandler::handler, TestRpcReq.valueOf(eventData.getPlayer().getId()), (rsp, err) -> {
			if (err != null) {
				LoggerType.DUODUO_CROSS.error("出现异常:{}", err);
				return;
			}
			LoggerType.DUODUO_CROSS.info("Cross服: PlayerId: {},跨服异步rpc", rsp.getPlayerId());
		});
	}
}
