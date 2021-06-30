package org.qiunet.test.testcase.http;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;
import org.qiunet.test.robot.IRobot;
import org.qiunet.utils.protobuf.ProtobufDataManager;

import java.lang.reflect.ParameterizedType;

/**
 *  一个http protobuf 类型的测试用例
 * Created by qiunet.
 * 17/12/8
 */
public abstract class HttpProtobufTestCase<RequestData extends IpbRequestData, ResponseData extends IpbChannelData, Robot extends IRobot> extends BaseHttpTestCase<RequestData, ResponseData, Robot> {
	private Class<ResponseData> responseDataClass;
	public HttpProtobufTestCase(){
		Class clazz = getClass();
		do {
			if (! (clazz.getGenericSuperclass() instanceof ParameterizedType)) {
				clazz = clazz.getSuperclass();
				continue;
			}

			this.responseDataClass = (Class) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[1];
			break;
		}while (clazz != Object.class);
	}
	@Override
	public MessageContent buildRequest(Robot robot) {
		RequestData requestData = requestBuild(robot);
		byte [] bytes = requestData.toByteArray();
		MessageContent content = new MessageContent(getRequestID() , bytes);
		return content;
	}

	@Override
	public void responseData(Robot robot, MessageContent content) {
		ResponseData responseData = ProtobufDataManager.decode(this.responseDataClass, content.bytes());
		this.responseData(robot, responseData);
	}
}
