package org.qiunet.flash.handler.netty.server.constants;

import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.netty.server.param.adapter.IProtocolHeaderAdapter;

/**
 * Created by qiunet.
 * 2019-03-23 21:33
 */
public class ServerConstants {
	/**
	 * netty 保存的handShaker
	 */
	public static final AttributeKey<WebSocketServerHandshaker> HANDSHAKER_ATTR_KEY =
		AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKER");
	/***
	 * channel 存储他是使用哪种方式连接的.
	 */
	public static final AttributeKey<HandlerType> HANDLER_TYPE_KEY = AttributeKey.newInstance("HANDLER_TYPE_KEY");
	/***
	 * 玩家的playerActor 存储在channel的key
	 */
	public static final AttributeKey<IPlayerActor> PLAYER_ACTOR_KEY = AttributeKey.newInstance("PLAYER_ACTOR_KEY");
	/***
	 * 使用的header adapter
	 */
	public static final AttributeKey<IProtocolHeaderAdapter> PROTOCOL_HEADER_ADAPTER = AttributeKey.newInstance("PROTOCOL_HEADER_ADAPTER");
}
