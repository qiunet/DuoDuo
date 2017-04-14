package org.qiunet.handler.iodata.base;

import org.qiunet.handler.iodata.data.CommonResponseData;
import org.qiunet.handler.iodata.data.HeadResponseData;
import org.qiunet.handler.iodata.net.AbstractResponseData;

/**
 * @author qiunet
 *         Created on 17/3/10 17:59.
 */
public abstract class BaseResponseData extends AbstractResponseData <HeadResponseData, CommonResponseData> {
	
	protected BaseResponseData(short responseId) {
		super(new HeadResponseData(), new CommonResponseData(), responseId);
	}
}
