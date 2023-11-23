package org.qiunet.flash.handler.context.request.check;

import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.session.ISession;

/***
 *
 *参数检查
 *
 * @author qiunet
 * 2022/1/5 17:02
 */
public interface IRequestCheck {
	/**
	 * 检查
	 * @return
	 */
	void check(ISession session, IChannelData data);
}
