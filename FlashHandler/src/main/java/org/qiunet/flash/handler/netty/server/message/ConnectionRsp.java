package org.qiunet.flash.handler.netty.server.message;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * @author qiunet
 * 2022/7/15 17:03
 */

@ChannelData(ID = IProtocolId.System.CONNECTION_RSP, desc = "Connection 响应")
public class ConnectionRsp implements IChannelData {
	@Ignore
	private static final ConnectionRsp instance = new ConnectionRsp();

	public static ConnectionRsp getInstance() {
		return instance;
	}
}
