package org.qiunet.test.testcase.http;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.DefaultProtocolHeader;
import org.qiunet.flash.handler.netty.client.http.NettyHttpClient;
import org.qiunet.test.robot.IRobot;

/**
 * Created by qiunet.
 * 17/12/4
 */
abstract class BaseHttpTestCase<RequestData, ResponseData, Robot extends IRobot> extends AbstractHttpTestCase<RequestData, ResponseData, Robot> {
	/***
	 * 请求id
	 * @return
	 */
	protected abstract int getRequestID();

	@Override
	public void sendRequest(Robot robot) {
		MessageContent content = buildRequest(robot);
		FullHttpResponse httpResponse = NettyHttpClient.sendRequest(content.encodeToByteBuf() , getServer().uri().toString());
		if (httpResponse == null) {
			robot.brokeRobot("http response is null .server maybe was shutdown!");
			return;
		}

		if (! httpResponse.status().equals(HttpResponseStatus.OK)) {
			robot.brokeRobot("http status not 200");
			return;
		}
		new DefaultProtocolHeader().parseHeader(httpResponse.content());
		byte [] bytes = new byte[httpResponse.content().readableBytes()];
		httpResponse.content().readBytes(bytes);
		content = new MessageContent(0, bytes);
		responseData(robot, content);
	}
	/***
	 * 得到一个请求数据
	 * @param robot
	 * @return
	 */
	protected abstract MessageContent buildRequest(Robot robot);
	/**
	 *
	 * @param bytes
	 * @return
	 */
	protected abstract void responseData(Robot robot, MessageContent bytes);
}
