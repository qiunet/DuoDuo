package org.qiunet.test.server.type;

import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.test.server.IServer;

import java.net.URI;

/**
 * Created by qiunet.
 * 17/12/9
 */
public enum ServerType implements IServer {
	/**http 逻辑服*/
	HTTP_LOGIC(URI.create("http://localhost:8080/f"), HandlerType.HTTP),
	/**长链接 在线服*/
	LC_ONLINE(URI.create("ws://localhost:8080/ws"), HandlerType.WEB_SOCKET),
	/**长链接 房间服*/
	LC_ROOM(URI.create("lc://localhost:8081"), HandlerType.TCP),
	;
	private URI uri;
	private HandlerType type;

	private ServerType(URI uri, HandlerType type) {
		this.uri = uri;
		this.type = type;
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public HandlerType getType() {
		return type;
	}

	@Override
	public URI uri() {
		return uri;
	}
}