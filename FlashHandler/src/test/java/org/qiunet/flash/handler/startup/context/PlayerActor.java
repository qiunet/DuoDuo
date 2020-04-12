package org.qiunet.flash.handler.startup.context;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.context.response.push.DefaultStringMessage;
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

	public void sendResponse(int protocolId, Object responseData) {
		// 为了兼容测试. 实际过程不需要这么判断.
		if (responseData instanceof GeneratedMessageV3) {
			this.send(new DefaultProtobufMessage(protocolId, ((GeneratedMessageV3) responseData)));
		}else if(responseData instanceof String){
			this.send(new DefaultStringMessage(protocolId, (String) responseData));
		}else {
			logger.error("ResponseData Type Not Support !!! ");
		}
	}
}
