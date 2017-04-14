package org.qiunet.handler.handler;

import org.qiunet.handler.annotation.RequestHandler;
import org.qiunet.handler.iodata.entitys.LoginRequestData;
import org.qiunet.handler.iodata.entitys.LoginResponseData;
import org.qiunet.handler.iodata.net.AbstractRequestData;
import org.qiunet.handler.response.IResponse;

/**
 * @author qiunet
 *         Created on 17/3/16 11:49.
 */
@RequestHandler(ID=100)
public class LoginHandler extends BaseTcpUdpHandler<LoginRequestData> {
	@Override
	public LoginRequestData getRequestDataObj() {
		return new LoginRequestData();
	}
	@Override
	protected void requestHandler(LoginRequestData requestData, IResponse response) {
		logger.info(requestData);

		LoginResponseData responseData = new LoginResponseData();
		responseData.setUid(1000);
		responseData.setSid("qiuyang");
		
		response.response(responseData);
	}
}
