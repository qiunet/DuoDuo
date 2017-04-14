package org.qiunet.handler.iodata.net;

import org.qiunet.handler.iodata.base.InputByteStream;
import org.qiunet.handler.iodata.base.OutputByteStream;

/**
 * 在业务中.还需要自己实现一个BaseRequestData. 返回CommonClass HeaderClass
 * @author qiunet
 *         Created on 17/3/3 12:06.
 */
public abstract class AbstractRequestData<HEADER extends IoData, COMMON extends IoData> extends BaseIoData<HEADER, COMMON> {
	
	protected AbstractRequestData(HEADER header, COMMON common) {
		super(header, common);
	}
}
