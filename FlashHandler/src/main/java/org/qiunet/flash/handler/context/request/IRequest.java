package org.qiunet.flash.handler.context.request;

import org.qiunet.flash.handler.context.request.attribute.IAttributeData;

/**
 * requestdata 默认封装给handler的东西
 * Created by qiunet.
 * 17/11/21
 */
public interface IRequest<RequestData> extends IAttributeData {
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
	/**
	 * 得到远程的Ip地址
	 * @return
	 */
	public String getRemoteAddress();
}
