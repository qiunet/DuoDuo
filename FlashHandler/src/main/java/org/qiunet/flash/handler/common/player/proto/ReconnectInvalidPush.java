package org.qiunet.flash.handler.common.player.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 * 断线重连数据已经无效.
 * 需要返回重走登录流程
 * @author qiunet
 * 2021/11/5 09:26
 */
@ChannelData(ID = IProtocolId.System.RECONNECT_INVALID_PUSH, desc = "断线重连数据已经无效,重走登录流程")
public class ReconnectInvalidPush implements IChannelData {
	@Ignore
	private static final ReconnectInvalidPush instance = new ReconnectInvalidPush();

	public static ReconnectInvalidPush getInstance() {
		return instance;
	}
}
