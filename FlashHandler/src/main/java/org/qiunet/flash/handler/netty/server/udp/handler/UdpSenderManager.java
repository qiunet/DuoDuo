package org.qiunet.flash.handler.netty.server.udp.handler;

import org.qiunet.flash.handler.netty.server.param.UdpBootstrapParams;

import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.util.concurrent.ConcurrentHashMap;

/***
 * sender 到 udpchannel的一个映射关系.
 * @Author qiunet
 * @Date Create in 2018/7/20 18:08
 **/
class UdpSenderManager {
	private ConcurrentHashMap<InetSocketAddress, UdpChannel> channels = new ConcurrentHashMap<>();

	private volatile static UdpSenderManager instance;

	protected UdpBootstrapParams params;

	private UdpSenderManager() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	static UdpSenderManager getInstance() {
		if (instance == null) {
			synchronized (UdpSenderManager.class) {
				if (instance == null)
				{
					new UdpSenderManager();
				}
			}
		}
		return instance;
	}

	void addSender(InetSocketAddress sender, UdpChannel channel) {
		params.getSessionEvent().sessionRegistered(channel);
		this.channels.put(sender, channel);
	}

	void removeChannel(InetSocketAddress sender) {
		params.getSessionEvent().sessionUnregistered(getUdpChannel(sender));
		this.channels.remove(sender);
	}

	boolean hasSender(InetSocketAddress sender) {
		return this.channels.containsKey(sender);
	}

	UdpChannel getUdpChannel(InetSocketAddress sender) {
		return this.channels.get(sender);
	}
}
