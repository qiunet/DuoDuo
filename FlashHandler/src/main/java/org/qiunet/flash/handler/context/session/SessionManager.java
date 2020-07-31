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
public enum  SessionManager {
	instance
	;
	private Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private static final AttributeKey<ISession> SESSION_KEY = AttributeKey.newInstance("SESSION_KEY");


	public static SessionManager getInstance() {
		return instance;
	}
	/***
	 * 添加一个Session
	 * @param val
	 * @return
	 */
	public boolean addSession(ISession val) {
		Preconditions.checkNotNull(val);
		Attribute<ISession> attr = val.getChannel().attr(SESSION_KEY);
		boolean result = attr.compareAndSet(null, val);
		if (! result) {
			logger.error("Session [{}] Duplicate", val);
			val.close(CloseCause.LOGIN_REPEATED);
		}
		val.getChannel().closeFuture().addListener(future -> logger.warn("Session ["+val+"] closed"));
		return result;
	}
	/***
	 * 得到一个Session
	 * @param channel
	 * @return
	 */
	public <T extends ISession> T getSession(Channel channel) {
		return (T) channel.attr(SESSION_KEY).get();
	}

}
