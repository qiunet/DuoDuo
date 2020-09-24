package org.qiunet.flash.handler.context.session;

import com.google.common.base.Preconditions;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * Created by qiunet.
 * 17/10/23
 */
public class SessionManager {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private static final AttributeKey<DSession> SESSION_KEY = AttributeKey.newInstance("SESSION_KEY");

	private SessionManager(){}
	/***
	 * 添加一个Session
	 * @param val
	 * @return
	 */
	public static boolean addSession(DSession val) {
		Preconditions.checkNotNull(val);
		Attribute<DSession> attr = val.channel().attr(SESSION_KEY);
		boolean result = attr.compareAndSet(null, val);
		if (! result) {
			logger.error("Session [{}] Duplicate", val);
			val.close(CloseCause.LOGIN_REPEATED);
		}
		return result;
	}
	/***
	 * 得到一个Session
	 * @param channel
	 * @return
	 */
	public static DSession getSession(Channel channel) {
		return channel.attr(SESSION_KEY).get();
	}

}
