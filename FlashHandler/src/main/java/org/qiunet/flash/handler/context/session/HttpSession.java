package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;

/**
 * @author qiunet
 * 2023/11/8 14:48
 */
public class HttpSession extends BaseChannelSession {

	public HttpSession(Channel channel) {
		setChannel(channel);
	}

	/**
	 * 获取HttpRequest
	 * @return 实例
	 */
	public HttpRequest getHttpRequest() {
		return getAttachObj(ServerConstants.HTTP_REQUEST_KEY);
	}
}
