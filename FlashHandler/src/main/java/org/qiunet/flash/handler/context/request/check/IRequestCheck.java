package org.qiunet.flash.handler.context.request.check;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.request.data.IChannelData;

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
	void check(Channel channel, IChannelData data);
}
