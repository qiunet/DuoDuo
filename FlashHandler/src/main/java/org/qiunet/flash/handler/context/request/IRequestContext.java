package org.qiunet.flash.handler.context.request;

import org.qiunet.flash.handler.handler.IHandler;


/**
 *  处理请求的封装类
 *  数据封装成context 在整个流程流转.
 * @author qiunet
 *         Created on 17/3/13 19:48.
 */
public interface IRequestContext<RequestData> extends IRequest<RequestData> {
	/**
	 * 得到Ihandler
	 * @return
	 */
	 IHandler<RequestData> getHandler();

	/**
	 * 处理请求
	 */
	 void handlerRequest() throws Exception;
}
