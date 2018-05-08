package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by qiunet.
 * 17/10/23
 */
public class SessionManager implements Runnable {
	private Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	/***
	 * 所有的session
	 */
	private final ConcurrentHashMap<String, ISession> sessions = new ConcurrentHashMap<>();

	private volatile static SessionManager instance;

	private int maxSessionValidTime = 5 * 60;

	private Thread thread;

	private SessionManager() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");

		thread = new Thread(this, "SessionManager");
		thread.setDaemon(true);
		thread.start();
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
	public <T extends ISession> T addSession(ISession val) {
		this.sessions.putIfAbsent(val.getKey(), val);
		return (T) sessions.get(val.getKey());
	}

	/***
	 * 得到一个Session
	 * @param key
	 * @return
	 */
	public <T extends ISession> T getSession(String key) {
		return (T) sessions.get(key);
	}
	/***
	 * 得到一个Session
	 * @param channel
	 * @return
	 */
	public <T extends ISession> T getSession(Channel channel) {return getSession(channel.id().asLongText()); }
	/**
	 * 移除 session
	 * @param keys
	 */
	public void removeSession(String... keys) {
		for (String key : keys) {
			this.sessions.remove(key);
		}
	}

	/**
	 * 移除 session
	 * @param channel
	 */
	public void removeSession(Channel channel) {
		this.sessions.remove(channel.id().asLongText());
	}

	/***
	 * 得到当前的人数
	 * @return
	 */
	public int sessionSize(){
		return sessions.size();
	}
	@Override
	public void run() {
		while (true) {
			long now = System.currentTimeMillis();

			Iterator<Map.Entry<String, ISession>> it = sessions.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, ISession> en = it.next();
				if (now - en.getValue().lastPackageTimeStamp() > maxSessionValidTime*1000 ){
					it.remove();
				}
			}

			logger.error("[SessionManager] Curr Session Manager Size ["+sessionSize()+"]");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
