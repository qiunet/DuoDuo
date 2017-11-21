package org.qiunet.flash.handler.tcp.handler;

import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.proto.TcpProtobufHandler;
import org.qiunet.flash.handler.proto.LoginProto;

/**
 * Created by qiunet.
 * 17/8/16
 */
public class LoginProtobufHandler extends TcpProtobufHandler<LoginProto.LoginRequest> {

	@Override
	public void handler(ITcpRequest<LoginProto.LoginRequest> context) {

	}
}
