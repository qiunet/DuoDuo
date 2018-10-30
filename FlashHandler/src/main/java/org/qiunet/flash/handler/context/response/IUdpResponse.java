package org.qiunet.flash.handler.context.response;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/30 20:47
 **/
public interface IUdpResponse<ResponseData> {
	/***
	 * 对客户端响应 默认是需要保证可靠性的
	 * @param responseData
	 */
	void udpResponse(int protocolId,  ResponseData responseData);
}
