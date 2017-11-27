package org.qiunet.flash.handler.context.session;

import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by qiunet.
 * 17/10/23
 */
public class SessionManager<Key, Val extends ISession<Key>> implements Runnable {
	private QLogger logger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);
	/***
	 * 所有的session
	 */
	private final ConcurrentHashMap<Key, Val> sessions = new ConcurrentHashMap<>();

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

			Iterator<Map.Entry<Key, Val>> it = sessions.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<Key, Val> en = it.next();
				if (now - en.getValue().lastPackageTimeStamp() > maxSessionValidTime*1000 ){
					it.remove();
				}
			}

			logger.error("[SessionManager] Curr Session Manager Size ["+sessionSize()+"]");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
