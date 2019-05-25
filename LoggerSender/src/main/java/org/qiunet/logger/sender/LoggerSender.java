package org.qiunet.logger.sender;

import org.qiunet.utils.hook.ShutdownHookThread;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/***
 * 日志发送
 */
public class LoggerSender {
	private Logger logger = LoggerType.DUODUO.getLogger();

	private Charset charset = Charset.forName("UTF-8");

	private HandlerMsgQueue handler;

	private InetSocketAddress address;

	private short gameId;

	private String secret;

	private SocketChannel channel;

	public LoggerSender(String remoteIp, int port, int gameId, String secret) {
		try {
			this.address = new InetSocketAddress(InetAddress.getByName(remoteIp), port);
			this.handler = new HandlerMsgQueue();
			this.gameId = (short) gameId;
			this.secret = secret;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		ShutdownHookThread.getInstance().addShutdownHook(() -> stop());
	}

	/***
	 * 得到发送的数量
	 * @return
	 */
	public long getSendCount(){
		return this.handler.atomicLong.get();
	}
	/***
	 * 停止服务
	 */
	public void stop(){
		this.handler.shutdown();
	}

	/***
	 * 发送一般消息. 使用udp
	 */
	public void sendLog(String logName, String msg) {
		byte [] bytes = (logName + "|" + msg).getBytes(charset);
		int msgLength = bytes.length;
		if (msgLength >= 1000) {
			logger.error("udp package length can not more than: "+1000);
		}
		this.handler.add(new UdpMessage(address, gameId, secret, bytes));
	}
	/***
	 * 发送重要消息 使用tcp.
	 */
	public void sendImportantLog(String logName, String msg) {
		if (channel == null || ! (channel.isConnected() && channel.isOpen())) {
			try {
				channel = SocketChannel.open(address);
				channel.configureBlocking(false);
			} catch (IOException e) {
				logger.error("异常", e);
			}
		}

		this.handler.add(new TcpMessage(channel, gameId, secret, (logName + "|" + msg).getBytes(charset)));
	}
}
