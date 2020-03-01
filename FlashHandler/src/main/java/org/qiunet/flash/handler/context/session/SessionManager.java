package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.common.listener.SessionCloseEventData;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by qiunet.
 * 17/10/23
 */
public class SessionManager{
	private Logger logger = LoggerType.DUODUO.getLogger();
	private static final AttributeKey<ISession> SESSION_KEY = AttributeKey.newInstance("SESSION_KEY");
	/***
	 * 所有的session
	 */
	private final ConcurrentHashMap<Long, ISession> sessions = new ConcurrentHashMap<>();
	private volatile static SessionManager instance;
	private SessionManager() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	public static SessionManager getInstance() {
		if (instance == null) {
			synchronized (SessionManager.class) {
				if (instance == null)
				{
					new SessionManager();
				}
			}
		}
		return instance;
	}
	/***
	 * 添加一个Session
	 * @param val
	 * @return
	 */
	public <T extends ISession> T addSession(ISession val) {
		val.getChannel().attr(SESSION_KEY).set(val);
		this.sessions.putIfAbsent(val.getUid(), val);

		val.getChannel().closeFuture().addListener(future -> {
			new SessionCloseEventData(future, val).fireEventHandler();
			sessions.remove(val.getUid());
		});
		return (T) sessions.get(val.getUid());
	}
	/***
	 * 得到一个Session
	 * @param uid
	 * @return
	 */
	public <T extends ISession> T getSession(long uid) {
		return (T) sessions.get(uid);
	}
	/***
	 * 得到一个Session
	 * @param channel
	 * @return
	 */
	public <T extends ISession> T getSession(Channel channel) {
		return (T) channel.attr(SESSION_KEY).get();
	}
	/***
	 * 得到当前的人数
	 * @return
	 */
	public int sessionSize(){
		return sessions.size();
	}
}
