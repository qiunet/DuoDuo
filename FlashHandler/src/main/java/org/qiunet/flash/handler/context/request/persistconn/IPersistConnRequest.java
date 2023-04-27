package org.qiunet.flash.handler.context.request.persistconn;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.request.IRequest;
import org.qiunet.flash.handler.context.sender.ISessionHolder;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.util.ChannelUtil;


/**
 * 长连接请求接口
 * Created by qiunet.
 * 17/12/2
 */
public interface IPersistConnRequest<RequestData> extends IRequest<RequestData>, ISessionHolder {
	/**
	 * netty channel
	 * @return netty channel
	 */
	Channel channel();

	@Override
	default ISession getSession() {
		return ChannelUtil.getSession(channel());
	}
}
