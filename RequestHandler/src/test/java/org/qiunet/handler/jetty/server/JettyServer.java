package org.qiunet.handler.jetty.server;

import org.eclipse.jetty.server.Server;
import org.qiunet.handler.annotation.support.RequestScannerHandler;
import org.qiunet.handler.jetty.server.Haneler.JettyHandler;
import org.qiunet.utils.classScanner.ScannerAllClassFile;

import java.net.InetSocketAddress;

/**
 * @author qiunet
 *         Created on 17/3/17 12:03.
 */
public class JettyServer {
	private Server server;
	private int port;

	public void setPort(int port) {
		this.port = port;
	}

	public void start(){
		if (port == 0) port = 8080;

		ScannerAllClassFile scannerAllClassFile = new ScannerAllClassFile();
		scannerAllClassFile.addScannerHandler(new RequestScannerHandler());
		try {
			scannerAllClassFile.scanner();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		server = new Server(new InetSocketAddress("localhost" , port));
		server.setHandler(JettyHandler.getInstance());
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop(){
		try {
			this.server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		JettyServer jettyServer = new JettyServer();
		jettyServer.setPort(8080);
		jettyServer.start();
	}
}
