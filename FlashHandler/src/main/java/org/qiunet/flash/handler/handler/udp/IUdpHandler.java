package org.qiunet.flash.handler.handler.udp;

import org.qiunet.flash.handler.context.request.udp.IUdpRequest;
import org.qiunet.flash.handler.handler.IHandler;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/28 11:10
 **/
public interface IUdpHandler<RequestData> extends IHandler<RequestData> {
	/***
	 * 处理请求
	 * @param context 请求的内容上下文
	 * @throws Exception
	 */
	void haneler(IUdpRequest<RequestData> context)throws Exception;
}
