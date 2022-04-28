package org.qiunet.flash.handler.netty.server.kcp.shakehands.mapping;

import com.google.common.collect.Maps;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.timer.TimerManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 * @author qiunet
 * 2022/4/27 11:42
 */
public final class KcpPlayerTokenMapping {
	/**
	 * token 对应的 playerID,convId
	 */
	private static final Map<Long, PlayerKcpParamInfo> mapping = Maps.newConcurrentMap();

	/**
	 * 关联一个token 和 playerId
	 *
	 * @param playerId
	 * @return 返回token
	 */
	public static PlayerKcpParamInfo mapping(long playerId) {
		for (Map.Entry<Long, PlayerKcpParamInfo> en : mapping.entrySet()) {
			if (en.getValue().getPlayerId() == playerId) {
				return en.getValue();
			}
		}
		PlayerKcpParamInfo playerKcpParamInfo = new PlayerKcpParamInfo(playerId);
		TimerManager.instance.scheduleWithDelay(() -> mapping.remove(playerId), 60, TimeUnit.SECONDS);
		mapping.put(playerId, playerKcpParamInfo);
		return playerKcpParamInfo;
	}

	/**
	 * 根据token 获得PlayerActor
	 * @param playerId
	 * @return
	 */
	public static PlayerKcpParamInfo getPlayer(long playerId) {
		return mapping.get(playerId);
	}

	public static class PlayerKcpParamInfo {
		private static final AtomicInteger id = new AtomicInteger();
		private final long playerId;
		private final String token;
		// convId 暂时没有用. 用的address 判断客户端.
		// 但是可以用来判断鉴权,之后如果需要. 也可以用convId 来管理session
		private final int convId;

		public PlayerKcpParamInfo(long playerId) {
			this.token = System.currentTimeMillis() +"_"+ StringUtil.randomString(24);
			this.convId = id.incrementAndGet();
			this.playerId = playerId;
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
}
