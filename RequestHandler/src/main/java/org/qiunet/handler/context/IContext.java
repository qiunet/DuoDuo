package org.qiunet.handler.context;

import org.qiunet.handler.handler.IHandler;
import org.qiunet.handler.handler.acceptor.Acceptor;
import org.qiunet.handler.iodata.net.AbstractRequestData;
import org.qiunet.handler.response.IResponse;
import org.qiunet.utils.nonSyncQuene.QueueElement;

/**
 *  处理请求的封装类
 * @author qiunet
 *         Created on 17/3/13 19:48.
 */
public interface IContext extends QueueElement, IResponse {
	/**
	 * 把Intercepter 设置进来.
	 * @param acceptor
	 */
	public void setAcceptor(Acceptor acceptor);

	/**
	 * 得到Ihandler
	 * @return
	 */
	public IHandler getHandler();
	/**
	 * 得到请求数据
	 * @return
	 */
	public AbstractRequestData getRequestData();
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
