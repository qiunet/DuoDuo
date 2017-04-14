package org.qiunet.handler.mina.server.evnet;

import org.qiunet.handler.mina.server.session.MinaSession;

/**
 * @author qiunet
 *         Created on 17/3/13 11:20.
 */
public interface ISessionEvent {
	/***
	 * 
	 * @param session
	 */
	public void sessionCreated(MinaSession session);
	/**
	 *  
	 * @param session
	 */
	public void sessionIdle(MinaSession session);
	/**
	 * session
	 * @param session
	 */
	public void sessionOpened(MinaSession session);
	/**
	 * 
	 * @param session
	 */
	public  void sessionClosed(MinaSession session);
	/**
	 * 
	 * @param session
	 * @param cause
	 * @throws Exception
	 */
	public void exceptionCaught(MinaSession session, Throwable cause) throws Exception;
}
