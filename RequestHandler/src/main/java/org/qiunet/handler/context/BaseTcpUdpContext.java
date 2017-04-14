package org.qiunet.handler.context;

import org.qiunet.handler.iodata.net.AbstractRequestData;

/**
 * @author qiunet
 *         Created on 17/3/15 17:54.
 */
public abstract class BaseTcpUdpContext extends BaseContext implements ITcpUdpContext {
	protected BaseTcpUdpContext(AbstractRequestData requestData) {
		super(requestData);
	}
}
