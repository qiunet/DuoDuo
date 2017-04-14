package org.qiunet.gametest.protocol.handler.socket;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * @author qiunet
 *         Created on 16/12/15 11:27.
 */
public class MinaHandler extends IoHandlerAdapter {
	private volatile static MinaHandler instance;

	public static MinaHandler getInstance() {
		if (instance == null) {
			synchronized (MinaHandler.class) {
				if (instance == null)
				{
					instance = new MinaHandler();
				}
			}
		}
		return instance;
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
	}
}
