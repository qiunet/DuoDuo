package org.qiunet.test.response;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.test.robot.IRobot;
import org.qiunet.utils.protobuf.ProtobufDataManager;

import java.lang.reflect.ParameterizedType;

/**
 * Created by qiunet.
 * 17/12/8
 */
public abstract class ProtobufResponse<ResponseData, Robot extends IRobot> implements IPersistConnResponse<ResponseData, Robot> {
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
		ResponseData responseData = ProtobufDataManager.decode(this.responseDataClass, content.bytes());
		this.response(robot, responseData);
	}
}
