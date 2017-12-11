package org.qiunet.test.testcase.http;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.test.robot.IRobot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 *  一个http protobuf 类型的测试用例
 * Created by qiunet.
 * 17/12/8
 */
public abstract class HttpProtobufTestCase<RequestData extends GeneratedMessageV3, ResponseData extends GeneratedMessageV3, Robot extends IRobot> extends BaseHttpTestCase<RequestData, ResponseData, Robot> {
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
		ResponseData responseData = null;
		try {
			Method method = responseDataClass.getMethod("parseFrom", byte[].class);
			responseData = (ResponseData) method.invoke(null, new Object[]{content.bytes()});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		this.responseData(robot, responseData);
	}
}
