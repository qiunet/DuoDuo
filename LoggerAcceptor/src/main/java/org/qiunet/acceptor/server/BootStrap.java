package org.qiunet.acceptor.server;


import org.apache.log4j.Logger;
import org.qiunet.acceptor.log.LogDataRegister;
import org.qiunet.acceptor.log.LoggerUtil;
import org.qiunet.utils.args.ArgsMapping;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/9/24
 */
public class BootStrap implements Runnable {
	private static Logger logger = LoggerUtil.getOutLogger();
	private static Thread udpServer;
	private static Thread tcpServer;
	public static void main(String[] args) {
		BootStrap bootstrap = new BootStrap();
		Thread thread = new Thread(bootstrap, "SupportBootstrap");
		thread.setDaemon(true);
		thread.start();

		ArgsMapping mapping = new ArgsMapping(args);
		udpServer = new Thread(() -> new UDPServer(mapping) , "udpServer");
		udpServer.setDaemon(true);
		udpServer.start();

		tcpServer = new Thread(() -> new TCPServer(mapping) , "tcpServer");
		tcpServer.setDaemon(true);
		tcpServer.start();

		LockSupport.park(Thread.currentThread());
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info("====msgCount ["+LogDataRegister.getInstance().getTotalCount()+"] currSize ["+LogDataRegister.getInstance().size()+"]");
		}
	}
}
