package org.qiunet.flash.handler.context.request;

import org.qiunet.flash.handler.context.request.attribute.IAttributeData;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.utils.nonSyncQuene.QueueElement;

/**
 *  处理请求的封装类
 *  数据封装成context 在整个流程流转.
 * @author qiunet
 *         Created on 17/3/13 19:48.
 */
public interface IRequest<RequestData> extends QueueElement, IAttributeData {
	/**
	 * 得到Ihandler
	 * @return
	 */
	public IHandler getHandler();
	/**
	 * 得到请求数据
	 * @return
	 */
	public RequestData getRequestData();
	/**
	 * 得到请求序列
	 * @return
	 */
	public int getSequence();
}
