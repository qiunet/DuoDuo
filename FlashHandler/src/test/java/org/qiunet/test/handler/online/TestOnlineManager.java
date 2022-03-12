package org.qiunet.test.handler.online;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/***
 *
 * @author qiunet
 * 2022/3/7 21:28
 */
public class TestOnlineManager {
	private static final EventLoopGroup eventExecutors = new NioEventLoopGroup(1, new DefaultThreadFactory("test-tcp-boss-event-loop-"));

	@BeforeAll
	public static void init() {
		ClassScanner.getInstance(ScannerType.EVENT).scanner();
	}

	@Test
	public void testOnlineCount() {
		this.buildPlayerActor(10);
		Assertions.assertEquals(1, UserOnlineManager.instance.onlineSize());
		this.buildPlayerActor(11);
		Assertions.assertEquals(2, UserOnlineManager.instance.onlineSize());
	}

	@Test
	public void testLogout() {

	}

	/**
	 * 获得player actor
	 * @param playerId 玩家ID
	 * @return
	 */
	protected PlayerActor buildPlayerActor(long playerId) {
		NioSocketChannel channel = new NioSocketChannel() {
			@Override
			public ChannelFuture write(Object msg) {
				return writeAndFlush(msg);
			}

			@Override
			public ChannelFuture writeAndFlush(Object msg) {
				return writeAndFlush(msg, null);
			}

			@Override
			public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
				return null;
			}
		};
		eventExecutors.register(channel);

		PlayerActor playerActor = new PlayerActor(new DSession(channel));
		channel.attr(ServerConstants.MESSAGE_ACTOR_KEY).set(playerActor);
		playerActor.auth(playerId);
		return playerActor;
	}
}
