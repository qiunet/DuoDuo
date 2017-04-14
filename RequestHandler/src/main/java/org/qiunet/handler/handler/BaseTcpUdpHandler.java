package org.qiunet.handler.handler;

import org.apache.log4j.Logger;
import org.qiunet.handler.context.IContext;
import org.qiunet.handler.enums.HandlerType;
import org.qiunet.handler.iodata.net.AbstractRequestData;
import org.qiunet.handler.iodata.net.AbstractResponseData;
import org.qiunet.handler.response.IResponse;

/**
 * @author qiunet
 *         Created on 17/3/7 10:43.
 */
public abstract class BaseTcpUdpHandler<RequestData extends AbstractRequestData> implements ITcpUdpHandler<RequestData> {
	protected Logger logger = Logger.getLogger(getClass());
	
	private short requestId;
	@Override
	public short getRequestID() {
		return requestId;
	}

	@Override
	public HandlerType getHandlerType() {
		return HandlerType.TCP_UDP;
	}

	@Override
	public boolean offical() {
		return true;
	}

	@Override
	public boolean ignoreMaintain() {
		return false;
	}

	@Override
	public boolean needSid() {
		return true;
	}

	@Override
	public void handler(IContext context) {
		this.requestHandler((RequestData) context.getRequestData(), new FacadeResponse(context));
	}

	/**
	 * 处理tcp的请求
	 * @param requestData
	 * @param response
	 */
	protected abstract void requestHandler(RequestData requestData, IResponse response);

	/***
	 * 保护IContext  不让Handler 进行context处理
	 */
	private static class FacadeResponse implements IResponse{
		private IResponse response;
		private FacadeResponse (IResponse response) {
			this.response = response;
		}
		@Override
		public void response(AbstractResponseData responseData) {
			this.response.response(responseData);
		}
	} 
}
