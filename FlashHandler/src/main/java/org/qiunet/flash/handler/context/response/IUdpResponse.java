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
	public void udpResponse(int protocolId,  ResponseData responseData);
	/***
	 * 对客户端响应
	 * @param importtantMsg 是否是需要保证可靠性的消息. true 需要 false 不需要
	 * @param responseData
	 */
	public void udpResponse(int protocolId, boolean importtantMsg,  ResponseData responseData);
}
