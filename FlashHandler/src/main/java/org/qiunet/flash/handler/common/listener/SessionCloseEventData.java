package org.qiunet.flash.handler.common.listener;

import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.utils.listener.EventListener;
import org.qiunet.utils.listener.IEventData;

/***
 *
 *  玩家的session 被关闭事件对象数据
 * @author qiunet
 * 2020-02-06 14:18
 **/
@EventListener(SessionCloseEventData.SessionCloseListener.class)
public class SessionCloseEventData implements IEventData {
	private ISession session;
	private CloseCause cause;
	public SessionCloseEventData(ISession session, CloseCause cause) {
		this.session = session;
		this.cause = cause;
	}

	public CloseCause getCause() {
		return cause;
	}

	public ISession getSession() {
		return session;
	}

	public interface SessionCloseListener {
		void onCloseSession(SessionCloseEventData data);
	}
}
