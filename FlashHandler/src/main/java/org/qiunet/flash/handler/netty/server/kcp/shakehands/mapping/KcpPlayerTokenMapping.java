package org.qiunet.flash.handler.netty.server.kcp.shakehands.mapping;

import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.utils.string.StringUtil;

import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 * @author qiunet
 * 2022/4/27 11:42
 */
public final class KcpPlayerTokenMapping {
	private static final AttributeKey<KcpPlayerTokenMapping> KCP_TOKEN_KEY = AttributeKey.newInstance("kcp_token_key");
	private static final AtomicInteger id = new AtomicInteger();
	private final long playerId;
	private final String token;
	// convId 暂时没有用. 用的address 判断客户端.
	// 但是可以用来判断鉴权,之后如果需要. 也可以用convId 来管理session
	private final int convId;

	public KcpPlayerTokenMapping(PlayerActor playerActor) {
		this.token = System.currentTimeMillis() +"_"+ StringUtil.randomString(24);
		this.playerId = playerActor.getId();
		this.convId = id.incrementAndGet();

		playerActor.getSession().attachObj(KCP_TOKEN_KEY, this);
	}

	public static KcpPlayerTokenMapping get(PlayerActor playerActor) {
		return playerActor.getSession().getAttachObj(KCP_TOKEN_KEY);
	}

	public static KcpPlayerTokenMapping getPlayer(long playerId) {
		PlayerActor playerActor = UserOnlineManager.instance.getPlayerActor(playerId);
		if (playerActor == null) {
			return null;
		}
		return get(playerActor);
	}

	public long getPlayerId() {
		return playerId;
	}

	public String getToken() {
		return token;
	}

	public int getConvId() {
		return convId;
	}

}
