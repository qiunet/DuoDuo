package org.qiunet.test.function.test.gm;

import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.function.gm.proto.req.GmDebugProtocolReq;
import org.qiunet.test.handler.bootstrap.TcpBootStrap;
import org.qiunet.test.handler.proto.GenderType;
import org.qiunet.test.handler.proto.ProtocolId;
import org.qiunet.test.handler.proto.TcpPbLoginRequest;
import org.qiunet.utils.json.JsonUtil;

/***
 *
 * @author qiunet
 * 2021/12/28 09:59
 */
public class GmDebugProtocolTest extends TcpBootStrap {

	@Test
	public void testDebug() {
		String text = "test [testTcpProtobuf]";
		TcpPbLoginRequest loginRequest = TcpPbLoginRequest.valueOf(text, text, 11, GenderType.FAMALE);
		String jsonData = JsonUtil.toJsonString(loginRequest);

		GmDebugProtocolReq gmDebugProtocolReq = GmDebugProtocolReq.valueOf(ProtocolId.Test.TCP_PB_LOGIN_REQ, jsonData);
		this.tcpClientConnector.sendMessage(gmDebugProtocolReq, true);
	}

	@Override
	protected void responseTcpMessage(DSession session, MessageContent data) {
		System.out.println("==response==");
	}


}
