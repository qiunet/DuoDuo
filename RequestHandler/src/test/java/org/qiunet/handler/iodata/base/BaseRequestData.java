package org.qiunet.handler.iodata.base;

import org.qiunet.handler.iodata.data.CommonRequestData;
import org.qiunet.handler.iodata.data.HeadRequestData;
import org.qiunet.handler.iodata.net.AbstractRequestData;

/**
 * @author qiunet
 *         Created on 17/3/10 17:56.
 */
public abstract class BaseRequestData extends AbstractRequestData<HeadRequestData, CommonRequestData> {

	protected BaseRequestData(){
		super(new HeadRequestData(), new CommonRequestData());
	}
}
