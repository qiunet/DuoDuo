package org.qiunet.flash.handler.netty.server.constants;

import com.google.common.collect.Lists;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector;
import org.qiunet.data.conf.ServerConfig;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.common.player.protocol.CommonProtocolCD;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.response.push.DefaultBytesMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.event.ServerStartupCompleteEvent;
import org.qiunet.flash.handler.util.NettyUtil;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerStoppedEvent;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.secret.StrCodecUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by qiunet.
 * 2019-03-23 21:33
 */
public final class ServerConstants {
	private ServerConstants(){}
	public static final int MAX_SOCKET_MESSAGE_LENGTH = 1024 * 1024;

	public static final EventLoopGroup WORKER = NettyUtil.newEventLoopGroup(0, "netty-tcp-server-worker-event-loop-");
	/**
	 * 感兴趣的消息列表
	 */
	public static final ArgumentKey<List<DefaultBytesMessage>> INTEREST_MESSAGE_LIST = new ArgumentKey<>(Lists::newLinkedList);
	/**
	 * 通用协议cd 检查
	 */
	public static final AttributeKey<CommonProtocolCD> COMMON_PROTOCOL_CD_CHECK_KEY = AttributeKey.newInstance("COMMON_PROTOCOL_CD_CHECK_KEY");

	/**
	 * netty 保存的handShaker
	 */
	public static final AttributeKey<WebSocketServerHandshaker> HANDLER_SHAKER_ATTR_KEY = AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKER");
	/***
	 * channel 存储他是使用哪种方式连接的.
	 */
	public static final AttributeKey<ServerConnType> HANDLER_TYPE_KEY = AttributeKey.newInstance("HANDLER_TYPE_KEY");
	/**
	 * Session key
	 */
	public static final AttributeKey<ISession> SESSION_KEY = AttributeKey.newInstance("SESSION_CHANNEL_KEY");
	/***
	 * 启动参数
	 */
	public static final AttributeKey<ServerBootStrapConfig> BOOTSTRAP_CONFIG_KEY = AttributeKey.newInstance("BOOTSTRAP_CONFIG");
	/***
	 * HTTP request
	 */
	public static final AttributeKey<HttpRequest> HTTP_REQUEST_KEY = AttributeKey.newInstance("HTTP_REQUEST");
	/***
	 * messageActor 存储在channel的key
	 */
	public static final AttributeKey<IMessageActor> MESSAGE_ACTOR_KEY = AttributeKey.newInstance("MESSAGE_ACTOR_KEY");
	/***
	 * http ws 的header
	 */
	public static final AttributeKey<HttpHeaders> HTTP_WS_HEADER_KEY = AttributeKey.newInstance("HTTP_WS_HEADER_KEY");
	/**
	 * 是否是connect
	 */
	public static final AttributeKey<Boolean> ALREADY_CONNECT_KEY = AttributeKey.newInstance("ALREADY_CONNECT_KEY");
	/***
	 * 使用的header adapter
	 */
	public static final AttributeKey<IProtocolHeader> PROTOCOL_HEADER = AttributeKey.newInstance("PROTOCOL_HEADER");
	/**
	 * 启动时间
	 */
	public static AtomicLong startDt = new AtomicLong();

