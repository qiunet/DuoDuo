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
import org.qiunet.flash.handler.common.player.observer.IPlayerDestroy;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.util.concurrent.atomic.AtomicBoolean;

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

	/**
	 * 正常登录
	 */
	@Test
	public void testOnlineCount() {
		PlayerActor playerActor1 = this.buildPlayerActor(10);
		Assertions.assertEquals(1, UserOnlineManager.instance.onlineSize());
		PlayerActor playerActor2 = this.buildPlayerActor(11);
		Assertions.assertEquals(2, UserOnlineManager.instance.onlineSize());
		UserOnlineManager.instance.playerQuit(playerActor1);
		UserOnlineManager.instance.playerQuit(playerActor2);
	}

	/**
	 * 正常退出
	 */
	@Test
	public void testLogout() {
		final AtomicBoolean destroyed = new AtomicBoolean(false);
		PlayerActor playerActor = this.buildPlayerActor(10);
		playerActor.attachObserver(IPlayerDestroy.class, (p) -> {
			destroyed.set(true);
		});

		UserOnlineManager.instance.playerQuit(playerActor);
		Assertions.assertEquals(0, UserOnlineManager.instance.onlineSize());
		Assertions.assertTrue(destroyed.get());
	}

	/**
	 * 模拟断网
	 */
	@Test
	public void brokenNet () {
		final AtomicBoolean destroyed = new AtomicBoolean(false);
		PlayerActor playerActor = this.buildPlayerActor(10);
		playerActor.attachObserver(IPlayerDestroy.class, (p) -> {
			// 断网不执行 destroy
			destroyed.set(true);
		});

		playerActor.getSession().close(CloseCause.NET_ERROR);

		Assertions.assertFalse(destroyed.get());
		Assertions.assertEquals(0, UserOnlineManager.instance.onlineSize());
		Assertions.assertNotNull(UserOnlineManager.getWaitReconnectPlayer(10));
	}

	/**
	 * 模拟重连
	 * @throws Exception
	 */
	@Test
	public void reconnect() throws Exception {
		this.brokenNet();
		final AtomicBoolean destroyed = new AtomicBoolean(false);

		// 重连使用老的actor. 这个actor 会回收 destroy
		PlayerActor playerActor = this.buildPlayerActor(10);
		playerActor.attachObserver(IPlayerDestroy.class, (p) -> {
			// 重连的抛弃是直接调用actor的destroy. 不会触发  IPlayerDestroy
			destroyed.set(true);
		});
		UserOnlineManager.instance.reconnect(10, playerActor);

		Assertions.assertFalse(destroyed.get());
		Assertions.assertNull(UserOnlineManager.getWaitReconnectPlayer(10));
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
