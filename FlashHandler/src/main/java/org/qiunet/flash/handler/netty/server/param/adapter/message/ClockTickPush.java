package org.qiunet.flash.handler.netty.server.param.adapter.message;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.utils.date.DateUtil;

/***
 *
 * @author qiunet
 * 2022/7/13 17:43
 */
@SkipDebugOut
@ChannelData(ID = IProtocolId.System.CLOCK_TICK_PUSH, desc = "时钟同步")
public class ClockTickPush implements IChannelData {
	@Protobuf(description = "时间戳(秒)")
	private int ts;

	public static ClockTickPush valueOf() {
		ClockTickPush data = new ClockTickPush();
		data.ts = (int) DateUtil.currSeconds();
		return data;
	}

	public int getTs() {
		return ts;
	}

	public void setTs(int ts) {
		this.ts = ts;
	}
}
