package org.qiunet.flash.handler.context.request;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.message.UriHttpMessageContent;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiunet.
 * 17/11/20
 */
public abstract class BaseRequestContext<RequestData> implements IRequestContext<RequestData> {
	protected MessageContent messageContent;
	protected ChannelHandlerContext ctx;
	private IHandler<RequestData> handler;
	private Map<String , Object> attributes;
	private static final Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);

	protected BaseRequestContext(MessageContent content, ChannelHandlerContext ctx) {
		this.ctx = ctx;
		this.messageContent = content;
		if (content.getProtocolId() > 0) {
			this.handler = RequestHandlerMapping.getInstance().getGameHandler(messageContent.getProtocolId());
		}else {

			this.handler = RequestHandlerMapping.getInstance().getUriPathRequestHandler(((UriHttpMessageContent) messageContent).getUriPath());
		}
	}
	@Override
	public IHandler<RequestData> getHandler() {
		return handler;
	}

	@Override
	public Object getAttribute(String key) {
		return attributes == null ? null : attributes.get(key);
	}
	@Override
	public void setAttribute(String key, Object val) {
		if (attributes == null) attributes = new HashMap<>();
		attributes.put(key, val);
	}

	/**
	 * 得到真实ip. http类型的父类
	 * 2019/08/05 zhengj修改
	 * @param headers
	 * @return
	 */
	protected String getRealIp(HttpHeaders headers) {
		String ip;
		if (!StringUtil.isEmpty(ip = headers.get("x-forwarded-for")) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}

		if (!StringUtil.isEmpty(ip = headers.get("X-Forwarded-For")) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}

		if (! StringUtil.isEmpty(ip = headers.get("HTTP_X_FORWARDED_FOR")) && ! "unknown".equalsIgnoreCase(ip)) {
			return ip;
		}

		if (!StringUtil.isEmpty(ip = headers.get("x-forwarded-for-pound")) &&! "unknown".equalsIgnoreCase(ip)) {
			return ip;
		}

		if (!StringUtil.isEmpty(ip = headers.get("Proxy-Client-IP") ) &&! "unknown".equalsIgnoreCase(ip)) {
			return ip;
		}

		if (!StringUtil.isEmpty(ip = headers.get("WL-Proxy-Client-IP")) &&! "unknown".equalsIgnoreCase(ip)) {
			return ip;
		}

		return ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
	}
}
