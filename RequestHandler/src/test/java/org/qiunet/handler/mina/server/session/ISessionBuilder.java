package org.qiunet.handler.mina.server.session;

import org.apache.mina.core.session.IoSession;

/**
 * @author qiunet
 *         Created on 17/3/13 10:19.
 */
public interface ISessionBuilder {
	/**
	 * session 的
	 * @param session
	 * @return
	 */
	public MinaSession build(IoSession session);
}
