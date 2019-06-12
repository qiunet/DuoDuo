package org.qiunet.test.testcase.http;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.netty.client.http.NettyHttpClient;
import org.qiunet.flash.handler.netty.client.param.HttpClientParams;
import org.qiunet.test.robot.IRobot;

/**
 * Created by qiunet.
 * 18/1/30
 */
abstract class BaseUriHttpTestCase<RequestData, ResponseData, Robot extends IRobot> extends AbstractHttpTestCase<RequestData,ResponseData, Robot> {
	/***
	 *
	 * @return
	 */
	public abstract String getUriPath();
	/***
	 * 得到一个请求数据
	 * @param robot
	 * @return
	 */
	protected  abstract byte[] buildRequest(Robot robot);
	@Override
	public void sendRequest(Robot robot) {
		FullHttpResponse httpResponse = NettyHttpClient.create(((HttpClientParams) getServer().getClientConfig()))
			.sendRequest(new MessageContent(0, buildRequest(robot)), this.getUriPath());
		if (httpResponse == null) {
			robot.brokeRobot("http response is null .server maybe was shutdown!");
			return;
		}

		if (! httpResponse.status().equals(HttpResponseStatus.OK)) {
			robot.brokeRobot("http status not 200");
			return;
		}
		byte [] bytes = new byte[httpResponse.content().readableBytes()];
		httpResponse.content().readBytes(bytes);
		responseData(robot, bytes);
	}
	/**
	 * 下行
	 * @param bytes
	 * @return
	 */
	protected abstract void responseData(Robot robot, byte [] bytes);
}
