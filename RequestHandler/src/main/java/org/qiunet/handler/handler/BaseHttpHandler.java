package org.qiunet.handler.handler;

import org.apache.log4j.Logger;
import org.qiunet.handler.enums.HandlerType;
import org.qiunet.handler.iodata.net.AbstractRequestData;

/**
 * http的基础类. 所有http的handler 继承这个类
 * @author qiunet
 *         Created on 17/3/6 20:57.
 */
public abstract class BaseHttpHandler<RequestData extends AbstractRequestData> implements IHttpHandler<RequestData> {
	protected Logger logger = Logger.getLogger(getClass());
	/**使用反射赋值, 避免被随意的修改*/
	private short requestId;

	@Override
	public HandlerType getHandlerType() {
		return HandlerType.HTTP;
	}
	@Override
	public short getRequestID() {
		return requestId;
	}
	@Override
	public boolean offical() {
		return true;
	}

	@Override
	public boolean ignoreMaintain() {
		return false;
	}

	@Override
	public boolean needSid() {
		return true;
	}
	@Override
	public boolean needRecodeData() {
		return false;
	}
	@Override
	public boolean fastRequestControl() {
		return false;
	}
}
