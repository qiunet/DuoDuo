package org.qiunet.handler.mina.client.handler;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.qiunet.handler.mina.server.handler.MinaHandler;

/**
 * @author qiunet
 *         Created on 17/3/17 01:11.
 */
public class ClientMinaHandler extends IoHandlerAdapter {
	private static final Logger logger = Logger.getLogger(MinaHandler.class);

	private IoSession ioSession;

	private volatile static ClientMinaHandler instance;

	private ClientMinaHandler() {
		instance = this;
	}

	public static ClientMinaHandler getInstance() {
		if (instance == null) {
			synchronized (ClientMinaHandler.class) {
				if (instance == null)
				{
					new ClientMinaHandler();
				}
			}
		}
		return instance;
	}
	
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		this.ioSession = session;
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		session.closeNow();
	}

	public IoSession getIoSession() {
		return ioSession;
	}
}
