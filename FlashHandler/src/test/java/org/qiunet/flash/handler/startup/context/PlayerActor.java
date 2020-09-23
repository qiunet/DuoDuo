package org.qiunet.flash.handler.startup.context;

import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 *
 * @author qiunet
 * 2020-04-12 16:28
 **/
public class PlayerActor extends AbstractPlayerActor<GameSession, PlayerActor> {
	private Logger logger = LoggerFactory.getLogger("PlayerActor");
	private long playerId;

	public PlayerActor(GameSession session) {
		super(session);
		// 测试不需要管鉴权的问题.
		this.auth(10000);
	}

	@Override
	protected String getIdent() {
		return "PlayerActor["+getPlayerId()+"]";
	}

	/**
	 * 如果已经鉴权. 调用该方法. playerId > 0
	 * @param playerId
	 */
	public void auth(long playerId) {
		this.playerId = playerId;
	}

	@Override
	public long getPlayerId() {
		return playerId;
	}

	@Override
	public String getOpenId() {
		return null;
	}

	public void sendResponse(IpbResponseData responseData) {
		this.send(responseData.buildResponseMessage());
	}
}
