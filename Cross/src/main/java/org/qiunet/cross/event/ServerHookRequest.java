package org.qiunet.cross.event;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * @author qiunet
 * 2021/11/20 14:24
 */
@ChannelData(ID = IProtocolId.System.HOOK_MESSAGE, desc = "服务器的钩子")
public class ServerHookRequest implements IChannelData {
	@Protobuf(description = "处理内容")
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
