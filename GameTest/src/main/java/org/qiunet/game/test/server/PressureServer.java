package org.qiunet.game.test.server;

import io.netty.util.CharsetUtil;
import org.qiunet.game.test.robot.creator.IRobotAccountFactory;
import org.qiunet.game.test.robot.creator.PressureConfig;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.net.NetUtil;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;
import org.qiunet.utils.scanner.anno.AutoWired;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/***
 * 创建机器人的服务
 *
 * @author qiunet
 * 2022/8/25 11:28
 */
public final class PressureServer {
	private static final Logger logger = LoggerType.DUODUO_GAME_TEST.getLogger();
	private static final PressureServer instance = new PressureServer();

	@AutoWired
	private static IRobotAccountFactory param;
	/**
	 * 添加机器人的cmd
	 */
	public static final String ADD_ROBOT = "AddRobot";
	/**
	 * 停止机器人服务
	 */
	public static final String STOP = "Stop";
	/**
	 * 阻塞的线程
	 */
	private static Thread parkThread;

	private static final ScannerType [] DEFAULT_SCANNER_TYPE = new ScannerType[] {
			ScannerType.ROBOT_BEHAVIOR_BUILDER,
			ScannerType.GAME_TEST_RESPONSE,
			ScannerType.CHANNEL_DATA,
			ScannerType.FILE_CONFIG,
			ScannerType.AUTO_WIRE
	};

	private PressureServer() {}

	/** 给服务器的钩子发送消息, 需要另起Main线程.
	 * 默认给本地的端口发送
	 * @param hookPort 钩子端口
	 * @param msg 发送消息内容
	 */
	public static void sendHookMsg(int hookPort, String msg) {
		if (hookPort <= 0) {
			logger.error("PressureServer sendHookMsg but hookPort is less than 0!");
			System.exit(1);
		}
		logger.error("PressureServer sendHookMsg [{}]!", msg);
		try {
			NetUtil.udpSendData("localhost", hookPort, msg.getBytes(CharsetUtil.UTF_8));
		} catch (IOException e) {
			logger.error("PressureServer sendHookMsg: ", e);
			System.exit(1);
		}
	}

	public static PressureServer scanner(String packages) {
		return scanner(packages, DEFAULT_SCANNER_TYPE);
	}

	public static PressureServer scanner(String packages, ScannerType ... scannerTypes) {
		ClassScanner.getInstance(scannerTypes).scanner(packages);
		return instance;
	}

	/**
	 * 启动服务
	 * 启动后会阻塞主线程.
	 */
	public void startup() {
		// 初始的数量
		RobotManager.instance.create(PressureConfig.getCount());
		Thread thread = new Thread(new HookListener(), "PressureHook");
		parkThread= Thread.currentThread();
		thread.setDaemon(true);
		thread.start();
		LockSupport.park();
	}
	/**
	 * 监听
	 */
	private static class HookListener implements Runnable {
		private final ByteBuffer buffer = ByteBuffer.allocate(256);
		private final int hookPort = PressureConfig.getHookPort();

		private final AtomicBoolean shutdown = new AtomicBoolean();
		@Override
		public void run() {
			logger.error("[HookListener]服务端 Hook Listener on udp port [{}]", hookPort);
			try(DatagramChannel channel = DatagramChannel.open()) {
				try {
					channel.bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), this.hookPort));
				}catch (Exception e) {
					logger.error("Bind error", e);
					System.exit(1);
				}

				while (! shutdown.get()) {
					buffer.clear();
					try {
						channel.receive(buffer);
					} catch (IOException e) {
						logger.error("handlerMsg", e);
					}
					buffer.flip();
					this.handlerMsg(CharsetUtil.UTF_8.decode(buffer).toString());
				}
			} catch (IOException e) {
				logger.error("[HookListener]", e);
			}
		}

		/***
		 * 处理现有的消息. 可以用户自定义
		 * @param msg channel
		 */
		private void handlerMsg(String msg) {
			msg = StringUtil.powerfulTrim(msg);
			logger.error("[HookListener]服务端 Received Msg: [{}]", msg);
			if (msg.equals(STOP)) {
				if (shutdown.compareAndSet(false, true)) {
					RobotManager.instance.stop();
					LockSupport.unpark(parkThread);
				}
			} else if (msg.startsWith(ADD_ROBOT)) {
				String[] strings = StringUtil.split(msg, " ");
				if (strings.length != 2 || !StringUtil.isNum(strings[1])) {
					logger.error("Add Robot error! param error! {}", msg);
					return;
				}
				logger.error("=====Add Robot Start=====");
				RobotManager.instance.create(Integer.parseInt(strings[1]));
				logger.error("======Add Robot End======");
			}
		}
	}
}
