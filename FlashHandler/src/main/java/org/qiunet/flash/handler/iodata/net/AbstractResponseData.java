package org.qiunet.flash.handler.iodata.net;

/**
 * 在业务中.还需要自己实现一个BaseRequestData. 返回CommonClass HeaderClass
 * @author qiunet
 *         Created on 17/3/3 12:06.
 */
public abstract class AbstractResponseData<HEADER extends IoData, COMMON extends IoData> extends BaseIoData<HEADER, COMMON>{

	protected AbstractResponseData(HEADER header, COMMON common, short responseId) {
		super(header, common);
		getLeader().setCmdId(responseId);
	}
}