	/** 祈福标 */
	private static String ICON =
		"0a3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d" +
		"3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d0a7c7c20202020202020202020202020" +
		"20202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020" +
		"202020202020202020202020202020202020202020202020202020202020202020202020202020207c7c0a7c7c20202020444444444444444444444444444444442020202020202020" +
		"20202020202020202020202020202020202020202020202020202020202020202020202044444444444444444444444444444444442020202020202020202020202020202020202020" +
		"202020202020202020202020202020202020202020202020207c7c0a7c7c20202020443a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a442020202020202020202020202020202020202020" +
		"202020202020202020202020202020202020202020443a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a44202020202020202020202020202020202020202020202020202020202020202020" +
		"202020202020202020207c7c0a7c7c202020204444443a3a3a444444444444444444443a3a3a4420202020202020202020202020202020202020202020202020202020202020202020" +
		"2020202020204444443a3a3a3a4444444444444444443a3a3a442020202020202020202020202020202020202020202020202020202020202020202020202020202020207c7c0a7c7c" +
		"202020202020443a3a3a44202020202020202020443a3a3a44202075757575752020202020207575757575202020206f6f6f6f6f6f6f6f6f6f6f6f20202020202020443a3a3a442020" +
		"20202020202020443a3a3a3a442075757575752020202020207575757575202020206f6f6f6f6f6f6f6f6f6f6f6f6f2020202020207c7c0a7c7c202020202020443a3a3a4420202020" +
		"202020202020443a3a3a4420753a3a3a75202020202020753a3a3a752020206f3a3a3a3a3a3a3a3a3a3a3a3a6f6f2020202020443a3a3a4420202020202020202020443a3a3a442075" +
		"3a3a3a75202020202020753a3a3a752020206f3a3a3a3a3a3a3a3a3a3a3a3a3a6f20202020207c7c0a7c7c202020202020443a3a3a4420202020202020202020443a3a3a4420753a3a" +
		"3a75202020202020753a3a3a7520206f3a3a3a3a6f6f6f6f6f6f6f3a3a3a3a6f20202020443a3a3a4420202020202020202020443a3a3a4420753a3a3a75202020202020753a3a3a75" +
		"20206f3a3a3a3a6f6f6f6f6f6f6f3a3a3a3a6f202020207c7c0a7c7c202020202020443a3a3a4420202020202020202020443a3a3a4420753a3a3a75202020202020753a3a3a752020" +
		"6f3a3a3a6f202020202020206f3a3a3a6f20202020443a3a3a4420202020202020202020443a3a3a4420753a3a3a75202020202020753a3a3a7520206f3a3a3a6f202020202020206f" +
		"3a3a3a6f202020207c7c0a7c7c202020202020443a3a3a4420202020202020202020443a3a3a4420753a3a3a75202020202020753a3a3a7520206f3a3a3a6f202020202020206f3a3a" +
		"3a6f20202020443a3a3a4420202020202020202020443a3a3a4420753a3a3a75202020202020753a3a3a7520206f3a3a3a6f202020202020206f3a3a3a6f202020207c7c0a7c7c2020" +
		"20202020443a3a3a44202020202020202020443a3a3a442020753a3a3a3a7575757575753a3a3a3a7520206f3a3a3a6f202020202020206f3a3a3a6f20202020443a3a3a4420202020" +
		"2020202020443a3a3a442020753a3a3a3a7575757575753a3a3a3a7520206f3a3a3a6f202020202020206f3a3a3a6f202020207c7c0a7c7c202020204444443a3a3a44444444444444" +
		"4444443a3a3a44202020753a3a3a3a3a3a3a3a3a3a3a3a3a3a3a75206f3a3a3a3a6f6f6f6f6f6f6f3a3a3a3a6f20204444443a3a4444444444444444444444443a3a44202020753a3a" +
		"3a3a3a3a3a3a3a3a3a3a3a3a3a75206f3a3a3a3a6f6f6f6f6f6f6f3a3a3a3a6f202020207c7c0a7c7c20202020443a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a4420202020203a3a3a3a" +
		"3a3a3a3a3a3a3a3a3a3a3a75206f3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a6f2020443a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a44202020203a3a3a3a3a3a3a3a3a3a3a3a3a3a3a7520" +
		"6f3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a6f202020207c7c0a7c7c2020202044444444444444444444444444444444202020202020202020207575757575757575752020757575202020" +
		"6f6f6f6f6f6f6f6f6f6f6f6f2020202020444444444444444444444444444444444420202020202020202075757575757575757575207575752020206f6f6f6f6f6f6f6f6f6f6f6f6f" +
		"2020202020207c7c0a7c7c2020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020" +
		"202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020207c7c0a3d3d3d3d3d3d" +
		"3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d" +
		"3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d";


	@EventListener(EventHandlerWeightType.LOWEST)
	private void onStartupComplete(ServerStartupCompleteEvent event) {
		if (! ServerConfig.isOfficial()) {
			ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
		}

		LoggerType.DUODUO_FLASH_HANDLER.error( StrCodecUtil.decrypt(ICON));
		ICON = null;

		if (startDt.get() > 0) {
			LoggerType.DUODUO_FLASH_HANDLER.error("Server startup successful in {} ms", (System.currentTimeMillis() - startDt.get()));
		}
		startDt = null;
	}

	@EventListener
	private void onServerShutdown(ServerStoppedEvent event) {
		WORKER.shutdownGracefully();
	}
}
