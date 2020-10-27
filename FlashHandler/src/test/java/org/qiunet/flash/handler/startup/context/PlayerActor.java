package org.qiunet.flash.handler.startup.context;

import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.session.DSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 *
 * @author qiunet
 * 2020-04-12 16:28
 **/
public class PlayerActor extends AbstractMessageActor<PlayerActor> {
	private Logger logger = LoggerFactory.getLogger("PlayerActor");
	private long playerId;

	public PlayerActor(DSession session) {
		super(session);
		// 测试不需要管鉴权的问题.
		this.auth(10000);
	}
	/**
	 * 如果已经鉴权. 调用该方法. playerId > 0
	 * @param playerId
	 */
	public void auth(long playerId) {
		this.playerId = playerId;
	}

	@Override
	public long getId() {
		return playerId;
	}

	public String getOpenId() {
		return null;
	}

	public void sendResponse(IpbResponseData responseData) {
		this.send(responseData.buildResponseMessage());
	}
}
