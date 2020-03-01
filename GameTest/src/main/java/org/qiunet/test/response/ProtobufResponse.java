package org.qiunet.test.response;

import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.test.robot.IRobot;

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
		ResponseData responseData = DataType.PROTOBUF.parseBytes(content.bytes(), this.responseDataClass);
		this.response(robot, responseData);
	}
}
