package org.qiunet.handler.mina.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.qiunet.handler.annotation.support.RequestScannerHandler;
import org.qiunet.handler.mina.server.handler.MinaHandler;
import org.qiunet.handler.mina.server.protocols.MessageCodecFactory;
import org.qiunet.utils.classScanner.ScannerAllClassFile;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author qiunet
 *         Created on 17/3/7 18:22.
 */
public class MinaServer {
	private static final Logger logger = Logger.getLogger(MinaServer.class);

	private int port;
	private MinaHandler minaHandler;
	private NioSocketAcceptor acceptor;
	private InetSocketAddress address;
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Mina 的启动
	 */
	public void start(){
		this.minaHandler = MinaHandler.getInstance();
		ScannerAllClassFile scannerAllClassFile = new ScannerAllClassFile();
		scannerAllClassFile.addScannerHandler(new RequestScannerHandler());
		try {
			scannerAllClassFile.scanner();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		IoBuffer.setUseDirectBuffer(false);
		IoBuffer.setAllocator(new SimpleBufferAllocator());
		address = new InetSocketAddress(port);
		acceptor = new NioSocketAcceptor();
		acceptor.setHandler(minaHandler);
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageCodecFactory()));
		acceptor.setReuseAddress(true);
		acceptor.getSessionConfig().setSendBufferSize(10240);
		acceptor.getSessionConfig().setReadBufferSize(1024);
		acceptor.getSessionConfig().setTcpNoDelay(true);
		acceptor.getSessionConfig().setSoLinger(0);

		try {
			acceptor.bind(address);
			logger.error("start mina server success. listened on:"+port);
		} catch (IOException e) {
			logger.error("start mina server error:", e);
		}
		logger.error("==========================MinaServer started!=============================");
	}

	/**
	 * Mina 的暂停
	 */
	public void stop(){
		logger.error("==========================MinaServer stoped!=============================");
		if(acceptor!=null){
			acceptor.unbind(address);
			acceptor.getFilterChain().clear();
			acceptor.dispose(true);
		}
		if (minaHandler != null) minaHandler.stop();
	}
}
