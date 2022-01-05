package org.qiunet.flash.handler.context.request.param.check;

import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 *参数检查
 *
 * @author qiunet
 * 2022/1/5 17:02
 */
public interface IParamCheck {
	/**
	 * 检查
	 * @return
	 */
	void check(IChannelData data);
}
