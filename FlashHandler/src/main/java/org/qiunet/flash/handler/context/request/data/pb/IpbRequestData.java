package org.qiunet.flash.handler.context.request.data.pb;


import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;

/***
 * protobuf的请求接口
 *
 * @author qiunet
 * 2020-09-21 15:17
 */
public interface IpbRequestData extends IpbChannelData {

	@Override
	default int protocolId() {
		return RequestHandlerMapping.getInstance().getProtocolId(this.getClass());
	}
}
