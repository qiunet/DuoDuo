package org.qiunet.flash.handler.handler;

import org.qiunet.flash.handler.common.enums.HandlerType;

/**
 * http的基础类
 * 如果需要处理protobuf 什么的, 可以自己继承该类 ,实现自己的父类.
 * Created by qiunet.
 * 17/7/21
 */
public abstract class BaseHttpHandler<RequestData, ResponseData> extends BaseHandler<RequestData> implements IHttpHandler<RequestData, ResponseData>{
	@Override
	public boolean fastRequestControl() {
		return true;
	}

	@Override
	public boolean needRecodeData() {
		return false;
	}

	@Override
	public HandlerType getHandlerType() {
		return HandlerType.HTTP;
	}
}
