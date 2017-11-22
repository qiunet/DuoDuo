package org.qiunet.flash.handler.handler.tcp;

import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.proto.TcpProtobufHandler;
import org.qiunet.flash.handler.handler.proto.LoginProto;

/**
 * Created by qiunet.
 * 17/8/16
 */
public class LoginProtobufHandler extends TcpProtobufHandler<LoginProto.LoginRequest> {

	@Override
	public void handler(ITcpRequest<LoginProto.LoginRequest> context) {
		context.getRequestData();
	}
}
