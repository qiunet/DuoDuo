package org.qiunet.test.testcase.http;

import io.netty.handler.codec.http.HttpResponseStatus;
import okhttp3.Response;
import org.qiunet.flash.handler.netty.client.param.HttpClientParams;
import org.qiunet.test.robot.IRobot;
import org.qiunet.utils.http.HttpRequest;
import org.qiunet.utils.http.IResultSupplier;

import java.io.IOException;

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
		Response response = HttpRequest.post(((HttpClientParams) getServer().getClientConfig()).getURI(getUriPath()))
			.withBytes(buildRequest(robot)).executor(IResultSupplier.identity());
		if (response == null) {
			robot.brokeRobot("http response is null .server maybe was shutdown!");
			return;
		}

		if (response.code() != HttpResponseStatus.OK.code()) {
			robot.brokeRobot("http status not 200");
			return;
		}
		try {
			responseData(robot, response.body().bytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 下行
	 * @param bytes
	 * @return
	 */
	protected abstract void responseData(Robot robot, byte [] bytes);
}
