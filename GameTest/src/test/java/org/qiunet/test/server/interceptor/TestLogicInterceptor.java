package org.qiunet.test.server.interceptor;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.handler.http.IHttpHandler;
import org.qiunet.flash.handler.netty.server.interceptor.HttpInterceptor;
import org.qiunet.test.proto.HeaderProto;

/**
 * Created by qiunet.
 * 17/12/9
 */
public class TestLogicInterceptor implements HttpInterceptor {
	@Override
	public Object handler(IHttpHandler handler, IHttpRequest request) {
		if (handler.needToken()) {

		}
		Object responseData = null;
		try {
			responseData = handler.handler(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseData;
	}
}
