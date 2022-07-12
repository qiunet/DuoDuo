package org.qiunet.flash.handler.netty.server.constants;

import com.google.common.collect.Lists;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.header.IProtocolHeaderType;
import org.qiunet.flash.handler.context.response.push.DefaultBytesMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.event.ServerStartupCompleteEvent;
import org.qiunet.flash.handler.netty.server.param.AbstractBootstrapParam;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.secret.StrCodecUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by qiunet.
 * 2019-03-23 21:33
 */
public final class ServerConstants {
	public static final EventLoopGroup BOSS = new NioEventLoopGroup(1, new DefaultThreadFactory("netty-server-boss-event-loop-"));
	public static final EventLoopGroup WORKER = new NioEventLoopGroup(new DefaultThreadFactory("netty-server-worker-event-loop-"));

	private ServerConstants(){}

	/**
	 * 感兴趣的消息列表
	 */
	public static final ArgumentKey<List<DefaultBytesMessage>> INTEREST_MESSAGE_LIST = new ArgumentKey<>(Lists::newLinkedList);
	/**
	 * netty 保存的handShaker
	 */
	public static final AttributeKey<WebSocketServerHandshaker> HANDSHAKER_ATTR_KEY =
		AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKER");
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
	public static final AttributeKey<AbstractBootstrapParam> HANDLER_PARAM_KEY = AttributeKey.newInstance("HANDLER_PARAM_KEY");
	/***
	 * messageActor 存储在channel的key
	 */
	public static final AttributeKey<IMessageActor> MESSAGE_ACTOR_KEY = AttributeKey.newInstance("MESSAGE_ACTOR_KEY");
	/***
	 * http ws 的header
	 */
	public static final AttributeKey<HttpHeaders> HTTP_WS_HEADER_KEY = AttributeKey.newInstance("HTTP_WS_HEADER_KEY");
	/***
	 * 使用的header adapter
	 */
	public static final AttributeKey<IProtocolHeaderType> PROTOCOL_HEADER_ADAPTER = AttributeKey.newInstance("PROTOCOL_HEADER_ADAPTER");
	/**
	 * 启动时间
	 */
	public static final AtomicLong startDt = new AtomicLong();

	/** 祈福标 */
	private static final String ICON = "0a2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2ee998bfe5bca5e99980e4bd9b2e2e" +
		"2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e0a20202020202020202020202020202020202020202020205f6f6f306f6f5f202" +
		"020202020202020202020202020202020202020200a202020202020202020202020202020202020202020206f383838383838386f" +
		"2020202020202020202020202020202020202020200a20202020202020202020202020202020202020202020383822202e2022383" +
		"82020202020202020202020202020202020202020200a20202020202020202020202020202020202020202020287c202d5f2d207c" +
		"292020202020202020202020202020202020202020200a20202020202020202020202020202020202020202020305c20203d20202" +
		"f302020202020202020202020202020202020202020200a202020202020202020202020202020202020205f5f5f2fe280982d2d2d" +
		"e280995c5f5f5f202020202020202020202020202020202020200a2020202020202020202020202020202020202e27205c7c20202" +
		"0202020207c2f20272e20202020202020202020202020202020200a20202020202020202020202020202020202f205c5c7c7c7c20" +
		"203a20207c7c7c2f2f205c202020202020202020202020202020200a202020202020202020202020202020202f205f7c7c7c7c7c2" +
		"02de58d8d2d7c7c7c7c7c5f205c2020202020202020202020202020200a2020202020202020202020202020207c2020207c205c5c" +
		"5c20202d20202f2f2f207c2020207c20202020202020202020202020200a2020202020202020202020202020207c205c5f7c20202" +
		"7275c2d2d2d2f272720207c5f2f207c20202020202020202020202020200a2020202020202020202020202020205c20202e2d5c5f" +
		"5f2020272d2720205f5f5f2f2d2e202f20202020202020202020202020200a202020202020202020202020205f5f5f272e202e272" +
		"0202f2d2d2e2d2d5c2020272e202e275f5f5f2020202020202020202020200a2020202020202020202e222220e280983c2020e280" +
		"982e5f5f5f5c5f3c7c3e5f2f5f5f5f2ee280993ee280992022222e202020202020202020200a202020202020207c207c203a2020e" +
		"280982d205ce280982e3be280985c205f202fe280993b2ee280992f202d20e28099203a207c207c20202020202020200a20202020" +
		"20202020205c20205c20e280985f2e2020205c5f205f5f5c202f5f5f205f2f2020202e2de28099202f20202f20202020202020200" +
		"a202020203d3d3d3d3de280982d2e5f5f5f5fe280982e5f5f5f205c5f5f5f5f5f2f5f5f5f2e2de280995f5f5f2e2de280993d3d3d" +
		"3d3d20202020200a2020202020202020202020202020202020202020202020e280983d2d2d2d3de28099202020202020202020202" +
		"020202020202020202020200a20202020202020202020202020202020202020202020202020202020202020202020202020202020" +
		"2020202020202020202020200a2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2ee4bd9be7a596e4bf9de4bd91202ce6b0b8e697a" +
		"04255472e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e";


	@EventListener(EventHandlerWeightType.LESS)
	private void onStartupComplete(ServerStartupCompleteEvent event) {
		if (startDt.get() > 0) {
			LoggerType.DUODUO_FLASH_HANDLER.info("Server startup successful in {} ms", (System.currentTimeMillis() - startDt.get()));
		}
	}

	@EventListener
	private void onServerStart(ServerStartupCompleteEvent eventData){
		if (LoggerType.DUODUO_FLASH_HANDLER.getLogger().isInfoEnabled()) {
			LoggerType.DUODUO_FLASH_HANDLER.info(StrCodecUtil.decrypt(ICON));
		}
	}
}
