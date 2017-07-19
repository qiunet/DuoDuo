package org.qiunet.flash.handler.context;

import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.utils.nonSyncQuene.QueueElement;

/**
 *  处理请求的封装类
 *  数据封装成context 在整个流程流转.
 * @author qiunet
 *         Created on 17/3/13 19:48.
 */
public interface IContext<RequestData> extends QueueElement {
	/**
	 * 得到Ihandler
	 * @return
	 */
	public IHandler<RequestData> getHandler();
	/**
	 * 得到请求数据
	 * @return
	 */
	public RequestData getRequestData();
	/**
	 * 得到对象
	 * @param key
	 * @return
	 */
	public Object getAttribute(String key);
	/**
	 * 得到对象
	 * @param key
	 * @return
	 */
	public void setAttribute(String key, Object val);

}
