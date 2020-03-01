package org.qiunet.flash.handler.common.listener;

import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.utils.listener.IEventData;

import java.util.concurrent.Future;

/***
 *
 *  玩家的session 被关闭事件对象数据
 * @author qiunet
 * 2020-02-06 14:18
 **/
public class SessionCloseEventData implements IEventData {

	private Future future;

	private ISession session;

	public SessionCloseEventData(Future future, ISession session) {
		this.future = future;
		this.session = session;
	}

	public Future getFuture() {
		return future;
	}

	public ISession getSession() {
		return session;
	}
}
