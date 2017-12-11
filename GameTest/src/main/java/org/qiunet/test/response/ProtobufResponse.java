package org.qiunet.test.response;

import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.test.robot.IRobot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * Created by qiunet.
 * 17/12/8
 */
public abstract class ProtobufResponse<ResponseData, Robot extends IRobot> implements ILongConnResponse<ResponseData, Robot> {
	private Class<ResponseData> responseDataClass;
	public ProtobufResponse(){
		Class clazz = getClass();
		do {
			if (! (clazz.getGenericSuperclass() instanceof ParameterizedType)) {
				clazz = clazz.getSuperclass();
				continue;
			}

			this.responseDataClass = (Class) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
			break;
		}while (clazz != Object.class);
	}
	@Override
	public void response(Robot robot, MessageContent content) {
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

		this.response(robot, responseData);
	}
}
