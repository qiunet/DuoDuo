package org.qiunet.handler.mina.server.session;

import org.apache.mina.core.session.IoSession;

/**
 * @author qiunet
 *         Created on 17/3/13 10:48.
 */
public class DefaultSessionBuilder implements ISessionBuilder {
	@Override
	public MinaSession build(IoSession session) {
		return new GameSession(session);
	}
}
