package org.qiunet.handler.mina.server.context;

import org.apache.mina.core.session.IoSession;
import org.qiunet.handler.context.BaseTcpUdpContext;
import org.qiunet.handler.iodata.net.AbstractRequestData;
import org.qiunet.handler.iodata.net.AbstractResponseData;

/**
 * @author qiunet
 *         Created on 17/3/13 19:56.
 */
public class MinaContext extends BaseTcpUdpContext {
	private IoSession ioSession;
	public MinaContext(AbstractRequestData requestData, IoSession session) {
		super(requestData);
		this.ioSession = session;
	}
	@Override
	public int getQueueHandlerIndex() {
		return (int) ioSession.getId();
	}

	@Override
	public void response(AbstractResponseData responseData) {
		ioSession.write(responseData);
	}
}
