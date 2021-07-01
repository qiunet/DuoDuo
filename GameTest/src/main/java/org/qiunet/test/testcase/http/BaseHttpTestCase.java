package org.qiunet.test.testcase.http;

import io.netty.handler.codec.http.HttpResponseStatus;
import okhttp3.Response;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.netty.client.param.HttpClientParams;
import org.qiunet.test.robot.IRobot;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.http.HttpRequest;
import org.qiunet.utils.http.IResultSupplier;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by qiunet.
 * 17/12/4
 */
abstract class BaseHttpTestCase<RequestData, ResponseData, Robot extends IRobot> extends AbstractHttpTestCase<RequestData, ResponseData, Robot> {
	@Override
	public void sendRequest(Robot robot) {
		MessageContent content = buildRequest(robot);
		Response response;
		response = HttpRequest.post(((HttpClientParams) getServer().getClientConfig()).getURI())
			.withBytes(getServer().getClientConfig().getProtocolHeaderAdapter().getAllBytes(content))
			.executor(IResultSupplier.identity());

		if (response == null) {
			robot.brokeRobot("http response is null .server maybe was shutdown!");
			return;
		}

		if (response.code() != HttpResponseStatus.OK.code()) {
			robot.brokeRobot("http status not 200");
			return;
		}

		ByteBuffer buffer;
		try {
			buffer = ByteBuffer.wrap(response.body().bytes());
		} catch (IOException e) {
			throw new CustomException(e, "http client response error!");
		}
		IProtocolHeader header = getServer().getClientConfig().getProtocolHeaderAdapter().newHeader(buffer);
		byte [] bytes = new byte[header.getLength()];
		buffer.get(bytes);
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
