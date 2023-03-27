package org.qiunet.flash.handler.context.session.kcp;

import org.qiunet.flash.handler.context.session.KcpSession;

/***
 *
 * @author qiunet
 * 2023/3/30 14:38
 */
public interface IKcpSessionManager extends IKcpSender {
	/**
	 * 绑定kcp session
	 * 仅 tcp 和 ws可以.
	 * @param kcpSession
	 */
	void bindKcpSession(KcpSession kcpSession);
}
