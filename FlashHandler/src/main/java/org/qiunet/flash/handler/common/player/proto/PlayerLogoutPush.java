package org.qiunet.flash.handler.common.player.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;

/***
 *
 * @author qiunet
 * 2021/11/5 09:26
 */
@ChannelData(ID = IProtocolId.System.PLAYER_LOGOUT_PUSH, desc = "玩家登出推送")
public class PlayerLogoutPush implements IChannelData {
	@Protobuf(description = "关闭的原因")
	private CloseCause cause;
	@Protobuf(description = "是否可以重连")
	private boolean canReconnect;

	public static PlayerLogoutPush valueOf(CloseCause cause){
		PlayerLogoutPush data = new PlayerLogoutPush();
		data.canReconnect = cause.needWaitConnect();
		data.cause = cause;
		return data;
	}

	public boolean isCanReconnect() {
		return canReconnect;
	}

	public void setCanReconnect(boolean canReconnect) {
		this.canReconnect = canReconnect;
	}

	public void setCause(CloseCause cause) {
		this.cause = cause;
	}

	public CloseCause getCause() {
		return cause;
	}
}
