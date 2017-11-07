package org.qiunet.flash.handler.netty.server.tcp.session;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by qiunet.
 * 17/10/23
 */
public class SessionManager {
	/***
	 * 所有的session
	 */
	private static final ConcurrentHashMap<String, IPlayerSession> sessions = new ConcurrentHashMap<>();
}
