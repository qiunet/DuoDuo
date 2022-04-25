package org.qiunet.flash.handler.netty.server.kcp.shakehands.mapping;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.timer.TimerManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/***
 *
 * @author qiunet
 * 2022/4/27 11:42
 */
public final class KcpPlayerTokenMapping {

	private static final Map<String, Long> mapping2 = Maps.newConcurrentMap();

	/**
	 * 关联一个token 和 playerId
	 *
	 * @param playerId
	 * @return 返回token
	 */
	public static String mapping(long playerId) {
		for (Map.Entry<String, Long> en : mapping2.entrySet()) {
			if (en.getValue() == playerId) {
				return en.getKey();
			}
		}

		String token = System.currentTimeMillis() +"_"+ StringUtil.randomString(24);
		TimerManager.instance.scheduleWithDelay(() -> mapping2.remove(token), 60, TimeUnit.SECONDS);
		mapping2.put(token, playerId);
		return token;
	}

	/**
	 * 根据token 获得PlayerActor
	 * @param token
	 * @return
	 */
	public static PlayerActor getPlayer(String token) {
		Long playerId = mapping2.remove(token);
		if (playerId == null) {
			return null;
		}
		return UserOnlineManager.getPlayerActor(playerId);
	}
}
