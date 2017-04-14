package org.qiunet.handler.handler;

import org.qiunet.handler.annotation.RequestHandler;
import org.qiunet.handler.iodata.entitys.LoginRequestData;
import org.qiunet.handler.iodata.entitys.LoginResponseData;
import org.qiunet.handler.iodata.net.AbstractResponseData;

/**
 * Created by qiunet on 3/20/17.
 */
@RequestHandler(ID=101)
public class Login2Handler extends BaseHttpHandler<LoginRequestData> {
	@Override
	public AbstractResponseData handler(LoginRequestData requestData) {
		LoginResponseData responseData = new LoginResponseData();
		responseData.setSid("qiuyang");
		responseData.setUid(1000);
		return responseData;
	}

	@Override
	public LoginRequestData getRequestDataObj() {
		return new LoginRequestData();
	}
}
