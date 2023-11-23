package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.string.StringUtil;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.StringJoiner;

/***
 * 包含真实Channel的session
 *
 * @author qiunet
 * 2023/3/24 20:37
 */
class BaseChannelSession extends BaseSession {
	/**
	 * channel
	 */
	protected Channel channel;

	protected void setChannel(Channel channel) {
		if (channel != null) {
			// channel 跟 session 生命周期不一致的情况.不能调用该set.  会导致内存泄露
			channel.closeFuture().addListener(f -> this.close(CloseCause.CHANNEL_CLOSE));
		}
		this.channel = channel;
	}

	@Override
	public String aliasId() {
		return channel.id().asShortText();
	}

	@Override
	public void flush() {
		channel.flush();
	}
	/**
	 * 关闭channel
	 * @param cause
	 */
	@Override
	protected void closeChannel(CloseCause cause) {
		if (channel == null) {
			return;
		}
		if ((channel.isActive() || channel.isOpen())) {
			logger.info("Session [{}] closed", this);
			this.flush();
		}
		channel.close();
	}
	/**
	 * session是否是活跃的.
	 * @return
	 */
	@Override
	public boolean isActive() {
		return channel != null && channel.isActive();
	}

	@Override
	public String getIp() {
		String ip = getIp0();
		if (! StringUtil.isEmpty(ip) && ip.contains(",")) {
			ip = ip.substring(0, ip.indexOf(","));
		}
		return ip;
	}

	private String getIp0() {
		HttpHeaders headers = getAttachObj(ServerConstants.HTTP_WS_HEADER_KEY);
		if (headers != null) {
			String ip;
			if (!StringUtil.isEmpty(ip = headers.get("x-forwarded-for")) && !"unknown".equalsIgnoreCase(ip)) {
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
		}

		SocketAddress socketAddress = channel.remoteAddress();
		if (socketAddress == null) {
			return "unknown-address";
		}

		return ((InetSocketAddress) socketAddress).getAddress().getHostAddress();
	}

	@Override
	public <T> Attribute<T> attr(AttributeKey<T> key) {
		return channel.attr(key);
	}

	@Override
	public <T> boolean hasAttr(AttributeKey<T> key) {
		return channel.hasAttr(key);
	}

	@Override
	public ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush) {
		return this.doSendMessage(channel, message, flush);
	}

	public Channel channel() {
		return channel;
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(",", "[", "]");
		if (channel != null) {
			boolean isServer = channel.hasAttr(ServerConstants.BOOTSTRAP_CONFIG_KEY);
			sj.add(isServer ? "Server": "Client");
			sj.add("Type = "+getAttachObj(ServerConstants.HANDLER_TYPE_KEY));
			IMessageActor messageActor = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
			if (messageActor != null) {
				sj.add(messageActor.getIdentity());
			}
			sj.add("ID = " + this.aliasId());
			if (isServer) {
				sj.add("Ip = " + getIp());
			}
		}
		return sj.toString();
	}
}
