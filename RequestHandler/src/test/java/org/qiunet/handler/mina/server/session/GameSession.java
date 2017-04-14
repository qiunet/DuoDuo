package org.qiunet.handler.mina.server.session;

import com.sun.deploy.services.PlatformType;
import org.apache.mina.core.session.IoSession;

/**
 * @author qiunet
 *         Created on 17/3/13 10:48.
 */
public class GameSession extends MinaSession {
	private int uid;
	private PlatformType platform;
	public GameSession(IoSession session) {
		super(session);
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public PlatformType getPlatform() {
		return platform;
	}

	public void setPlatform(PlatformType platform) {
		this.platform = platform;
	}
}
