package org.qiunet.flash.handler.handler;

/**
 * http的处理
 * @author qiunet
 *         Created on 17/3/3 12:01.
 */
public interface IHttpHandler<RequestData, ResponseData> extends IHandler<RequestData> {
	/**
	 * 一般用于http协议. true的情况不允许频繁访问.
	 * @return
	 */
	public boolean fastRequestControl();

	/**
	 * 是否需要记录请求数据 当重复请求时候, 返回上次已经计算好的数据
	 * @return
	 */
	public boolean needRecodeData();
	/**
	 * http返回处理后的ResponseData
	 * @param requestData
	 * @return
	 */
	public ResponseData handler(RequestData requestData);
}
