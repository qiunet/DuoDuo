package org.qiunet.flash.handler.context.session;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by qiunet.
 * 17/10/23
 */
public class SessionManager<Key, Val extends ISession<Key>> {
	/***
	 * 所有的session
	 */
	private final ConcurrentHashMap<Key, Val> sessions = new ConcurrentHashMap<>();

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
	 * 存入key
	 * @param val
	 * @return
	 */
	public Val addSession(Val val) {
		this.sessions.putIfAbsent(val.getKey(), val);
		return sessions.get(val.getKey());
	}

	/***
	 * 得到一个key
	 * @param key
	 * @return
	 */
	public Val getSession(Key key) {
		return sessions.get(key);
	}
	/**
	 * 移除 session
	 * @param keys
	 */
	public void removeSession(Key... keys) {
		for (Key key : keys) {
			this.sessions.remove(key);
		}
	}
}
