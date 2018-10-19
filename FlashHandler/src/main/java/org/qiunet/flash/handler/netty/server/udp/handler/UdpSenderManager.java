package org.qiunet.flash.handler.netty.server.udp.handler;

import org.qiunet.flash.handler.netty.server.param.UdpBootstrapParams;
import org.qiunet.utils.timer.AsyncTimerTask;
import org.qiunet.utils.timer.TimerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private Logger logger = LoggerFactory.getLogger(getClass());
	private volatile static UdpSenderManager instance;

	protected UdpBootstrapParams params;

	private UdpSenderManager() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		TimerManager.getInstance().scheduleAtFixedRate(new AsyncTimerTask() {
			@Override
			protected void asyncRun() {
				logger.info("Size"+channels.size());
				for (UdpChannel channel : channels.values()) {
					// 处理channel里面package重传. 以及ask索要.
					channel.timeoutHandler();
				}
			}
		}, 1000, 500);
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
		logger.info("Sender ["+sender.toString()+"] was add");
		this.channels.put(sender, channel);
	}

	void removeChannel(InetSocketAddress sender) {
		params.getSessionEvent().sessionUnregistered(getUdpChannel(sender));
		logger.info("Sender ["+sender.toString()+"] was removed");
		this.channels.remove(sender);
	}

	boolean hasSender(InetSocketAddress sender) {
		return this.channels.containsKey(sender);
	}

	UdpChannel getUdpChannel(InetSocketAddress sender) {
		return this.channels.get(sender);
	}
}
