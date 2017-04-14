package org.qiunet.handler.mina.server.session;

import org.apache.mina.core.session.IoSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiunet
 *         Created on 17/3/13 10:20.
 */
public class MinaSession {
	/**mina 的原生session*/
	private IoSession ioSession;
	/**最后收发包的时间*/
	private long lastPackageDt;
	/**属性*/
	public Map<String, Object> attributes;

	public MinaSession(IoSession session) {
		this.ioSession = session;
		this.lastPackageDt = System.currentTimeMillis();
		this.attributes = new ConcurrentHashMap<>(4);
	}

	public IoSession getIoSession() {
		return ioSession;
	}

	public void setIoSession(IoSession ioSession) {
		this.ioSession = ioSession;
	}

	public long getLastPackageDt() {
		return lastPackageDt;
	}

	public void setLastPackageDt(long lastPackageDt) {
		this.lastPackageDt = lastPackageDt;
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public void setAttribute(String key , Object obj) {
		this.attributes.put(key, obj) ;
	}
}
