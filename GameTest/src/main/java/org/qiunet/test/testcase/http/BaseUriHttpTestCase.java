package org.qiunet.test.testcase.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.qiunet.flash.handler.netty.bytebuf.PooledBytebufFactory;
import org.qiunet.flash.handler.netty.client.http.NettyHttpClient;
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
		StringBuilder sb = new StringBuilder();
		sb.append(getServer().uri().getScheme());
		sb.append("://").append(getServer().uri().getAuthority());
		if (! getUriPath().startsWith("/")) sb.append("/");
		sb.append(getUriPath());

		ByteBuf byteBuf = PooledBytebufFactory.getInstance().alloc(buildRequest(robot));
		FullHttpResponse httpResponse = NettyHttpClient.sendRequest(byteBuf , sb.toString());
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
