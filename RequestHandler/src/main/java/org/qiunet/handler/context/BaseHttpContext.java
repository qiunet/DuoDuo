package org.qiunet.handler.context;

import org.qiunet.handler.iodata.net.AbstractRequestData;
import org.qiunet.handler.iodata.net.AbstractResponseData;
import org.qiunet.utils.math.MathUtil;

/**
 * @author qiunet
 *         Created on 17/3/13 20:33.
 */
public abstract class BaseHttpContext extends BaseContext {
	protected BaseHttpContext(AbstractRequestData requestData) {
		super(requestData);
	}
}
