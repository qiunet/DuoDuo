package org.qiunet.flash.handler.netty.server.kcp.event;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.player.event.UserEventData;

/***
 *
 * 可用性推送
 *
 * @author qiunet
 * 2022/5/19 15:09
 */
public class KcpUsabilityEvent extends UserEventData {
	@Protobuf
	private boolean prepare;


	public static KcpUsabilityEvent valueOf(boolean prepare) {
		KcpUsabilityEvent data = new KcpUsabilityEvent();
		data.prepare = prepare;
		return data;
	}

	public boolean isPrepare() {
		return prepare;
	}
}
