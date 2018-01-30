package org.qiunet.test.testcase.http;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.netty.client.http.NettyHttpClient;
import org.qiunet.test.robot.IRobot;
import org.qiunet.test.server.IServer;
import org.qiunet.test.testcase.ITestCase;

/**
 * Created by qiunet.
 * 17/12/4
 */
public abstract class BaseHttpTestCase<RequestData, ResponseData, Robot extends IRobot> implements ITestCase<Robot> {
	/***
	 * 请求id
	 * @return
	 */
	protected abstract int getRequestID();
	/***
	 * 得到当前的server数据
	 * @return
	 */
	protected abstract IServer getServer();
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
		new ProtocolHeader(httpResponse.content());
		byte [] bytes = new byte[httpResponse.content().readableBytes()];
		httpResponse.content().readBytes(bytes);
		content = new MessageContent(0, bytes);
		responseData(robot, content);
	}

	@Override
	public boolean cancelIfConditionMiss() {
		return false;
	}
	/**
	 *
	 * @param bytes
	 * @return
	 */
	protected abstract void responseData(Robot robot, MessageContent bytes);

	/**
	 * 下层的requestBuild
	 * @param robot
	 * @return
	 */
	protected abstract RequestData requestBuild(Robot robot);

	/**
	 * 下层的响应
	 * @param robot
	 * @param responseData
	 */
	protected abstract void responseData(Robot robot, ResponseData responseData);
}
