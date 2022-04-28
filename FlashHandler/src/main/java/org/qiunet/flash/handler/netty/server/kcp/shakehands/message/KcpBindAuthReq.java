package org.qiunet.flash.handler.netty.server.kcp.shakehands.message;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * @author qiunet
 * 2022/4/27 11:27
 */
@ChannelData(ID = IProtocolId.System.KCP_BIND_AUTH_REQ, desc = "KCP和其它长连接绑定鉴权请求")
public class KcpBindAuthReq implements IChannelData {
	@Protobuf(description = "玩家ID")
	private long playerId;
	@Protobuf(description = "KcpTokenRsp下发的TOKEN")
	private String token;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
